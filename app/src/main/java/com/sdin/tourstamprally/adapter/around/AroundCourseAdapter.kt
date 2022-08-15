package com.sdin.tourstamprally.adapter.around

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.databinding.ViewholderCourseIncludeBinding
import com.sdin.tourstamprally.model.AroundTouristSpot

class AroundCourseAdapter(
    private val spotClickListener: (model : AroundTouristSpot) -> Unit,
    private val detailViewClickListener: (model : AroundTouristSpot) -> Unit,
    private val currentLatitude: Double,
    private val currentLongitude: Double

) : ListAdapter<AroundTouristSpot, AroundCourseAdapter.ViewHolder>(diff) {


    inner class ViewHolder(private val binding: ViewholderCourseIncludeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(model: AroundTouristSpot) = with(binding) {
            spotNameTxv.text = model.touristspot_name
            spotLocationTxv.text = model.location_name

            distanceTxv.text = distanceTo(model.touristspot_latitude.toDouble(), model.touristspot_longitude.toDouble())

            spotDistanceTxv.text = "스탬프존 거리 ${
                distanceTo(model.touristspot_latitude.toDouble(), model.touristspot_longitude.toDouble())
            }"

            Glide.with(storeImv.context)
                .load("http://coratest.kr/imagefile/bsr/" + model.touristspot_img)
                .into(storeImv)

            binding.root.setOnClickListener {
                spotClickListener(model)
            }

            binding.plusImb.setOnClickListener {
                detailViewClickListener(model)
            }
        }

    }

    private fun distanceTo(latitude: Double, longitude: Double): String {
        val locationA = Location("current Location")
        val locationB = Location("store Location")

        locationA.latitude = currentLatitude
        locationA.longitude = currentLongitude

        locationB.latitude = latitude
        locationB.longitude = longitude

        val distance: Float = locationA.distanceTo(locationB)
        if(distance.toInt()>1000){

            val kmDistance  = distance.toDouble()/1000
            return String.format("%.2f", kmDistance)+"km"
        }else{
            Log.wtf("distance", distance.toInt().toString())
            return distance.toInt().toString()+"m"
        }
//        return distance.toInt().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewholderCourseIncludeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<AroundTouristSpot>() {
            override fun areItemsTheSame(
                oldItem: AroundTouristSpot,
                newItem: AroundTouristSpot
            ): Boolean {
                return oldItem.touristspot_idx == newItem.touristspot_idx
            }

            override fun areContentsTheSame(
                oldItem: AroundTouristSpot,
                newItem: AroundTouristSpot
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}