package com.sdin.tourstamprally.ui.fragment.store.coupon

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.FragmentFinishMycouponBinding
import com.sdin.tourstamprally.databinding.UsedCouponLayoutBinding
import com.sdin.tourstamprally.model.store_coupon.StoreMyCouponModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.listener.observe.Observer
import com.sdin.tourstamprally.utill.recyclerveiew.CustomItemDecoration

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
            usedCouponRe.apply {
                adapter = UnavailableCouponAdapter(0).apply {
                    submitList(it)
                }
                addItemDecoration(
                    CustomItemDecoration(
                        topPadding = 20, bottomPadding = 20, null, null
                    )
                )
            }
            usedCouponTxv.text = it.size.toString()

            couponListTitle.setOnClickListener {
                usedCouponRe.visibility =
                    if (usedCouponRe.visibility == View.GONE) {
                        Glide.with(overCouponArrowImb.context)
                            .load(R.drawable.ic_arrow_down)
                            .into(usedCouponArrowImb)

                        View.VISIBLE
                    } else {

                        Glide.with(overCouponArrowImb.context)
                            .load(R.drawable.ic_arrow_up)
                            .into(usedCouponArrowImb)

                        View.GONE
                    }
            }
        }

    }

    private fun initOverItem() = with(binding!!) {
        overList?.let {
            overCouponRe.apply {
                adapter = UnavailableCouponAdapter(1).apply {
                    submitList(it)
                }

                addItemDecoration(
                    CustomItemDecoration(
                        topPadding = 20, bottomPadding = 20, null, null
                    )
                )
            }

            overTimeTxv.text = it.size.toString()

            overListTitle.setOnClickListener {
                overCouponRe.visibility =
                    if (overCouponRe.visibility == View.GONE) {
                        Glide.with(overCouponArrowImb.context)
                            .load(R.drawable.ic_arrow_down)
                            .into(overCouponArrowImb)

                        View.VISIBLE
                    } else {

                        Glide.with(overCouponArrowImb.context)
                            .load(R.drawable.ic_arrow_up)
                            .into(overCouponArrowImb)


                        View.GONE
                    }
            }
        }
    }

    override fun update1(message: MutableList<StoreMyCouponModel>) = Unit

    override fun update2(
        message1: MutableList<StoreMyCouponModel>,
        message2: MutableList<StoreMyCouponModel>
    ) {
        usedList = message1
        overList = message2
        initView()
    }

    class UnavailableCouponAdapter(val state: Int) :
        ListAdapter<StoreMyCouponModel, UnavailableCouponAdapter.ViewHolder>(differ) {
        inner class ViewHolder(private val binding: UsedCouponLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

            @SuppressLint("SetTextI18n")
            fun onBind(model: StoreMyCouponModel) = with(binding) {
                idxTxv.text = (absoluteAdapterPosition + 1).toString()

                Glide.with(logoImv.context)
                    .load("http://coratest.kr/imagefile/bsr/store_logo/" + model.store_logo_icon)
                    .into(logoImv)

                couponName.text = model.store_coupon_name
                startDataTxv.text = model.store_coupon_expiration_startDate
                endDataTxv.text = model.store_coupon_expiration_endDate

                Glide.with(stateImv.context)
                    .load(
                        if (state == 0) {
                            R.drawable.used_store_coupon
                        } else {
                            R.drawable.overday_store_coupon
                        }

                    )
                    .into(stateImv)

            }

        }

        override fun getItemCount(): Int {
            return super.getItemCount()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                UsedCouponLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(currentList[position])
        }

        companion object {
            val differ = object : DiffUtil.ItemCallback<StoreMyCouponModel>() {
                override fun areItemsTheSame(
                    oldItem: StoreMyCouponModel,
                    newItem: StoreMyCouponModel
                ): Boolean {
                    return oldItem.store_mycoupon_idx == newItem.store_mycoupon_idx
                }

                override fun areContentsTheSame(
                    oldItem: StoreMyCouponModel,
                    newItem: StoreMyCouponModel
                ): Boolean {
                    return oldItem == newItem
                }

            }
        }


    }
}