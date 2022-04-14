package com.sdin.tourstamprally.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.ItemReCommentsBinding
import com.sdin.tourstamprally.model.ReveiwCommentsDC
import com.sdin.tourstamprally.ui.dialog.reviewcommnet.ReportDialog
import java.text.SimpleDateFormat
import java.util.*

class ReviewCommentsAdapter(val list: MutableList<ReveiwCommentsDC>, val callback : (ReveiwCommentsDC) -> Unit) :
    RecyclerView.Adapter<ReviewCommentsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemReCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ReveiwCommentsDC) {
            binding.commentTxv.text = data.review_comment_content

            binding.userNameTxv.text = data.user_name
            val oldTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            val newTimeFormat = SimpleDateFormat("yyyy.MM.dd a H:mm", Locale.KOREA)
            val date = oldTimeFormat.parse(data.review_comment_updatetime)
            val newDate: String = newTimeFormat.format(date)
            binding.dataTxv.text = newDate

            //it.dataTxv.text = newDate
            Glide.with(binding.userProfileImv.context)
                .load("http://coratest.kr/imagefile/bsr/" + data.user_profile)
                .placeholder(R.drawable.sample_profile_image)
                .circleCrop()
                .error(R.drawable.sample_profile_image)
                .into(binding.userProfileImv)

            binding.reportTxv.setOnClickListener {
                callback(data)
            }
        }
    }

    fun removeList(index : Int) {
        notifyItemRemoved(index)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReCommentsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}