package com.sdin.tourstamprally.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.GetCouponAlertBinding

class GetCouponAlertDialog(context: Context, private val getCouponCallback : (Boolean) -> Unit) :
    Dialog(context, R.style.FullScreenDialogStyle) {

    private var binding: GetCouponAlertBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.get_coupon_alert,
            null,
            false
        )

        setContentView(binding?.root!!)

        window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.AnimationPopupStyle
        }

        binding?.cancelTxv?.setOnClickListener {
            dismiss()
        }

        binding?.okTxv?.setOnClickListener {
            getCouponCallback(true)
            dismiss()
        }

    }
}