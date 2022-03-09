package com.sdin.tourstamprally.adapter.review

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.WriteReviewPictureItemBinding
import com.sdin.tourstamprally.model.ReviewImg

class WriteReviewPictureAdapter :
    ListAdapter<ReviewImg, WriteReviewPictureAdapter.ViewHolder>(differ) {

    inner class ViewHolder(private val binding: WriteReviewPictureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(model: ReviewImg) {
            with(binding) {

                if (absoluteAdapterPosition == 0) {
                    if (currentList.size > 5) {
                        firstItemGroup.visibility = View.VISIBLE
                        closeImb.visibility = View.GONE
                        selectedImv.visibility = View.GONE
                        topContainer.setOnClickListener {
                            Log.wtf("topContainer click ", "position : $absoluteAdapterPosition")
                        }

                        closeImb.setOnClickListener {
                            Log.wtf("closeImb click ", "position : $absoluteAdapterPosition")
                        }

                    } else {
                        firstItemGroup.visibility = View.GONE
                        closeImb.visibility = View.VISIBLE
                        selectedImv.visibility = View.VISIBLE


                        Glide.with(selectedImv.context)
                            .load("http://coratest.kr/imagefile/bsr/" + model.review_img_url)
                            .placeholder(R.drawable.sample_bg)
                            .error(R.drawable.sample_bg)
                            .into(selectedImv)
                    }


                } else {
                    firstItemGroup.visibility = View.GONE
                    closeImb.visibility = View.VISIBLE
                    selectedImv.visibility = View.VISIBLE


                    Glide.with(selectedImv.context)
                        .load("http://coratest.kr/imagefile/bsr/" + model.review_img_url)
                        .placeholder(R.drawable.sample_bg)
                        .error(R.drawable.sample_bg)
                        .into(selectedImv)
                }
            }


        }

    }

    override fun getItemCount(): Int {
        return if (currentList.size > 5) {
            5
        } else {
            super.getItemCount()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WriteReviewPictureItemBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<ReviewImg>() {
            override fun areItemsTheSame(oldItem: ReviewImg, newItem: ReviewImg): Boolean {
                return oldItem.reviewImgIdx == newItem.reviewImgIdx
            }

            override fun areContentsTheSame(oldItem: ReviewImg, newItem: ReviewImg): Boolean {
                return oldItem == newItem
            }

        }
    }


}