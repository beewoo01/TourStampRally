package com.sdin.tourstamprally.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.model.TaggingnoCourseDTO

class LocationReAdapter(
    private val context: Context,
    private val list: List<TaggingnoCourseDTO>
    ) : RecyclerView.Adapter<LocationReAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationReAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_re_location, parent, false)
        Log.wtf("onCreateViewHolder","success")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationReAdapter.ViewHolder, position: Int) {
        holder.onBind(list[position])
        Log.wtf("onBindViewHolder","success")

    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onItemClick(v:View, data: TaggingnoCourseDTO, pos : Int, tag : String?)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val location : TextView = itemView.findViewById(R.id.location_txv)
        private val touristspot : TextView = itemView.findViewById(R.id.tourspot_name_txv)
        private val hashtag : TextView = itemView.findViewById(R.id.hashtag_txv)
        private val icon : ImageView = itemView.findViewById(R.id.tourimg_imv)

        fun onBind(item : TaggingnoCourseDTO){
            if(absoluteAdapterPosition % 2 == 0){
               location.setBackgroundResource(R.drawable.bg_rounded_33_yellow)
            }else{
                location.setBackgroundResource(R.drawable.bg_rounded_33_sky)
            }
            location.text = item.location_name
            touristspot.text = item.touristspot_name
            hashtag.text = item.touristspotpoint_tag



            var iconimg: Int? = when(item.touristspot_location_location_idx){
                1 -> R.drawable.ic_main_roadtour
                2 -> R.drawable.ic_main_hardtour
                3 -> R.drawable.ic_main_trakingtour
                4 -> R.drawable.ic_main_findtreasure
                5 -> R.drawable.ic_main_festivaltour
                6 -> R.drawable.ic_main_webtoontour
                7 -> R.drawable.ic_main_historytour
                else -> 0
            }
            iconimg.let {
                Glide.with(itemView).load(it).into(icon)
            }

            val pos = absoluteAdapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {

                    listener?.onItemClick(itemView,item,pos, item.touristspotpoint_tag)
                }
            }

        }
    }
}