package com.sdin.tourstamprally.adapter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.VisithistoryItemBinding
import com.sdin.tourstamprally.model.ReviewWriter
import com.sdin.tourstamprally.model.history_spotModel2
import com.sdin.tourstamprally.ui.dialog.Del_Review_Dialog
import com.sdin.tourstamprally.ui.dialog.NoReview_Dialog
import com.sdin.tourstamprally.utill.listener.ItemCliclListener
import com.sdin.tourstamprally.utill.listener.ReviewListener
import java.text.ParseException
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class ViSitReAdapter(
    private val historySpotList: ArrayList<history_spotModel2>,
    private val context: Context,
    private val reviewListener: ReviewListener
) : RecyclerView.Adapter<ViSitReAdapter.ViewHolder>() {

    private val selectedItems = SparseBooleanArray()
    private var listener: ItemCliclListener? = null
    private var prePosition = -1
    private var oldSdf: SimpleDateFormat? = null
    private var newSdf: SimpleDateFormat? = null
    private var timeSdf: SimpleDateFormat? = null

    private var delPosition = -1

    init {
        oldSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
        newSdf = SimpleDateFormat("yy.MM.dd")
        timeSdf = SimpleDateFormat("HH:mm")
    }

    fun itemClickListener(listener: ItemCliclListener) {
        this.listener = listener
    }

    inner class ViewHolder(val binding: VisithistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //private var onViewHolderItemClickListener: OnViewHolderItemClickListener? = null

        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun onBind(model: history_spotModel2, selectedItems: SparseBooleanArray) {
            binding.seekBar.isEnabled = false
            binding.titleTxv.text = model.touristspot_name
            binding.explanTxv.text = model.touristspot_explan

            val allCount = model.allCount
            val myCount = model.myCount
            val allCounted = (myCount.toDouble() / allCount.toDouble() * 100).toInt()
            binding.seekBar.max = allCount
            binding.seekBar.progress = myCount

            binding.logoImv.setOnClickListener {
                if (allCounted == 100) {
                    listener?.clearClick(model.touristspot_idx)
                }
            }


            if (allCounted != 100) {
                binding.dateTxv.text = "$allCounted%"
                Glide.with(binding.logoImv.context).load(R.drawable.logo_gray).into(binding.logoImv)
            } else {
                Glide.with(binding.logoImv.context)
                    .load(R.drawable.visit_logo)
                    .into(binding.logoImv)
                try {
                    val oldDate = oldSdf?.parse(model.touristhistory_updatetime)
                    //Log.wtf("update", model.touristhistory_updatetime)
                    if (oldDate != null) {
                        val nDate = newSdf!!.format(oldDate)
                        val nTime = timeSdf!!.format(oldDate)
                        //Log.wtf("nTime", nTime)
                        binding.dateTxv.text = "$nDate $nTime"
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                    binding.dateTxv.text = model.touristhistory_updatetime
                }
            }

            binding.ratingbar.setOnTouchListener { _, _ -> true }

            binding.heartImv.setOnClickListener {
                if (listener != null && absoluteAdapterPosition > -1) {
                    listener?.deapsClick(
                        absoluteAdapterPosition,
                        historySpotList[absoluteAdapterPosition]
                    )
                }
            }

            binding.swipeView.setOnClickListener {
                onViewHolderItemClick(absoluteAdapterPosition)
                //onViewHolderItemClickListener?.onViewHolderItemClick()
            }

            Glide.with(binding.visitHistoryImv.context)
                .load("http://coratest.kr/imagefile/bsr/" + model.touristspot_img)
                .placeholder(R.drawable.sample_profile_image)
                .error(
                    ContextCompat.getDrawable(
                        binding.visitHistoryImv.context,
                        R.drawable.sample_profile_image
                    )
                )
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(binding.visitHistoryImv)

            if (model.review_idx > 0) {
                binding.reviewLayout.visibility = View.VISIBLE
                binding.nameTxv.text = Utils.User_Name
                Glide.with(binding.profileIcon.context)
                    .load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile).circleCrop()
                    .placeholder(R.drawable.sample_profile_image)
                    .error(
                        ContextCompat.getDrawable(
                            binding.visitHistoryImv.context,
                            R.drawable.sample_profile_image
                        )
                    )
                    .into(binding.profileIcon)
                binding.ratingbar.rating = model.review_score
                binding.reviewExplan.text = model.review_contents
            } else {
                binding.reviewLayout.visibility = View.GONE
            }


            if (model.review_idx > 0) {

                binding.delReview.setOnClickListener {
                    delPosition = absoluteAdapterPosition
                    val dialog = Del_Review_Dialog(context)
                    dialog.setClickListener {
                        listener!!.delReview(
                            historySpotList[delPosition].review_idx,
                            delPosition
                        )
                        historySpotList[delPosition].review_idx = 0
                        historySpotList[delPosition].review_contents = ""
                        onViewHolderItemClick(absoluteAdapterPosition)
                        //onViewHolderItemClickListener!!.onViewHolderItemClick()
                    }
                    dialog.show()
                }

                binding.gotoReview.setOnClickListener {
                    reviewListener.onWriteReviewClick(
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
                    reviewListener.onWriteReviewClick(
                        ReviewWriter(
                            model.touristspot_idx,
                            model.touristspot_name,
                            true
                        )
                    )
                }
                binding.delReview.setOnClickListener {
                    val dialog = NoReview_Dialog(context)
                    dialog.show()
                }
            }

            changeVisibility(selectedItems[absoluteAdapterPosition])
        }

        @SuppressLint("Recycle")
        private fun changeVisibility(isExpanded: Boolean) {
            val va = if (isExpanded) {
                ValueAnimator.ofInt(0, 600)
            } else {
                ValueAnimator.ofInt(600, 0)
            }

            va.duration = 100
            va.addUpdateListener {
                binding.reviewParentLayout.layoutParams.height = it.animatedValue as Int
                binding.reviewParentLayout.requestLayout()
                binding.reviewParentLayout.visibility =
                    if (isExpanded) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }

            va.start()
        }

    }


    fun setList(historySpotList: ArrayList<history_spotModel2>) {
        this.historySpotList.clear()
        this.historySpotList.addAll(historySpotList)
    }

    fun setChange(position: Int) {
        var model = historySpotList[position]
        model = history_spotModel2(
            model.allCount, model.myCount,
            model.location_idx, model.location_name,
            model.touristspot_idx, model.touristspot_name,
            model.touristspot_explan, model.touristspot_latitude,
            model.touristspot_longitude, model.touristspot_img,
            model.touristhistory_updatetime, 0, 0F, null
        )

        historySpotList[position] = model
        notifyItemChanged(position)
    }

    private fun onViewHolderItemClick(position: Int) {
        if (selectedItems[position]) {
            // 펼쳐진 Item을 클릭 시
            selectedItems.delete(position)
        }else {
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
        //reviewListener = parent.context
        return ViewHolder(
            VisithistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(model = historySpotList[position], selectedItems = selectedItems)
    }

    override fun getItemCount(): Int = historySpotList.size

}