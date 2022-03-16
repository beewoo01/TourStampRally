package com.sdin.tourstamprally.adapter.review

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.ReItemMorereviewBinding
import com.sdin.tourstamprally.model.AllReviewModel

class MoreFragReviewAdapter(private val callback: (AllReviewModel) -> Unit) :
    ListAdapter<AllReviewModel, MoreFragReviewAdapter.ViewHolder>(differ) {

    inner class ViewHolder(private val binding: ReItemMorereviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(model: AllReviewModel) {
            with(binding) {
                locationTxv.text = model.location_name
                ratingbar.rating = model.review_score
                userNameTxv.text = model.user_name
                reviewContentTxv.text = model.review_contents
                tourspotName.text = model.touristspot_name



                Glide.with(userProfileImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.user_profile)
                    .placeholder(R.drawable.sample_profile_image)
                    .circleCrop()
                    .error(R.drawable.sample_profile_image)
                    .into(userProfileImv)

                Glide.with(tourspotBgImv.context)
                    .load(
                        if (model.review_img_url != null) {
                            "http://coratest.kr/imagefile/bsr/" + model.review_img_url
                        } else {
                            "http://coratest.kr/imagefile/bsr/" + model.touristspot_img
                        }
                    )
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .error(R.drawable.sample_bg)
                    .into(tourspotBgImv)

                itemContainer.setOnClickListener {
                    callback(model)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReItemMorereviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[position])
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