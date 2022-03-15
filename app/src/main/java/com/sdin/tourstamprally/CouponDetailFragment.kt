package com.sdin.tourstamprally

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.sdin.tourstamprally.databinding.FragmentCouponDetailBinding
import com.sdin.tourstamprally.model.store_coupon.StoreMyCouponModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*


class CouponDetailFragment : BaseFragment() {

    private var binding: FragmentCouponDetailBinding? = null
    private var model: StoreMyCouponModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val paramData = arguments?.getParcelable<StoreMyCouponModel>("model")
        paramData?.let {
            model = it
        }
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coupon_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        initView()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initView() = with(binding!!) {
        model?.run {
            Glide.with(logoImv.context)
                .load("http://coratest.kr/imagefile/bsr/store_logo/$store_logo_icon")
                .into(logoImv)

            storeNameTxv.text = store_name
            couponNameTxv.text = store_coupon_name

            // TODO: ImageView Init BarCode

            couponNumberTxv.text = store_coupon_number
            validityDateTxv.text = store_coupon_expiration_endDate
            couponStateTxv.text =
                when (store_mycoupon_state) {
                    0 -> {
                        "사용안함"
                    }
                    1 -> {
                        "사용완료"
                    }
                    else -> {
                        "기간지남"
                    }
                }

            Glide.with(barCordImv.context).load(createBarCode(store_coupon_number)).into(barCordImv)
            exchangeLocateTxv.text = store_name

            copyCouponNumTxv.setOnClickListener {
                val clipBoard =
                    requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", store_coupon_number)
                clipBoard.setPrimaryClip(clip)
            }

            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date: Date? = sdf.parse(store_mycoupon_createtime)
                val calendar = Calendar.getInstance()
                date?.let {
                    calendar.time = it
                    issueDateTxv.text =
                        "${calendar.get(Calendar.YEAR)}년 " +
                                "${calendar.get(Calendar.MONTH) + 1}월 " +
                                "${calendar.get(Calendar.DAY_OF_MONTH)}일"
                }


            } catch (e: DateTimeParseException) {
                e.printStackTrace()
            }


        }


    }

    private fun createBarCode(code: String): Bitmap {
        val widthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 390f,
            resources.displayMetrics
        )
        val heightPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 111f,
            resources.displayMetrics
        )
        val format: BarcodeFormat = BarcodeFormat.CODE_128
        val matrix: BitMatrix =
            MultiFormatWriter().encode(code, format, widthPx.toInt(), heightPx.toInt())
        val bitmap = createBitmap(matrix)
        return bitmap
    }

    private fun createBitmap(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (matrix.get(x, y)) BLACK else WHITE)
            }
        }
        return bitmap
    }


}