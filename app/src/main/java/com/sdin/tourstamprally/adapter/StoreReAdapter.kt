package com.sdin.tourstamprally.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.GuidStoreItemBinding
import com.sdin.tourstamprally.model.StoreModel

class StoreReAdapter(
    /*private val list: MutableList<StoreModel>,*/
    private val callback : (StoreModel) -> Unit
) :
    ListAdapter<StoreModel, StoreReAdapter.ViewHolder>(diff) {

    inner class ViewHolder(val binding: GuidStoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(storeModel: StoreModel) {
            binding.run {
                model = storeModel

                Glide.with(storeImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + storeModel.store_curver_img)
                    .placeholder(R.drawable.sample_profile_image)
                    .error(R.drawable.sample_profile_image)
                    .into(storeImv)

                plusImb.setOnClickListener {
                    callback(storeModel)
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
        holder.onBind(currentList[position])
    }

    fun removeItem(position :Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<StoreModel>() {
            override fun areItemsTheSame(oldItem: StoreModel, newItem: StoreModel): Boolean {
                return oldItem.store_idx == newItem.store_idx
            }

            override fun areContentsTheSame(oldItem: StoreModel, newItem: StoreModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    //override fun getItemCount(): Int = list.size
}