package com.sdin.tourstamprally.ui.fragment.store.coupon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.FragmentFinishMycouponBinding
import com.sdin.tourstamprally.model.store_coupon.StoreMyCouponModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.listener.observe.Observer

class MyFinishCouponFragment : BaseFragment(), Observer<MutableList<StoreMyCouponModel>> {

    private var usedList: MutableList<StoreMyCouponModel>? = null
    private var overList: MutableList<StoreMyCouponModel>? = null
    private var binding: FragmentFinishMycouponBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_finish_mycoupon, container, false)
        return binding?.root
    }


    private fun initView() {

        initUsedItem()
        initOverItem()

    }

    private fun initUsedItem() = with(binding!!) {
        usedList?.let {
            if (it.size > 0) {
                /*firstUsedCoupon.root.visibility = View.VISIBLE
                with(firstUsedCoupon) {
                    Glide.with(logoImv.context)
                        .load("" + it[0].store_logo_icon)
                        .into(logoImv)

                    couponName.text = it[0].store_coupon_name
                    startDataTxv.text = it[0].store_coupon_expiration_startDate
                    endDataTxv.text = it[0].store_coupon_expiration_endDate

                    Glide.with(stateImv.context)
                        .load(R.drawable.used_store_coupon)
                        .into(stateImv)

                }*/
            }

            if (it.size > 1) {
                /*secondUsedCoupon.root.visibility = View.VISIBLE
                with(secondUsedCoupon) {

                    Glide.with(logoImv.context)
                        .load("" + it[1].store_logo_icon)
                        .into(logoImv)

                    couponName.text = it[1].store_coupon_name
                    startDataTxv.text = it[1].store_coupon_expiration_startDate
                    endDataTxv.text = it[1].store_coupon_expiration_endDate

                    Glide.with(stateImv.context)
                        .load(R.drawable.used_store_coupon)
                        .into(stateImv)
                }*/

            }
        }
    }

    private fun initOverItem() = with(binding!!) {
        overList?.let {
            if (it.size > 0) {
                /*firstOverCoupon.root.visibility = View.VISIBLE
                with(firstOverCoupon) {
                    Glide.with(logoImv.context)
                        .load("" + it[0].store_logo_icon)
                        .into(logoImv)

                    couponName.text = it[0].store_coupon_name
                    startDataTxv.text = it[0].store_coupon_expiration_startDate
                    endDataTxv.text = it[0].store_coupon_expiration_endDate

                    Glide.with(stateImv.context)
                        .load(R.drawable.overday_store_coupon)
                        .into(stateImv)
                }*/
            }

            if (it.size > 1) {
                /*secondUsedCoupon.root.visibility = View.VISIBLE
                with(secondUsedCoupon) {
                    Glide.with(logoImv.context)
                        .load("" + it[1].store_logo_icon)
                        .into(logoImv)

                    couponName.text = it[1].store_coupon_name
                    startDataTxv.text = it[1].store_coupon_expiration_startDate
                    endDataTxv.text = it[1].store_coupon_expiration_endDate

                    Glide.with(stateImv.context)
                        .load(R.drawable.overday_store_coupon)
                        .into(stateImv)
                }*/

            }
        }
    }

    override fun update1(message: MutableList<StoreMyCouponModel>) {

    }

    override fun update2(
        message1: MutableList<StoreMyCouponModel>,
        message2: MutableList<StoreMyCouponModel>
    ) {
        usedList = message1
        overList = message2
        initView()
        Log.wtf("MyFinishCouponFragment", "MyFinishCouponFragment")
    }
}