package com.sdin.tourstamprally.adapter

import android.location.Location
import android.util.Log
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
    private val callback: (StoreModel) -> Unit,
    private val interCallback: (StoreModel) -> Unit,
    private val locationCallback: (StoreModel) -> Unit,
    private val currentLatitude: Double,
    private val currentLongitude: Double
//    private var mItemClickListener: AdapterView.OnItemClickListener? = null

) : ListAdapter<StoreModel, StoreReAdapter.ViewHolder>(diff) {

    inner class ViewHolder(val binding: GuidStoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(storeModel: StoreModel) {
            binding.run {
                model = storeModel

                itemView.setOnClickListener {
                    locationCallback(storeModel)
                }

                distanceTxv.text = distanceTo(
                    latitude = storeModel.store_latitude.toDouble(),
                    longitude = storeModel.store_longitude.toDouble()
                )

                Glide.with(storeImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + storeModel.store_curver_img)
                    .placeholder(R.drawable.sample_profile_image)
                    .error(R.drawable.sample_profile_image)
                    .into(storeImv)


                plusImb.setOnClickListener {
                    callback(storeModel)
                }

                /*val heartImg =
                    if (storeModel.user_store_interest_idx > 0) {
                        R.drawable.full_heart_resize

                    } else {
                        R.drawable.heart_resize
                    }*/

                /*Glide.with(likeImb.context).load(heartImg).into(likeImb)

                likeImb.setOnClickListener {
                    val heartChangeImg =
                        if (storeModel.user_store_interest_idx > 0) {
                            R.drawable.heart_resize
                        } else {
                            R.drawable.full_heart_resize
                        }

                    interCallback(storeModel)

                    Glide.with(likeImb.context).load(heartChangeImg).into(likeImb)
                }*/
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

    fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }
    /*fun setOnItemClickListener(listener : AdapterView.OnItemClickListener){
        mItemClickListener = listener
    }*/

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