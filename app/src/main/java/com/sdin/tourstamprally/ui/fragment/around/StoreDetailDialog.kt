package com.sdin.tourstamprally.ui.fragment.around

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.store.ImageViewPagerAdapter
import com.sdin.tourstamprally.databinding.StoreDetailDialogBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.model.StoreSubimg
import com.sdin.tourstamprally.ui.dialog.BaseDialog

class StoreDetailDialog(context: Context, val storeModel: StoreModel) :
    BaseDialog(context, R.style.FullScreenDialogStyle) {

    private var binding: StoreDetailDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.store_detail_dialog,
            null,
            false
        )

        setContentView(binding?.root!!)
        binding?.dialog = this@StoreDetailDialog
        binding?.model = storeModel

        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.attributes.windowAnimations = R.style.AnimationPopupStyle

        initView()
    }

    private fun initView() {
        binding?.apply {
            imageViewPager.apply {
                adapter = ImageViewPagerAdapter().apply {
                    if (storeModel.storeSubDto?.storeSubimgList != null) {
                        submitList(storeModel.storeSubDto?.storeSubimgList)
                    } else {
                        submitList(
                            mutableListOf(
                                StoreSubimg(
                                    0,
                                    storeModel.store_idx,
                                    storeModel.store_curver_img
                                )
                            )
                        )
                    }

                }
            }

            TabLayoutMediator(tabLayout, imageViewPager)
            { tab, _ ->
                tab.view.isClickable = false
            }.attach()
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