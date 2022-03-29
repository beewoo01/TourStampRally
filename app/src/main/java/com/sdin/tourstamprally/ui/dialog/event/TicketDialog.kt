package com.sdin.tourstamprally.ui.dialog.event

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.ImgPopupBinding
import com.sdin.tourstamprally.model.CouponModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TicketDialog(
    context: Context,
    private val couponModel: CouponModel,
    private val couponCount : Int,
    private val callBack: (Pair<Int, CouponModel?>) -> Unit
) :
    Dialog(context, R.style.FullScreenDialogStyle) {

    private var binding: ImgPopupBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.img_popup,
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

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initView() = with(binding!!) {
        couponTxv.text = "쿠폰번호 : ${couponModel.coupon_number}"
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sdf2 = SimpleDateFormat("yyyy.MM.dd")
        againChallengeTxv.setOnClickListener {
            //다시 도전
            callBack(Pair(0, null))
            dismiss()
        }

        checkImb.setOnClickListener {
            dismiss()
        }


        congratsTxv.text = "축하합니다!\n스탬프를 ${couponCount}번째 모두 획득하셨습니다."
        couponModel.coupon_touristspot_idx


        applyEventTxv.setOnClickListener {
            //이벤트 응모하기
            callBack(Pair(1, couponModel))
            dismiss()
        }

        val calender = Calendar.getInstance()

        try {
            val to = sdf.parse(couponModel.coupon_createtime)
            calender.time = to
            val oldDate: String = sdf2.format(to)
            calender.add(Calendar.DATE, 30)
            val nMonth: Int = calender.get(Calendar.MONTH) + 1
            val nDate: Int = calender.get(Calendar.DATE)
            //String endDate = month + "." + date;
            val endDate = "$nMonth.$nDate"
            expirationPeriodTxv.text = "사용기간 : $oldDate~$endDate"
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

}