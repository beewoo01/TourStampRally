package com.sdin.tourstamprally.adapter.tour.direction

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding
import com.sdin.tourstamprally.model.TopFourLocationModel
import com.sdin.tourstamprally.utill.GpsTracker
import com.sdin.tourstamprally.utill.LocationUtil

class DirectionGuidAdapter(context: Context, private val callback: (TopFourLocationModel) -> Unit) :
    ListAdapter<TopFourLocationModel, DirectionGuidAdapter.ViewHolder>(differ) {

    private val latitude: Double
    private val longitude: Double

    init {
        val gpsTracker = GpsTracker(context)
        latitude = gpsTracker.latitude
        longitude = gpsTracker.longitude
    }

    override fun getItemCount(): Int {
        return 8
    }

    inner class ViewHolder(private val binding: DirectionGuidLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun onBind(model: TopFourLocationModel) {
            binding.apply {
                locationSpotTxv.text = model.location_name

                val meter = LocationUtil.calcDistance(
                    latitude,
                    longitude,
                    model.touristspot_latitude,
                    model.touristspot_longitude
                )

                directionFromStempTxv.text = meter

                if (model.location_img.isNotEmpty()) {
                    Glide.with(locationBg.context)
                        .load("http://coratest.kr/imagefile/bsr/" + model.location_img)
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                locationBg.background = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) = Unit

                        })
                }

                if (model.location_idx > 0) {
                    val allCount = model.allPointCount
                    val clearCount = model.myHistoryCount
                    val allCounted: Int =
                        (((clearCount.toDouble() / allCount.toDouble()) * 100).toInt())

                    seekBarDirectionItem.max = allCount
                    seekBarDirectionItem.progress = clearCount
                    seekPercentTxv.text = "$allCounted%"
                }

                joinnerNumberTxv.text = model.popular.toString()
                locationBg.setOnClickListener {
                    callback(model)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DirectionGuidLocationItemBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<TopFourLocationModel>() {
            override fun areItemsTheSame(
                oldItem: TopFourLocationModel,
                newItem: TopFourLocationModel
            ): Boolean {
                return oldItem.location_idx == newItem.location_idx
            }

            override fun areContentsTheSame(
                oldItem: TopFourLocationModel,
                newItem: TopFourLocationModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}