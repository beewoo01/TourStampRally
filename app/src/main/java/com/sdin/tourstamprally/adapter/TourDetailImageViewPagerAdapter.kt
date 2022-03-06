package com.sdin.tourstamprally.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.model.TouristSpotPoint
import com.sdin.tourstamprally.model.TouristSpotPointImg

class TourDetailImageViewPagerAdapter :
    ListAdapter<TouristSpotPointImg, TourDetailImageViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(model: TouristSpotPointImg) {
            val imageView = view.findViewById<ImageFilterView>(R.id.viewpager_imv)

            Glide.with(view.context)
                .load("http://coratest.kr/imagefile/bsr/" + model.touristspotpoint_img_url)
                .placeholder(R.drawable.sample_bg)
                .error(R.drawable.sample_bg)
                .into(imageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            inflater.inflate(
                R.layout.detail_dialog_viewpager_item, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<TouristSpotPointImg>() {
            override fun areItemsTheSame(
                oldItem: TouristSpotPointImg,
                newItem: TouristSpotPointImg
            ): Boolean {
                return oldItem.touristspotpoint_img_idx == newItem.touristspotpoint_img_idx
            }

            override fun areContentsTheSame(
                oldItem: TouristSpotPointImg,
                newItem: TouristSpotPointImg
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}