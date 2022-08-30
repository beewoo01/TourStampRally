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
import com.sdin.tourstamprally.model.MapMarkerNumDTO
import com.sdin.tourstamprally.model.RallyMapModel
import com.sdin.tourstamprally.model.TaggingnoCourseDTO
import com.sdin.tourstamprally.model.course.SelectCourseModel
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.dialog.DialogFailTimeOver
import com.sdin.tourstamprally.ui.dialog.QRScanWanttogoDialog
import com.sdin.tourstamprally.ui.dialog.ScanResultPopup
import com.sdin.tourstamprally.ui.dialog.course.SelectCourseDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.GpsTracker
import com.sdin.tourstamprally.utill.listener.DialogListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QRScanFragment : BaseFragment(), DialogListener {

    private var binding: FragmentQrScanBinding? = null
    private var codeScanner: CodeScanner? = null
    private val gpsTracker: GpsTracker? = null

    private var taginfo : String? = null
    private var checkpop : Boolean = false
    private var nocourse : Boolean = false

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
                taginfo = result.text

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
//                getCourseInfo() //주석
//                getCourse()
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
        checkpop = true
        SelectCourseDialog(requireContext(),) {
            checkpop = false
            codeScanner?.startPreview()
        }.show()
    }

    fun checkIn(taggingStr: String) {

        Log.wtf("checkIn", "taggingStr? $taggingStr" )
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

                Log.wtf("setCheckInResult","0")
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

                /*if(!checkpop){

                    getCourseInfo()
                }*/
                getCourse()
//                getCourseInfo() //주석
                //여기에 팝업 들어가면될듯
                Log.wtf("setCheckInResult","1")
                /*apiService.hasTaggingInfonocourse(taginfo).enqueue(object : Callback<List<TaggingnoCourseDTO>>{
                    override fun onResponse(
                        call: Call<List<TaggingnoCourseDTO>>,
                        response: Response<List<TaggingnoCourseDTO>>
                    ) {
                        if (response.isSuccessful){
                            val result = response.body()
                            if(result?.size!! > 1){
                                Log.wtf("resultsize",result?.size.toString())
                                result?.let {
                                    QRScanWanttogoDialog(requireContext(),result, callback = {} ){
                                        //선택된 tag 받아와서 checkin 쓰면될듯
                                        checkIn(it)
                                    }.show()
                                }
                                *//*codeScanner?.startPreview()*//*
                            }
                            else{
                                getCourseInfo()
                            }

                        }else{
                            getCourseInfo()
                        }
                    }

                    override fun onFailure(call: Call<List<TaggingnoCourseDTO>>, t: Throwable) {
                        t.printStackTrace()
                    }

                })*/

            }

            2 -> {
                // 시간 오버 POPUP

                DialogFailTimeOver(requireContext()).show()
            }
            3 -> {
                // 태그 정보 잘못됨 POPUP
                Log.wtf("setCheckInResult","1")
                ScanResultPopup(requireContext(), 1, "QR", this@QRScanFragment) {

                }.show()
            }
            4 -> {
                //이미 인증됨  POPUP
                Log.wtf("setCheckInResult","2")
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
                    title = "\n현재 코스에 없는 스탬프 입니다.\n코스를 변경하시겠습니까?",
                    content = "중단 시 진행중인 코스는\n완주실패 처리가 됩니다.\n\n",
                    isSpecial = true,
                    isSwitchBtn = false,
                    leftBtnStr = "취소",
                    rightBtnStr = "변경"
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
//                                            codeScanner?.startPreview()
                                            getCourse()
                                        }
                                        1 -> {
                                            //기록 삭제 안됨
                                        }
                                        2 -> {
                                            //코스 삭제 안됨
                                        }
                                    }

//                                    codeScanner?.startPreview()
                                }

                                override fun onError(e: Throwable) {
                                    codeScanner?.startPreview()
                                    e.printStackTrace()
                                }
                            })
                    }
                    else{
                        codeScanner?.startPreview()
                    }

                }.show()
            }

            else -> {
                showToast("인증에 실패하였습니다. 관리자에게 문의해주세요 $result")
            }
        }
    }

    private fun overlapCourse(){
        apiService.hasTaggingInfonocourse(taginfo).enqueue(object : Callback<List<TaggingnoCourseDTO>>{
            override fun onResponse(
                call: Call<List<TaggingnoCourseDTO>>,
                response: Response<List<TaggingnoCourseDTO>>
            ) {
                if (response.isSuccessful){
                    val result = response.body()
                    if(result?.size!! > 1){
                        Log.wtf("resultsize",result?.size.toString())
                        result?.let {
                            QRScanWanttogoDialog(requireContext(),result, callback = {codeScanner?.startPreview()} ){
                                //선택된 tag 받아와서 checkin 쓰면될듯
                                checkIn(it)
                            }.show()
                        }
//                                codeScanner?.startPreview()
                    }

                }
            }

            override fun onFailure(call: Call<List<TaggingnoCourseDTO>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun getCourse(){
        Log.wtf("getCourse", taginfo)
        apiService.selectTouristspotidx(Utils.User_Idx, taginfo).enqueue(object : Callback<List<MapMarkerNumDTO>>{
            override fun onResponse(
                call: Call<List<MapMarkerNumDTO>>,
                response: Response<List<MapMarkerNumDTO>>
            ) {
                val result = response.body()
                Log.wtf("getCourse", result?.size?.toString())
                if(result?.size!! > 1){
                    overlapCourse()
                }else if(result?.size!! == 0){
                    Toast.makeText(requireContext(), "이미 완료된 코스입니다.", Toast.LENGTH_SHORT).show()
//                    getCourseInfo()
                    codeScanner?.startPreview()
                    Log.wtf("getCourse", "nocourseeeeeeeeeeeeeeee")
                }
                else{
                    Log.wtf("getCourse",result.get(0).touristspot_point_sub_touristspot_idx.toString())
                    insertcourse(result.get(0).touristspot_point_sub_touristspot_idx)
                }
            }

            override fun onFailure(call: Call<List<MapMarkerNumDTO>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
    private fun insertcourse(idx : Int){

        apiService.insertCurrentCourse(idx, Utils.User_Idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(result: Int) {
                    if (result > 0) {
                        Log.wtf("insertcourse","success")
                        Log.wtf("insertcourse", taginfo)
                        checkIn(taginfo!!)

                    } else {
                        Log.wtf("insertcourse","fail")
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
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