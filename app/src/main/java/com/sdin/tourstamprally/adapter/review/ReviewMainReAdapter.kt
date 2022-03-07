package com.sdin.tourstamprally.adapter.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.ReItemReviewMainBinding
import com.sdin.tourstamprally.model.AllReviewModel

class ReviewMainReAdapter(val callback: (AllReviewModel) -> Unit) :
    ListAdapter<AllReviewModel, ReviewMainReAdapter.ViewHolder>(differ) {

    inner class ViewHolder(private val binding: ReItemReviewMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(model: AllReviewModel) {
            binding.apply {
                locationTxv.text = model.location_name
                spotpointNameTxv.text = model.touristspot_name
                userNameTxv.text = model.user_name
                reviewTxv.text = model.review_contents
                ratingbar.rating = model.review_score

                Glide.with(userProfileImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.user_profile)
                    .placeholder(R.drawable.sample_profile_image)
                    .circleCrop()
                    .error(R.drawable.sample_profile_image)
                    .into(userProfileImv)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReItemReviewMainBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemCount(): Int {
        return when {
            currentList.size in 1..3 -> {
                currentList.size
            }
            currentList.size > 3 -> {
                3
            }
            else -> {
                0
            }
        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<AllReviewModel>() {
            override fun areItemsTheSame(
                oldItem: AllReviewModel,
                newItem: AllReviewModel
            ): Boolean {
                return oldItem.review_idx == newItem.review_idx
            }

            override fun areContentsTheSame(
                oldItem: AllReviewModel,
                newItem: AllReviewModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}