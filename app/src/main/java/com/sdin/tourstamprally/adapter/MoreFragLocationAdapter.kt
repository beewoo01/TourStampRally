package com.sdin.tourstamprally.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.ReItemReviewLocationBinding

class MoreFragLocationAdapter(val list: MutableList<Pair<Int, String>>, val context: Context) :
    RecyclerView.Adapter<MoreFragLocationAdapter.ViewHolder>() {

    private var litener: MoreLocationItemListener? = null
    private var selectedItem = -1
    private var prevSelected = -1
    private var locationIdx = 0


    interface MoreLocationItemListener {
        fun onItemClick(item: Int)
    }

    fun setMoreLocationListener(listener: MoreLocationItemListener) {
        this.litener = listener
    }



    inner class ViewHolder(val binding: ReItemReviewLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Pair<Int, String>) {


            locationIdx = data.first

            binding.tagItemTxv.text = data.second
            binding.tagItemTxv.setOnClickListener {

                prevSelected = selectedItem
                selectedItem = absoluteAdapterPosition

                litener?.onItemClick(data.first)

                if (prevSelected == absoluteAdapterPosition) {

                    selectedItem = -1
                    prevSelected = -1
                    notifyItemChanged(absoluteAdapterPosition)

                } else {

                    notifyItemChanged(selectedItem)
                    notifyItemChanged(prevSelected)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReItemReviewLocationBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        var icon: Int? = null
        if (selectedItem == position) {
            holder.binding.tagItemCon.background = ContextCompat.getDrawable(
                holder.binding.tagItemCon.context,
                R.drawable.bg_rounded_category_selected
            )

            holder.binding.tagItemTxv.setTextColor(
                ContextCompat.getColor(
                    holder.binding.tagItemTxv.context,
                    R.color.white
                )
            )

            icon = setIcon(true, locationIdx)
        } else {
            holder.binding.tagItemCon.background = ContextCompat.getDrawable(
                holder.binding.tagItemCon.context,
                R.drawable.bg_rounded_category
            )

            icon = setIcon(false, locationIdx)
            holder.binding.tagItemTxv.setTextColor(
                ContextCompat.getColor(
                    holder.binding.tagItemTxv.context,
                    R.color.mainColor
                )
            )
        }
        icon.let {
            Glide.with(holder.binding.tagItemImg.context)
                .load(it)
                //.error(R.drawable.button_selector_drawable)
                .into(holder.binding.tagItemImg)
        }

    }
    fun setIcon(isSelected: Boolean, position: Int): Int {
        return when (position) {
            1 -> {
                if (isSelected) {
                    R.drawable.ic_roadtour_on
                } else {
                    R.drawable.ic_roadtour_off
                }
            }
            2 -> {
                if (isSelected) {
                    R.drawable.ic_hardtour_on
                } else {
                    R.drawable.ic_hardtour_off
                }
            }
            3 -> {
                if (isSelected) {
                    R.drawable.ic_trackingtour_on
                } else {
                    R.drawable.ic_trackingtour_off
                }
            }
            5 -> {
                if (isSelected) {
                    R.drawable.ic_festivaltour_on
                } else {
                    R.drawable.ic_festivaltour_off
                }
            }
            6 -> {
                if (isSelected) {
                    R.drawable.ic_webtoontour_on
                } else {
                    R.drawable.ic_webtoontour_off
                }
            }
            7 -> {
                if (isSelected) {
                    R.drawable.ic_historytour_on
                } else {
                    R.drawable.ic_historytour_off
                }
            }
            else -> {
                R.drawable.ic_menu_restaurant_on
            }
        }
    }



    override fun getItemCount(): Int = list.size

}