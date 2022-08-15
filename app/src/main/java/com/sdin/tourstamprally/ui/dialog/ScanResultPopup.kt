package com.sdin.tourstamprally.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.DialogQrScansuccessBinding
import com.sdin.tourstamprally.utill.listener.DialogListener

class ScanResultPopup(
    context: Context,
    private val scanResult: Int,
    private val parentFragment: String,
    private val dismissListener : DialogListener?,
    val callBack : () -> Unit
) :
    Dialog(context, R.style.FullScreenDialogStyle) {
    /** scanResult
     *  0 -> Success
     *  1 -> Wrong
     *  2 -> Already
     * */
    private var binding: DialogQrScansuccessBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_qr_scansuccess,
            null,
            false
        )

        setContentView(binding?.root!!)

        initView()

    }


    private fun initView() {
        window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.AnimationPopupStyle
        }

        when (scanResult) {
            0 -> initSuccess()

            1 -> initFail()

            2 -> initAlready()
        }

        binding?.closeImb?.setOnClickListener {
            Log.wtf("ScanResultPopup", "closeImb click")
            dismissListener?.onDisMiss()
            dismiss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.wtf("onBackPressed", "ScanResultDialog")
        dismissListener?.onDisMiss()
    }

    @SuppressLint("SetTextI18n")
    private fun initSuccess() = with(binding!!) {
        mainContainer.background = ContextCompat.getDrawable(
            mainContainer.context,
            R.drawable.bg_scan_success
        )

        titleTxv.apply {
            text = "$parentFragment 스캔 성공"
            setTextColor(ContextCompat.getColor(this.context, R.color.scansuccess))
        }

        /*stempImv.apply {
            Glide.with(this.context).load(R.drawable.ic_scan_result_di_success_stemp).into(this)
            visibility = View.VISIBLE
        }*/

        contentTxv.apply {
            text = "스탬프 랠리 획득"
            background = ContextCompat.getDrawable(this.context, R.drawable.bg_rounded_28_purple)
            setTextColor(ContextCompat.getColor(this.context, R.color.white))
        }

        Glide.with(alertImv.context).load(R.drawable.ic_scan_success).into(alertImv)

        checkTxv.apply {
            text = "확인하러가기 >"
            setTextColor(ContextCompat.getColor(this.context, R.color.scansuccess))
            background = ContextCompat.getDrawable(this.context, R.drawable.bg_rounded_02_green)
            setOnClickListener {
                callBack()
                dismiss()
            }

        }


    }

    @SuppressLint("SetTextI18n")
    private fun initFail() = with(binding!!) {
        mainContainer.background = ContextCompat.getDrawable(
            mainContainer.context,
            R.drawable.bg_qr_scanfail
        )

        titleTxv.apply {
            text = "$parentFragment 스캔 실패"
            setTextColor(ContextCompat.getColor(this.context, R.color.mainColor))
        }

        /* stempImv.apply {
             Glide.with(this.context).load(R.drawable.thumb_down).into(this)
             visibility = View.VISIBLE
         }*/

        contentTxv.apply {
            text = "재시도 해주세요."
            setTextColor(ContextCompat.getColor(this.context, R.color.black))
            background = ContextCompat.getDrawable(this.context, R.drawable.bg_rounded_28_white)
        }

        Glide.with(alertImv.context).load(R.drawable.ic_scan_fail).into(alertImv)

        checkTxv.apply {
            text = "재시도하기 >"
            setTextColor(ContextCompat.getColor(this.context, R.color.white))
            background = ContextCompat.getDrawable(this.context, R.drawable.bg_rounded_03_purple)

            setOnClickListener {
                dismissListener?.onDisMiss()
                dismiss()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initAlready() = with(binding!!) {
        mainContainer.background = ContextCompat.getDrawable(
            mainContainer.context,
            R.drawable.bg_rounded_28_blue
        )

        titleTxv.apply {
            text = "스탬프 확인"
            setTextColor(ContextCompat.getColor(this.context, R.color.white))

        }

        /*stempImv.apply {
            visibility = View.INVISIBLE
        }*/

        contentTxv.apply {
            text = "이미획득 완료하셨습니다."
            background = ContextCompat.getDrawable(this.context, R.drawable.bg_rounded_28_deepblue)
            setTextColor(ContextCompat.getColor(this.context, R.color.white))
        }

        Glide.with(alertImv.context).load(R.drawable.ic_scan_overlapping).into(alertImv)

        checkTxv.apply {
            text = "확인하러가기 >"
            setTextColor(ContextCompat.getColor(this.context, R.color.mainColor))
            background = ContextCompat.getDrawable(this.context, R.drawable.bg_rounded_02_purple)

            setOnClickListener {
                // TODO: MoveFragment
                callBack()
                dismiss()
            }
        }

    }
}