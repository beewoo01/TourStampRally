package com.sdin.tourstamprally.ui.dialog.course


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.course.SelectLocationForCurrentAdapter
import com.sdin.tourstamprally.databinding.DialogSelectCourseBinding
import com.sdin.tourstamprally.databinding.SelectCourseReItemBinding
import com.sdin.tourstamprally.model.course.SelectCourseModel
import com.sdin.tourstamprally.ui.dialog.BaseDialog
import com.sdin.tourstamprally.utill.recyclerveiew.CustomItemDecoration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class SelectCourseDialog(context: Context, val callback: (() -> Unit?)?) :
    BaseDialog(context, R.style.FullScreenDialogStyle) {

    private var binding: DialogSelectCourseBinding? = null
    private val originalList = mutableListOf<SelectCourseModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_select_course,
            null,
            false
        )

        setContentView(binding?.root!!)

        window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.AnimationPopupStyle
        }


        initView()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        callback?.invoke()
    }

    private fun initView() = with(binding!!) {
        closeImb.setOnClickListener {
            dismiss()
            callback?.invoke()
        }

        getData()

    }

    private fun getData() {
        apiService.selectNotClearCourse(Utils.User_Idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<SelectCourseModel>>() {
                override fun onSuccess(result: List<SelectCourseModel>) {
                    originalList.addAll(result.toMutableList())
                    initLocationRecyclerView(result)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun initLocationRecyclerView(list: List<SelectCourseModel>) {
        if (!list.isNullOrEmpty()) {
            val locationMap = HashMap<Int, String>()
            for (model in list) {
                locationMap[model.location_idx] = model.location_name
            }

            val locationList: List<Pair<Int, String>> = locationMap.toList()
            binding?.tourSpotRe?.visibility = View.GONE
            binding?.locationRecyclerview?.apply {
                adapter = SelectLocationForCurrentAdapter { item ->
                    setTourSpotAdapter(item.first)
                    item.first
                }.apply {
                    addItemDecoration(
                        CustomItemDecoration(
                            topPadding = 20, bottomPadding = 20, 20, 20
                        )
                    )

                    submitList(locationList)
                }
            }

        }
    }

    private fun setTourSpotAdapter(locationIdx: Int) {
        val list = mutableListOf<SelectCourseModel>()
        for (model in originalList) {
            if (locationIdx == model.location_idx) {
                list.add(model)
            }
        }

        binding?.run {
            locationRecyclerview.visibility = View.GONE
            tourSpotRe.visibility = View.VISIBLE
            topTxv.visibility = View.VISIBLE
            tourSpotRe.apply {
                adapter = SelectTourSpotForCurrentAdapter { model ->
                    apiService.insertCurrentCourse(model.touristspot_idx, Utils.User_Idx)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Int>() {
                            override fun onSuccess(result: Int) {
                                if (result > 0) {
                                    Toast.makeText(
                                        this@SelectCourseDialog.context,
                                        "코스등록에 성공하였습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(this@SelectCourseDialog.context, "관광지 등록에 실패하였습니다. 관리자에게 문의 해주세요.", Toast.LENGTH_SHORT).show()
                                }
                                dismiss()
                                callback?.invoke()
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                Toast.makeText(this@SelectCourseDialog.context, "관광지 등록에 실패하였습니다. 관리자에게 문의 해주세요.", Toast.LENGTH_SHORT).show()
                                dismiss()
                                callback?.invoke()
                            }

                        })

                }.apply {
                    submitList(list)
                }
            }
        }
    }


    class SelectTourSpotForCurrentAdapter(val callback: (SelectCourseModel) -> Unit) :
        ListAdapter<SelectCourseModel, SelectTourSpotForCurrentAdapter.ViewHolder>(differ) {

        inner class ViewHolder(private val binding: SelectCourseReItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun onBind(model: SelectCourseModel) = with(binding) {
                if (absoluteAdapterPosition % 2 > 0) {
                    Glide.with(locationImv.context).load(R.drawable.location_sky).into(locationImv)

                } else {
                    Glide.with(locationImv.context).load(R.drawable.location_puple)
                        .into(locationImv)
                }

                Glide.with(spotImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.touristspot_img)
                    .into(spotImv)

                spotName.text = model.touristspot_name
                addressTxv.text = model.touristspot_address
                topLayout.setOnClickListener {
                    callback(model)
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                SelectCourseReItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(currentList[position])
        }


        companion object {
            val differ = object : DiffUtil.ItemCallback<SelectCourseModel>() {
                override fun areItemsTheSame(
                    oldItem: SelectCourseModel,
                    newItem: SelectCourseModel
                ): Boolean {
                    return oldItem.touristspot_idx == newItem.touristspot_idx
                }

                override fun areContentsTheSame(
                    oldItem: SelectCourseModel,
                    newItem: SelectCourseModel
                ): Boolean {
                    return oldItem == newItem
                }


            }
        }

    }

}