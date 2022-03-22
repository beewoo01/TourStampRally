package com.sdin.tourstamprally.ui.fragment.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.zxing.Result
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentQrScanBinding
import com.sdin.tourstamprally.model.RallyMapDTO
import com.sdin.tourstamprally.model.RallyMapModel
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.dialog.DialogFailTimeOver
import com.sdin.tourstamprally.ui.dialog.ScanResultPopup
import com.sdin.tourstamprally.ui.dialog.course.SelectCourseDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.ui.fragment.report.review.MoreReviewFragmentDirections
import com.sdin.tourstamprally.utill.GpsTracker
import com.sdin.tourstamprally.utill.listener.DialogListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class QRScanFragment : BaseFragment(), DialogListener {

    private var binding: FragmentQrScanBinding? = null
    private var codeScanner: CodeScanner? = null
    private val gpsTracker: GpsTracker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_scan, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        initView()

    }

    override fun onResume() {
        super.onResume()
        Log.wtf("QRScanFragment", "onResume")
        codeScanner?.startPreview()
    }

    override fun onPause() {
        super.onPause()
        Log.wtf("QRScanFragment", "onPause")
        codeScanner?.releaseResources()
    }

    private fun initView() = with(binding!!) {
        Log.wtf("QRScanFragment", "initView")
        codeScanner = CodeScanner(requireContext(), scannerView)
        codeScanner?.decodeCallback = DecodeCallback { result: Result ->
            requireActivity().runOnUiThread {
                Log.wtf("taginfo", result.text)
                checkIn(result.text)
            }
        }

        moveNfcBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_qr_scan_to_fragment_nfc)
        }

        getData()
    }

    private fun getData() {
        Log.wtf("QRFragment", "getData")
        apiService.haveCourseForStemp(Utils.User_Idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(integer: Int) {
                    Log.wtf("QRFragment", "haveCourseForStemp, onSuccess")
                    initGuid(integer)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    private fun initGuid(position: Int) {
        Log.wtf("QRFragment", "initGuid")
        when (position) {
            0 -> {
                //코스 선택 안함
                Log.wtf("QRFragment", "코스 선택 안함")
                getCourseInfo()
            }
            1 -> {
                Log.wtf("QRFragment", "코스 있음")
            }

            2 -> {
                //코스 시간 지남 POPUP
                Log.wtf("QRFragment", "코스 시간 지남")
                DialogFailTimeOver(requireContext()).show()
            }
        }
    }

    private fun getCourseInfo() {
        SelectCourseDialog(requireContext()).show()
    }

    private fun checkIn(taggingStr: String) {

        //final String finalText = "T05100AA028";
        //테스트용
        apiService.checkInStemp(Utils.User_Idx, taggingStr, Utils.UserPhone)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<HashMap<String, Int>>() {
                override fun onSuccess(map: HashMap<String, Int>) {
                    setCheckInResult(map)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }


    private fun setCheckInResult(map: HashMap<String, Int>) {
        val result = map["result"]
        val touristspotIdx = map["touristspot_idx"]
        when (result) {
            0 -> {
                // 인증성공 POPUP
                ScanResultPopup(requireContext(), 0, "QR", this@QRScanFragment) {
                    touristspotIdx?.let {
                        apiService.selectSpotSimpleInfo(it)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<RallyMapModel>() {
                                override fun onSuccess(result: RallyMapModel) {
                                    if (result != null) {
                                        moveFragment(result)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            })
                    }
                }.show()

            }
            1 -> { //코스 선택 안함 POPUP
                getCourseInfo()
            }

            2 -> {
                // 시간 오버 POPUP
                DialogFailTimeOver(requireContext()).show()
            }
            3 -> {
                // 태그 정보 잘못됨 POPUP
                ScanResultPopup(requireContext(), 1, "QR", this@QRScanFragment) {

                }.show()
            }
            4 -> {
                //이미 인증됨  POPUP
                ScanResultPopup(requireContext(), 2, "QR", this@QRScanFragment) {
                    touristspotIdx?.let {
                        apiService.selectSpotSimpleInfo(it)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<RallyMapModel>() {
                                override fun onSuccess(result: RallyMapModel) {
                                    if (result != null) {
                                        moveFragment(result)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            })
                    }

                    Log.wtf("ScanResultPopup", "touristspotIdx $touristspotIdx")
                }.show()

            }

            5 -> {

                // 현재 코스가 아님 POPUP
                DefaultBSRDialog(
                    requireContext(),
                    title = "현재 진행중인 코스를\n중단 하시겠습니까?",
                    content = "중간 시 해당 코스는 모두\n실패 처리가 됩니다.",
                    isSpecial = true,
                    isSwitchBtn = false,
                    leftBtnStr = "취소",
                    rightBtnStr = "중단"
                ) { isCancel: Boolean ->
                    if (!isCancel) {
                        apiService.removeMyCourse(Utils.User_Idx)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<Int>() {
                                override fun onSuccess(result: Int) {
                                    when (result) {
                                        0 -> {
                                            //정상적으로 삭제됨
                                        }
                                        1 -> {
                                            //기록 삭제 안됨
                                        }
                                        2 -> {
                                            //코스 삭제 안됨
                                        }
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            })
                    }

                }.show()
            }
            else -> {
                showToast("인증에 실패하였습니다. 관리자에게 문의해주세요 $result")
            }
        }
    }

    private fun moveFragment(model: RallyMapModel) {
        Log.wtf("moveFragment", model.touristspot_name)
        val action = QRScanFragmentDirections.checkSpotPointQr(
            title = model.touristspot_name,
            state = 1,
            rallyMapModel = model
        )

        findNavController().navigate(action)
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun distance_calculation(latitude: Double, longitude: Double): Boolean {
        // 거리계산 30M 내
        //35.174201, 128.983804
        //35.256003, 129.013801
        //35.174208, 128.983784
        val dice: Double = distance(latitude, longitude)
        return if (dice <= 30) {
            Log.wtf("distance_calculation", "30M이내")
            true
        } else {
            Log.wtf("distance_calculation", "30M넘음")
            false
        }
        //Log.wtf("distance", String.valueOf(dice));
    }

    private fun distance(latitude: Double, longitude: Double): Double {
        gpsTracker?.let {
            val userLatitude: Double = it.latitude
            val userLongitude: Double = it.longitude
            return Utils.distance(latitude, longitude, userLatitude, userLongitude)
        }
        return 0.0
    }

    override fun onDisMiss() {
        codeScanner?.startPreview()
    }

}