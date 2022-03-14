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
import com.sdin.tourstamprally.databinding.DialogEventJoinSuccessBinding

class DialogEventJoinSuccess(context : Context, private val callBack : () -> Unit) :
    Dialog(context, R.style.FullScreenDialogStyle) {

    private var binding : DialogEventJoinSuccessBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_event_join_success,
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
            callBack()
            dismiss()
        }

    }

}