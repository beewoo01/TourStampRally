package com.sdin.tourstamprally.adapter.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding

class CategoryAdapter(private val callback: (String) -> Unit) :
    ListAdapter<String, CategoryAdapter.ViewHolder>(differ) {

    private var selectedItem = 0
    private var prevSelected = -1

    inner class ViewHolder(private val binding: DirectionGuidTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(tag : String) {
            with(binding) {
                tagItemTxv.apply {



                    if (selectedItem == absoluteAdapterPosition) {
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.bg_rounded_category_selected
                        )

                        setTextColor(ContextCompat.getColor(context, R.color.white))

                    } else {
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.bg_rounded_category
                        )

                        setTextColor(ContextCompat.getColor(context, R.color.mainColor))
                    }

                    setOnClickListener {
                        prevSelected = selectedItem
                        selectedItem = absoluteAdapterPosition
                        callback(tag)
                        notifyItemChanged(prevSelected)
                        notifyItemChanged(selectedItem)
                    }

                    text = tag
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
        val differ = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }


}