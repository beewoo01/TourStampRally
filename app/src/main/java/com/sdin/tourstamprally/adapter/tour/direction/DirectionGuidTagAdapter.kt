package com.sdin.tourstamprally.adapter.tour.direction

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding
import com.sdin.tourstamprally.model.TourTagModelDC

class DirectionGuidTagAdapter(private val callback : (TourTagModelDC) -> Unit) :
    ListAdapter<TourTagModelDC, DirectionGuidTagAdapter.ViewHolder>(differ) {

    private var selectedItem: Int = -1
    private var prevSelected: Int = -1


    inner class ViewHolder(private val binding: DirectionGuidTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(model : TourTagModelDC) {
            with(binding.tagItemTxv) {

                text = "#${model.hashTag}"

                when (selectedItem) {
                    absoluteAdapterPosition -> {
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.bg_rounded_category_selected
                        )

                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )

                    }

                    else -> {
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.bg_rounded_category
                        )

                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.mainColor
                            )
                        )
                    }
                }

                setOnClickListener {
                    selectedItem = absoluteAdapterPosition
                    if (selectedItem == prevSelected) {
                        notifyItemChanged(prevSelected)
                        selectedItem = -1
                        prevSelected = -1
                        callback(TourTagModelDC("",model.location_idx))
                    } else {
                        prevSelected = selectedItem
                        if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                            notifyItemChanged(selectedItem)
                            notifyItemChanged(prevSelected)
                            callback(model)
                        }
                    }

                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DirectionGuidTagItemBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<TourTagModelDC>() {
            override fun areItemsTheSame(
                oldItem: TourTagModelDC,
                newItem: TourTagModelDC
            ): Boolean {
                return oldItem.hashTag == newItem.hashTag
            }

            override fun areContentsTheSame(
                oldItem: TourTagModelDC,
                newItem: TourTagModelDC
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}