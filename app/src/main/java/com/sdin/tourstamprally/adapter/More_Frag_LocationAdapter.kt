package com.sdin.tourstamprally.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.ReItemReviewLocationBinding

class More_Frag_LocationAdapter(val list: MutableList<Pair<Int, String>>, val context: Context) : RecyclerView.Adapter<More_Frag_LocationAdapter.ViewHolder>() {

    private var litener : MoreLocationItemListener? = null
    private var selectedItem = -1
    private var prevSelected = -1

    interface MoreLocationItemListener{
        fun onItemClick(item: Int)
    }

    fun setMoreLocationListener(litener: MoreLocationItemListener) {
        this.litener = litener
    }

    inner class ViewHolder(val binding: ReItemReviewLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Pair<Int, String>) {
            binding.tagItemTxv.text = data.second
            binding.tagItemTxv.setOnClickListener {

                prevSelected = selectedItem
                selectedItem = absoluteAdapterPosition

                litener?.onItemClick(data.first)

                if (prevSelected == absoluteAdapterPosition){

                    selectedItem = -1
                    prevSelected = -1
                    notifyItemChanged(absoluteAdapterPosition)

                }else{

                    notifyItemChanged(selectedItem)
                    notifyItemChanged(prevSelected)
                }


            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ReItemReviewLocationBinding.inflate(
                        LayoutInflater.from(context), parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

        if (selectedItem == position) {
            holder.binding.tagItemTxv.background = ContextCompat.getDrawable(holder.binding.tagItemTxv.context, R.drawable.bg_rounded_category_selected)
            holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.context, R.color.white))
        } else {
            holder.binding.tagItemTxv.background = ContextCompat.getDrawable(holder.binding.tagItemTxv.context, R.drawable.bg_rounded_category)
            holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.context, R.color.mainColor))
        }

    }

    override fun getItemCount(): Int = list.size

}