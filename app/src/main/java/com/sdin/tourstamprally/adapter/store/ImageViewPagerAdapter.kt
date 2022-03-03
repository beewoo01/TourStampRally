package com.sdin.tourstamprally.adapter.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.model.StoreSubimg

class ImageViewPagerAdapter :
    ListAdapter<StoreSubimg, ImageViewPagerAdapter.ItemViewHolder>(differ) {
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(model: StoreSubimg) {
            val imageView = view.findViewById<ImageFilterView>(R.id.viewpager_imv)
            Glide.with(view.context)
                .load("http://coratest.kr/imagefile/bsr/" + model.store_subimg_url)
                .error(R.drawable.sample_bg)
                .into(imageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            inflater.inflate(
                R.layout.detail_dialog_viewpager_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<StoreSubimg>() {
            override fun areItemsTheSame(oldItem: StoreSubimg, newItem: StoreSubimg): Boolean {
                return oldItem.store_subimg_idx == oldItem.store_subimg_idx
            }

            override fun areContentsTheSame(
                oldItem: StoreSubimg, newItem: StoreSubimg
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}