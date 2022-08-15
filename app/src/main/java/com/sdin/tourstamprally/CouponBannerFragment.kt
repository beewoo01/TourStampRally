package com.sdin.tourstamprally

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.databinding.FragmentCouponBannerBinding
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class CouponBannerFragment : BaseFragment() {

    private var binding: FragmentCouponBannerBinding? = null
    private var allcoupon = AllCouponFragment()
    private val args: CouponBannerFragmentArgs  by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coupon_banner, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        initView()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initView() = with(binding!!) {

        args.bannerModel.run {
            Glide.with(logoImv.context)
                .load("http://coratest.kr/imagefile/bsr/store_logo/$store_logo_icon")
                .into(logoImv)

            storeNameTxv.text = store_name
            couponNameTxv.text = store_coupon_name


            couponNumberTxv.text = store_coupon_number
            validityDateTxv.text = store_coupon_expiration_endDate


    //            Glide.with(barCordImv.context).load(createBarCode(store_coupon_number)).into(barCordImv)
            exchangeLocateTxv.text = store_name

            copyCouponNumTxv.setOnClickListener {
                val clipBoard =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", store_coupon_number)
                clipBoard.setPrimaryClip(clip)
            }

            btnImgSave.setOnClickListener {
                DefaultBSRDialog(
                    requireContext(),
                    title = "\n 해당 쿠폰을\n발급 받으시겠습니까?",
                    content = "\n\n",
                    isSpecial = true,
                    isSwitchBtn = false,
                    leftBtnStr = "취소",
                    rightBtnStr = "발급"
                ) {isCancel: Boolean ->
                    if(!isCancel){
                        //쿠폰 저장하는거 넣으면 될듯?
                        apiService.getCouponFromFree(
                                store_coupon_idx, //스토어 쿠폰 IDX
                                Utils.User_Idx
                            )
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object : DisposableSingleObserver<Int>() {
                                    override fun onSuccess(result: Int) {
                                        Toast.makeText(requireContext(), "쿠폰이 저장 되었습니다.", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onError(e: Throwable) {
                                        Toast.makeText(requireContext(), "쿠폰 저장에 실패했습니다..", Toast.LENGTH_SHORT).show()
                                        e.printStackTrace()
                                    }

                                })
                    }

                }.show()
            }

        }



    }


}