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
import com.sdin.tourstamprally.databinding.DialogFailTimeoverBinding

class DialogFailTimeOver(context: Context) :
    Dialog(context, R.style.FullScreenDialogStyle) {

    private var binding : DialogFailTimeoverBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_fail_timeover,
            null,
            false
        )

        setContentView(binding?.root!!)

        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.attributes.windowAnimations = R.style.AnimationPopupStyle

        binding?.checkTxv?.setOnClickListener {
            dismiss()
        }

        binding?.closeImb?.setOnClickListener {
            dismiss()
        }

    }


}