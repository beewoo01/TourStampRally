package com.sdin.tourstamprally.ui.fragment.store

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.store.ImageViewPagerAdapter
import com.sdin.tourstamprally.databinding.StoreDetailDialogBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.model.StoreSubimg
import com.sdin.tourstamprally.ui.dialog.BaseDialog

class StoreDetailDialog(context: Context, val model: StoreModel) :
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

        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.attributes.windowAnimations = R.style.AnimationPopupStyle

        initView()
    }

    private fun initView() {
        binding?.apply {
            storeNameTxv.text = model.store_name
            contentTxv.text = model.store_description

            imageViewPager.apply {
                adapter = ImageViewPagerAdapter().apply {
                    submitList(model.storeSubDto?.storeSubimgList)
                }
            }

            closeImb.setOnClickListener {
                dismiss()
            }

            TabLayoutMediator(tabLayout, imageViewPager)
            { tab, position ->
                imageViewPager.currentItem = tab.position
                tab.view.isClickable = false
                Log.wtf("hihi", "hihi$position")
            }.attach()
        }
    }


}