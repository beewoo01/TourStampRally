package com.sdin.tourstamprally.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.databinding.ReItemReviewLocationBinding

class More_Frag_LocationAdapter(val list: MutableList<Pair<Int, String>>, val context: Context) : RecyclerView.Adapter<More_Frag_LocationAdapter.ViewHolder>() {

    private var litener : MoreLocationItemListener? = null

    interface MoreLocationItemListener{
        fun onItemClick(item : Int)
    }

    fun setMoreLocationListener(litener : MoreLocationItemListener) {
        this.litener = litener
    }

    inner class ViewHolder(val binding: ReItemReviewLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data : Pair<Int, String>) {
            Log.wtf("More_Frag_LocationAdapter", data.second)
            binding.tagItemTxv.text = data.second
            binding.tagItemTxv.setOnClickListener {
                litener?.onItemClick(data.first)
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

    }

    override fun getItemCount(): Int = list.size

}