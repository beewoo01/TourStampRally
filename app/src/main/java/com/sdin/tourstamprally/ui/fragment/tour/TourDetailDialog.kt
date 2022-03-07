package com.sdin.tourstamprally.ui.fragment.tour

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.TourDetailImageViewPagerAdapter
import com.sdin.tourstamprally.databinding.DialogDetailBinding
import com.sdin.tourstamprally.model.TouristSpotPointDC
import com.sdin.tourstamprally.model.TouristSpotPointImg
import com.sdin.tourstamprally.ui.dialog.BaseDialog

class TourDetailDialog(context: Context, val touristSpotPoint: TouristSpotPointDC) :
    BaseDialog(context, R.style.FullScreenDialogStyle) {

    private var binding: DialogDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_detail,
            null,
            false
        )

        setContentView(binding?.root!!)

        binding?.dialog = this

        window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.AnimationPopupStyle
        }

        initView()

    }

    private fun initView() {
        binding?.apply {
            titleTxv.text = touristSpotPoint.touristspotpoint_name
            contentTxv.movementMethod = ScrollingMovementMethod()
            if (!touristSpotPoint.touristspotpoint_detail_explan.isNullOrEmpty()) {
                contentTxv.text = touristSpotPoint.touristspotpoint_detail_explan
            } else {
                contentTxv.visibility = View.GONE
            }

            closeBtn.setOnClickListener {
                binding = null
                dismiss()

            }

            imageViewPager.adapter = TourDetailImageViewPagerAdapter().apply {
                touristSpotPoint.touristSpotPointImgModelList?.let {
                    if (it.isNotEmpty()) {
                        submitList(it)
                    } else {
                        val list = listOf(touristSpotPoint.touristSpotPointCurverImg?.let { it1 ->
                            TouristSpotPointImg(
                                0,
                                0,
                                it1
                            )
                        })
                        submitList(list)
                    }
                }
            }

        }
    }

    fun leftClick() {
        binding?.run {
            val position = imageViewPager.currentItem
            if (position > 0) {
                imageViewPager.currentItem = position - 1
            }
        }
    }

    fun rightClick() {
        binding?.run {
            val position = imageViewPager.currentItem
            if (position < imageViewPager.adapter?.itemCount!! - 1) {
                imageViewPager.currentItem = position + 1
            }
        }
    }

}