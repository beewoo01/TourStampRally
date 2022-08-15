package com.sdin.tourstamprally.adapter.report

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding
import com.sdin.tourstamprally.model.around.AllArroundEntity

class CategoryAdapter(private val callback: (AllArroundEntity) -> Unit) :
    ListAdapter<AllArroundEntity, CategoryAdapter.ViewHolder>(differ) {

    private var selectedItem = 0
    private var prevSelected = -1
    private var currentViewType = 0
    private var locationCount = 0
    private var storeCount = 0

    fun setViewType(viewType: Int) {
        currentViewType = viewType
    }

    fun setLocationCount(locationCount: Int) {
        this.locationCount = locationCount
    }

    fun setStoreCount(storeCount: Int) {
        this.storeCount = storeCount
    }


    override fun getItemCount(): Int {
        return if (currentViewType == 0) {
            locationCount
        } else {
            storeCount
        }

    }


    inner class ViewHolder(private val binding: DirectionGuidTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(tag: AllArroundEntity) {
            with(binding) {
                tagItemContainer.apply {
                    var icon: Int? = null
                    var storeicon : Int? = null

                    if (selectedItem == absoluteAdapterPosition) {
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.bg_rounded_category_selected
                        )

                        if (currentViewType == 0) {
                            //Location

                            icon = setIcon(true, absoluteAdapterPosition)
                        }else {
                            //Store
                            tagItemImg.isVisible = absoluteAdapterPosition == 0
                            storeicon = storeIcon(true, absoluteAdapterPosition)

                        }

                        tagItemTxv.setTextColor(ContextCompat.getColor(context, R.color.white))


                    } else {
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.bg_rounded_category
                        )
                        if(currentViewType == 0){
                            icon = setIcon(false, absoluteAdapterPosition)
                        }else{
                            tagItemImg.isVisible = absoluteAdapterPosition == 0
                            storeicon = storeIcon(false, absoluteAdapterPosition)
                        }

                        tagItemTxv.setTextColor(ContextCompat.getColor(context, R.color.mainColor))

                    }

                    if(currentViewType == 0){
                        icon.let {
                            Glide.with(tagItemImg.context)
                                .load(it)
                                //.error(R.drawable.button_selector_drawable)
                                .into(tagItemImg)
                        }
                    }else{
                        storeicon.let {
                            Glide.with(tagItemImg.context)
                                .load(it)
                                //.error(R.drawable.button_selector_drawable)
                                .into(tagItemImg)
                        }
                    }




                    setOnClickListener {
                        prevSelected = selectedItem
                        selectedItem = absoluteAdapterPosition
                        Log.wtf("setOnClickListener", selectedItem.toString())
                        callback(tag)
                        notifyItemChanged(prevSelected)
                        notifyItemChanged(selectedItem)
                    }

                    tagItemTxv.text = if (currentViewType == 0) {
                        //Location
                        tag.location_name
                    } else {
                        tag.storeName
                    }

                }




            }
        }
        private fun storeIcon(isSelected: Boolean, position: Int):Int{
            return when (position){
                0->{
                    if (isSelected) {
                        0
                    } else {
                        0
                    }
                }
                1->{
                    if (isSelected) {
                        R.drawable.ic_menu_restaurant_on
                    } else {
                        R.drawable.ic_menu_restaurant_off
                    }
                }
                2->{
                    if (isSelected) {
                        R.drawable.ic_menu_cafe_on
                    } else {
                        R.drawable.ic_menu_cafe_off
                    }
                }
                3->{
                    if (isSelected) {
                        R.drawable.ic_menu_bed_on
                    } else {
                        R.drawable.ic_menu_bed_off
                    }
                }
                else -> R.drawable.ic_menu_bed_off
            }
        }

        private fun setIcon(isSelected: Boolean, position: Int): Int {
            return when (position) {
                0 -> {
                    if (isSelected) {
                        R.drawable.ic_roadtour_on
                    } else {
                        R.drawable.ic_roadtour_off
                    }
                }
                1 -> {
                    if (isSelected) {
                        R.drawable.ic_hardtour_on
                    } else {
                        R.drawable.ic_hardtour_off
                    }
                }
                2 -> {
                    if (isSelected) {
                        R.drawable.ic_trackingtour_on
                    } else {
                        R.drawable.ic_trackingtour_off
                    }
                }
                3->{ //보물찾기
                    if (isSelected) {
                        R.drawable.treasure_on
                    } else {
                        R.drawable.treasure_off
                    }
                }
                4 -> {
                    if (isSelected) {
                        R.drawable.ic_festivaltour_on
                    } else {
                        R.drawable.ic_festivaltour_off
                    }
                }
                5 -> {
                    if (isSelected) {
                        R.drawable.ic_webtoontour_on
                    } else {
                        R.drawable.ic_webtoontour_off
                    }
                }
                6 -> {
                    if (isSelected) {
                        R.drawable.ic_historytour_on
                    } else {
                        R.drawable.ic_historytour_off
                    }
                }
                else -> {
                    R.drawable.ic_hardtour_off
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DirectionGuidTagItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<AllArroundEntity>() {
            override fun areItemsTheSame(
                oldItem: AllArroundEntity,
                newItem: AllArroundEntity
            ): Boolean {
                if (oldItem.viewType == 0) {
                    return oldItem.location_idx == newItem.location_idx
                } else {
                    return oldItem.storeType == newItem.storeType
                }


            }

            override fun areContentsTheSame(
                oldItem: AllArroundEntity,
                newItem: AllArroundEntity
            ): Boolean {
                return oldItem.toString() == newItem.toString()
            }

        }
    }


}