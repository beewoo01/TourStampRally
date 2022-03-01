package com.sdin.tourstamprally.adapter

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.model.StoreHashtag

class StoreDetailHashTagReAdapter : ListAdapter<StoreHashtag, StoreDetailHashTagReAdapter.ItemViewHolder>(differ){

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(model: StoreHashtag) {
            val tagTxv = view.findViewById<TextView>(R.id.tag_txv)
            tagTxv.text = "#${model.store_hashtag_tag}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.re_item_store_hashtag, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        val differ = object : DiffUtil.ItemCallback<StoreHashtag>() {

            override fun areItemsTheSame(
                oldItem: StoreHashtag,
                newItem: StoreHashtag
            ): Boolean {
                return oldItem.store_hashtag_idx == newItem.store_hashtag_idx
            }

            override fun areContentsTheSame(
                oldItem: StoreHashtag,
                newItem: StoreHashtag
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    class PhOffsetItemDecoration(val padding : Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = padding
            outRect.right = padding
        }
    }
}