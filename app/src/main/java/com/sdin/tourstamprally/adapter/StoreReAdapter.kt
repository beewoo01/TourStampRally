package com.sdin.tourstamprally.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.databinding.GuidStoreItemBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.v2.RecycleItemOnClick

class StoreReAdapter(
    private val list: MutableList<StoreModel>,
    private val itemOnClick: RecycleItemOnClick<StoreModel>
) :
    RecyclerView.Adapter<StoreReAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: GuidStoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: StoreModel) {
            binding.run {
                storeNameTxv.text = model.store_name
                storeLocationTxv.text = model.location_name

                Glide.with(storeImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + "1637572660349.jpg")
                    .into(storeImv)

                plusImb.setOnClickListener {
                    itemOnClick.onItemClickListener(model = model, position = absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GuidStoreItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}