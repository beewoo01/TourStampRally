package com.sdin.tourstamprally.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.LocationReAdapter
import com.sdin.tourstamprally.databinding.DialogQrScanWanttogoBinding
import com.sdin.tourstamprally.model.TaggingnoCourseDTO
import com.sdin.tourstamprally.ui.fragment.auth.QRScanFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class QRScanWanttogoDialog(
    context: Context,
    private val list: List<TaggingnoCourseDTO>,
    private val callback : (Boolean) -> Unit,
    private val datacallback: (String) -> Unit
) :
    BaseDialog(context, R.style.FullScreenDialogStyle) {
    private var binding: DialogQrScanWanttogoBinding? = null
    private var qrscan = QRScanFragment()
    private lateinit var recyclerAdapter : LocationReAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_qr_scan_wanttogo,
            null,
            false
        )
        setContentView(binding?.root!!)

        window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.AnimationPopupStyle
        }
        recyclerAdapter = LocationReAdapter(context,list)

        binding?.locationRe?.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding?.locationRe?.adapter = recyclerAdapter
        val spaceDeco = VerticalSpaceItemDecoration(30)
        binding?.locationRe?.addItemDecoration(spaceDeco)

        recyclerAdapter.setOnItemClickListener(object : LocationReAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: TaggingnoCourseDTO, pos: Int, tag: String?) {
                Log.wtf("setOnItemClickListener",tag)
                getCourse(data)
            }
        })

        initView()
    }


    private fun getCourse(model : TaggingnoCourseDTO){
        apiService.insertCurrentCourse(model.touristspot_idx, Utils.User_Idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(result: Int) {
                    if (result > 0) {
                        Log.wtf("getCourseonSuccess","success")

                    } else {
                        Log.wtf("getCourseonSuccess","fail")
                    }
                    dismiss()
                    datacallback(model.test_touristspotpoint_tagging_info)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    dismiss()
                    datacallback(model.test_touristspotpoint_tagging_info)
                }

            })
    }
    inner class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }
    private fun initView() = with(binding!!){

        closeImb.setOnClickListener {
            callback(false)
            dismiss()
        }
    }

    private fun setIcon(position: Int) : Int{
        return when(position){
            0-> 0
            1 -> R.drawable.ic_main_roadtour
            2 -> R.drawable.ic_main_hardtour
            3 -> R.drawable.ic_main_trakingtour
            4 -> R.drawable.ic_main_findtreasure
            5 -> R.drawable.ic_main_festivaltour
            6 -> R.drawable.ic_main_webtoontour
            7 -> R.drawable.ic_main_historytour
            else -> 0

        }
    }
}
