package com.sdin.tourstamprally.adapter.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.ReItemMorereviewBinding
import com.sdin.tourstamprally.model.AllReviewDTO

class MoreFragReviewAdapter(val context: Context, var list: ArrayList<AllReviewDTO>) :
        RecyclerView.Adapter<MoreFragReviewAdapter.ViewHolder>() {

    private var litener : MoreReviewListener? = null

    interface MoreReviewListener {
        fun onItemClick(data : AllReviewDTO)
    }


    fun setListener(litener: MoreReviewListener){
        this.litener = litener
    }


    @SuppressLint("NotifyDataSetChanged")
    fun changeList(list : ArrayList<AllReviewDTO>){
        this.list = list
        notifyDataSetChanged()

    }

    inner class ViewHolder(val binding: ReItemMorereviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AllReviewDTO) {
                binding.locationTxv.text = data.location_name
                binding.ratingbar.rating = data.review_score
                binding.userNameTxv.text = data.user_name
                binding.reviewContentTxv.text = data.review_contents
                binding.tourspotName.text = data.touristspot_name

                Glide.with(binding.userProfileImv.context)
                        .load("http://coratest.kr/imagefile/bsr/" + data.user_profile)
                        .circleCrop()
                        .error(R.drawable.sample_profile_image)
                        .into(binding.userProfileImv)


                Glide.with(binding.tourspotBgImv.context)
                        .load("http://coratest.kr/imagefile/bsr/" + data.touristspot_img)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                        .error(R.drawable.sample_bg)
                        .into(binding.tourspotBgImv)

                binding.itemContainer.setOnClickListener{
                    litener?.onItemClick(data = data)
                }



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ReItemMorereviewBinding.inflate(
                        LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}