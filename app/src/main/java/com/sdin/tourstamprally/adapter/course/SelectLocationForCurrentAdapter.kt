package com.sdin.tourstamprally.adapter.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.databinding.SelectLocationItemBinding

class SelectLocationForCurrentAdapter(val callback: (Pair<Int, String>) -> Unit) :
    ListAdapter<Pair<Int, String>, SelectLocationForCurrentAdapter.ViewHolder>(differ) {

    inner class ViewHolder(private val binding: SelectLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: Pair<Int, String>) {
            binding.locationTxv.text = model.second
            binding.locationTxv.setOnClickListener {
                callback(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SelectLocationItemBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<Pair<Int, String>>() {
            override fun areItemsTheSame(
                oldItem: Pair<Int, String>,
                newItem: Pair<Int, String>
            ): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(
                oldItem: Pair<Int, String>,
                newItem: Pair<Int, String>
            ): Boolean {
                return oldItem == newItem
            }


        }
    }

}