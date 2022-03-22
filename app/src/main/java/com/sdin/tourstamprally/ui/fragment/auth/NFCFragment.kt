package com.sdin.tourstamprally.ui.fragment.auth

import android.graphics.Outline
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentNfcBinding
import com.sdin.tourstamprally.model.RallyMapModel
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.dialog.DialogFailTimeOver
import com.sdin.tourstamprally.ui.dialog.ScanResultPopup
import com.sdin.tourstamprally.ui.dialog.course.SelectCourseDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.ui.fragment.auth.NFCListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.experimental.and

class NFCFragment : BaseFragment(), NFCListener {

    private var binding: FragmentNfcBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nfc, container, false)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        if (nfcAdapter == null) {
            Toast.makeText(context, "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show()
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        initView()
    }

    private fun initView() = with(binding!!) {
        imageViewNfcBg.apply {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height + 200, 60f)
                }
            }
            clipToOutline = true
        }

        getData()

    }

    private fun buildtagViews(msgs: String?) {
        if (msgs == null || msgs.isEmpty()) return
        Log.wtf("NFCFragment", "buildtagViews $msgs")
        if (!msgs.isNullOrEmpty()) {
            checkIn(msgs)
        }
    }

    private fun getData() {
        Log.wtf("NFCFragment", "getData")
        apiService.haveCourseForStemp(Utils.User_Idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(integer: Int) {
                    Log.wtf("NFCFragment", "haveCourseForStemp, onSuccess")
                    initGuid(integer)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    private fun initGuid(position: Int) {
        Log.wtf("NFCFragment", "initGuid")
        when (position) {
            0 -> {
                //코스 선택 안함
                Log.wtf("NFCFragment", "코스 선택 안함")
                getCourseInfo()
            }
            1 -> {
                Log.wtf("NFCFragment", "코스 있음")
            }

            2 -> {
                //코스 시간 지남 POPUP
                Log.wtf("NFCFragment", "코스 시간 지남")
                DialogFailTimeOver(requireContext()).show()
            }
        }
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

    private fun setCheckInResult(map: java.util.HashMap<String, Int>) {
        val result = map["result"]
        val touristspotIdx = map["touristspot_idx"]
        when (result) {
            0 -> {
                // 인증성공 POPUP
                ScanResultPopup(requireContext(), 0, "NFC", null) {
                    Log.wtf("ScanResultPopup", "touristspotIdx $touristspotIdx")
                    touristspotIdx?.let {
                        apiService.selectSpotSimpleInfo(it)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<RallyMapModel>() {
                                override fun onSuccess(result: RallyMapModel) {
                                    if (result != null) {
                                        val action = NFCFragmentDirections.checkSpotPointNfc(
                                            title = result.touristspot_name,
                                            state = 1,
                                            rallyMapModel = result
                                        )

                                        findNavController().navigate(action)
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
                ScanResultPopup(requireContext(), 1, "NFC", null) {

                }.show()
            }
            4 -> {
                //이미 인증됨  POPUP
                ScanResultPopup(requireContext(), 2, "NFC", null) {
                    touristspotIdx?.let {
                        apiService.selectSpotSimpleInfo(it)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<RallyMapModel>() {
                                override fun onSuccess(result: RallyMapModel) {
                                    if (result != null) {
                                        val action = NFCFragmentDirections.checkSpotPointNfc(
                                            title = result.touristspot_name,
                                            state = 1,
                                            rallyMapModel = result
                                        )

                                        findNavController().navigate(action)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            })
                    }
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

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun getCourseInfo() {
        SelectCourseDialog(requireContext()).show()
    }

    override fun onReadTag(action: String) {
        buildtagViews(action)
    }
}