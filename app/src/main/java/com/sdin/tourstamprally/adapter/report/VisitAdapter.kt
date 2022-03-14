package com.sdin.tourstamprally.adapter.report

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.VisithistoryItemBinding
import com.sdin.tourstamprally.model.HistorySpotModel
import com.sdin.tourstamprally.model.ReviewWriter
import com.sdin.tourstamprally.model.history_spotModel2
import com.sdin.tourstamprally.ui.dialog.Del_Review_Dialog
import com.sdin.tourstamprally.ui.dialog.NoReview_Dialog
import com.sdin.tourstamprally.utill.listener.VisitItemClickListener
import java.text.ParseException
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class VisitAdapter(
    private val visitItemClickListener : VisitItemClickListener,
    private val reviewWriterCallback : (ReviewWriter) -> Unit

) :
    ListAdapter<HistorySpotModel, VisitAdapter.ViewHolder>(differ) {

    private var oldSdf: SimpleDateFormat? = null
    private var newSdf: SimpleDateFormat? = null
    private var timeSdf: SimpleDateFormat? = null

    private val selectedItems = SparseBooleanArray()
    private var prePosition = -1
    private var delPosition = -1

    init {
        oldSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
        newSdf = SimpleDateFormat("yy.MM.dd")
        timeSdf = SimpleDateFormat("HH:mm")
    }

    inner class ViewHolder(val binding: VisithistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun onBind(model: HistorySpotModel) {
            with(binding) {
                seekBar.isEnabled = false
                titleTxv.text = model.touristspot_name
                explanTxv.text = model.touristspot_explan

                val allCount = model.allCount
                val myCount = model.myCount
                val allCounted = (myCount.toDouble() / allCount.toDouble() * 100).toInt()

                seekBar.max = allCount
                seekBar.progress = myCount

                logoImv.setOnClickListener {
                    if (allCounted == 100) {
                        visitItemClickListener.clearClick(model, absoluteAdapterPosition)
                    }
                }

                if (allCounted != 100) {
                    dateTxv.text = "$allCounted%"
                    Glide.with(logoImv.context)
                        .load(R.drawable.logo_gray)
                        .into(logoImv)
                } else {
                    Glide.with(logoImv.context)
                        .load(R.drawable.visit_logo)
                        .into(logoImv)

                    try {
                        val oldDate = oldSdf?.parse(model.touristhistory_updatetime)
                        if (oldDate != null) {
                            val nDate = newSdf!!.format(oldDate)
                            val nTime = timeSdf!!.format(oldDate)
                            binding.dateTxv.text = "$nDate $nTime"
                        } else {
                            binding.dateTxv.text = model.touristhistory_updatetime
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        binding.dateTxv.text = model.touristhistory_updatetime
                    }
                }

                ratingbar.setOnTouchListener { _, _ -> true }

                heartImv.setOnClickListener {
                    visitItemClickListener.deapClick(
                        position = absoluteAdapterPosition,
                        model = model
                    )
                }

                swipeView.setOnClickListener {
                    onViewHolderItemClick(absoluteAdapterPosition)
                }

                Glide.with(visitHistoryImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.touristspot_img)
                    .placeholder(R.drawable.sample_profile_image)
                    .error(R.drawable.sample_profile_image)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(visitHistoryImv)


                if (model.review_idx > 0) {
                    reviewLayout.visibility = View.VISIBLE
                    nameTxv.text = Utils.User_Name

                    Glide.with(binding.profileIcon.context)
                        .load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile).circleCrop()
                        .placeholder(R.drawable.sample_profile_image)
                        .error(R.drawable.sample_profile_image)
                        .into(binding.profileIcon)

                    ratingbar.rating = model.review_score
                    reviewExplan.text = model.review_contents

                    delReview.setOnClickListener {
                        delPosition = absoluteAdapterPosition
                        val dialog = Del_Review_Dialog(it.context)
                        dialog.setClickListener {
                            visitItemClickListener.deleteReview(
                                model.review_idx,
                                delPosition
                            )

                            model.review_idx = 0
                            model.review_contents = ""
                            onViewHolderItemClick(absoluteAdapterPosition)
                        }
                        dialog.show()


                    }

                    gotoReview.setOnClickListener {

                        reviewWriterCallback(
                            ReviewWriter(
                                model.touristspot_idx,
                                model.touristspot_name,
                                false,
                                model.review_idx,
                                model.review_score,
                                model.review_contents,
                                null
                            )
                        )
                    }



                } else {
                    binding.gotoReview.setOnClickListener {
                        reviewWriterCallback(
                            ReviewWriter(
                                model.touristspot_idx,
                                model.touristspot_name,
                                true
                            )
                        )
                    }

                    delReview.setOnClickListener {
                        NoReview_Dialog(it.context).show()
                    }
                    reviewLayout.visibility = View.GONE
                }

                changeVisibility(selectedItems[absoluteAdapterPosition])
            }
        }

        @SuppressLint("Recycle")
        private fun changeVisibility(isExpanded: Boolean) {
            val animator = if (isExpanded) {
                ValueAnimator.ofInt(0, 600)
            } else {
                ValueAnimator.ofInt(600, 0)
            }

            animator.duration = 100
            animator.addUpdateListener {
                binding.reviewParentLayout.apply {
                    layoutParams.height = it.animatedValue as Int
                    requestLayout()
                    visibility = if (isExpanded) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            animator.start()
        }

    }

    fun setChange(position: Int) {
        var model = currentList[position]
        model = HistorySpotModel(
            model.allCount, model.myCount,
            model.location_idx, model.location_name,
            model.touristspot_idx, model.touristspot_name,
            model.touristspot_explan,  model.touristspot_img,
            model.touristhistory_updatetime, 0, 0F, null,
            model.couponCount, model.coupon_idx, model.coupon_number, model.coupon_status, model.coupon_createtime
        )

        currentList[position] = model
        notifyItemChanged(position)
    }


    private fun onViewHolderItemClick(position: Int) {
        if (selectedItems[position]) {
            // 펼쳐진 Item을 클릭 시
            selectedItems.delete(position)
        } else {
            // 직전의 클릭됐던 Item의 클릭상태를 지움
            selectedItems.delete(position)
            // 클릭한 Item의 position을 저장
            selectedItems.put(position, true)
        }

        // 해당 포지션의 변화를 알림
        if (prePosition != -1) notifyItemChanged(prePosition)
        notifyItemChanged(position)
        // 클릭된 position 저장
        prePosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VisithistoryItemBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<HistorySpotModel>() {
            override fun areItemsTheSame(
                oldItem: HistorySpotModel,
                newItem: HistorySpotModel
            ): Boolean {
                return oldItem.review_idx == newItem.review_idx
            }

            override fun areContentsTheSame(
                oldItem: HistorySpotModel,
                newItem: HistorySpotModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}