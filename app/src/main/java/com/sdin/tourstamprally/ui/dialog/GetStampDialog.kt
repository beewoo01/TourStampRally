package com.sdin.tourstamprally.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sdin.tourstamprally.*
import com.sdin.tourstamprally.databinding.DialogGetstampBinding

class GetStampDialog(context: Context, private val fa: FragmentActivity) :
    Dialog(context, R.style.FullScreenDialogStyle) {
    private var binding : DialogGetstampBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_getstamp,
            null,
            false
        )

        setContentView(binding?.root!!)

        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.attributes.windowAnimations = R.style.AnimationPopupStyle
        binding?.viewpagerStamp?.adapter = ScreenSlidePagerAdapter(fa) //Dialog ViewPager
        binding?.dotsIndicator?.setViewPager2(binding?.viewpagerStamp!!)
        binding?.closeImb?.setOnClickListener {
            dismiss()
        }
    }

    private inner class ScreenSlidePagerAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int {
            return 5;
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> {
                    GetStamp1Fragment.newInstance(0)
                }
                1 -> {
                    GetStamp2Fragment.newInstance(1)
                }
                2 -> {
                    GetStamp3Fragment.newInstance(2)
                }
                3->{
                    GetStamp4Fragment.newInstance(3)
                }
                4->{
                    GetStamp5Fragment.newInstance(4)
                }
                else -> {
                    GetStamp1Fragment.newInstance(0)
                }

            }
        }
    }
}