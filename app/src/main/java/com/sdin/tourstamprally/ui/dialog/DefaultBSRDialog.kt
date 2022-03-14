package com.sdin.tourstamprally.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.kakao.sdk.template.model.Content
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.DialogStempResetBinding
import kotlin.math.log

class DefaultBSRDialog(
    context: Context,
    val title: String,
    val content: String,
    val isSpecial: Boolean,
    val leftBtnStr: String,
    val rightBtnStr: String,
    val callback : (Boolean) -> Unit
) :
    Dialog(context, R.style.FullScreenDialogStyle) {

    private var binding: DialogStempResetBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_stemp_reset,
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

    private fun initView() = with(binding!!) {
        if (isSpecial) {
            Glide.with(logoImv.context).load(R.drawable.ic_popup_content_logo).into(logoImv)
            contentLayout.background =
                ContextCompat.getDrawable(contentLayout.context, R.drawable.bg_rounded_28)
        }

        titleTxv.text = title
        contentTxv.text = content
        okTxv.text = leftBtnStr
        cancelTxv.text = rightBtnStr
        contentLayout.background =
            ContextCompat.getDrawable(contentLayout.context, R.drawable.bg_rounded_22)

        okTxv.setOnClickListener {
            callback(true)
            dismiss()
        }

        cancelTxv.setOnClickListener {
            callback(false)
            dismiss()
        }

    }
}