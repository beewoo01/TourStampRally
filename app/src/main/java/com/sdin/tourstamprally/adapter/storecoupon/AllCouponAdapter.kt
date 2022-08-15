package com.sdin.tourstamprally.adapter.storecoupon

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.MycouponItemBinding
import com.sdin.tourstamprally.model.StoreAllCouponModel

class AllCouponAdapter(private val callback : (StoreAllCouponModel) -> Unit): ListAdapter<StoreAllCouponModel, AllCouponAdapter.ViewHolder>(differ) {
    inner class ViewHolder(private val binding: MycouponItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(model: StoreAllCouponModel) {
            with(binding) {
                if (model.store_logo_icon != null) {
                    Glide.with(logoImv.context)
                        .load("http://coratest.kr/imagefile/bsr/store_logo/" + model.store_logo_icon)
                        .placeholder(R.drawable.sample_profile_image)
                        .error(R.drawable.sample_profile_image)
                        .into(logoImv)
                } else {
                    Glide.with(logoImv.context)
                        .load(R.drawable.sample_profile_image)
                        .into(logoImv)
                }

                idxTxv.text = "${absoluteAdapterPosition + 1}"
                couponName.text = model.store_coupon_name
                startDataTxv.text = model.store_coupon_expiration_startDate
                endDataTxv.text = model.store_coupon_expiration_endDate
                itemContainer.setOnClickListener {
                    callback(model)
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MycouponItemBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<StoreAllCouponModel>() {
            override fun areItemsTheSame(
                oldItem: StoreAllCouponModel,
                newItem: StoreAllCouponModel
            ): Boolean {
                return oldItem.store_coupon_idx == newItem.store_coupon_idx
            }

            override fun areContentsTheSame(
                oldItem: StoreAllCouponModel,
                newItem: StoreAllCouponModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}