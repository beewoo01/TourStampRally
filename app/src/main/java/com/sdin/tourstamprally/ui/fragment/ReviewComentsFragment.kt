package com.sdin.tourstamprally.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.ReviewCommentsAdapter
import com.sdin.tourstamprally.databinding.FragmentReviewComentsBinding
import com.sdin.tourstamprally.model.ReveiwCommentsDC
import com.sdin.tourstamprally.model.ReviewDetailDC
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


class ReviewComentsFragment : BaseFragment() {

    private var review_idx: Int? = null

    private var bining: FragmentReviewComentsBinding? = null

    private var likeState = false

    val mutableList = arrayListOf<ReveiwCommentsDC>()
    lateinit var commentAdapter: ReviewCommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            review_idx = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        bining = DataBindingUtil.inflate(inflater, R.layout.fragment_review_coments, container, false)
        return bining?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        bining?.let { it ->
            it.likeImb.setOnClickListener { view ->
                if (likeState) {
                    Glide.with(view.context)
                            .load(R.drawable.heart_resize)
                            .error(R.drawable.heart_resize)
                            .into(it.likeImb)
                } else {
                    Glide.with(view.context)
                            .load(R.drawable.full_heart_resize)
                            .error(R.drawable.heart_resize)
                            .into(it.likeImb)
                }

                review_idx?.let { it1 ->
                    apiService.inordel_review_interest(it1, Utils.User_Idx).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<Int>() {
                                override fun onSuccess(t: Int?) {
                                    t?.let { result ->
                                        if (result > 0) {
                                            Log.wtf("like OR Delete", "성공")
                                        }
                                    }
                                }

                                override fun onError(e: Throwable?) {
                                    e?.printStackTrace()
                                }

                            })
                }
            }

            it.writeImb.setOnClickListener { view ->
                if (!TextUtils.isEmpty(it.writeCommentEdt.text) && it.writeCommentEdt.text.isNotEmpty()) {
                    review_idx?.let { reviewidx ->
                        apiService.insert_review_comment(reviewidx, Utils.User_Idx, it.writeCommentEdt.text.toString())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object : DisposableSingleObserver<Int>() {
                                    override fun onSuccess(t: Int?) {
                                        t?.let { result ->
                                            if (result > 0) {
                                                it.writeCommentEdt.setText("")
                                                Toast.makeText(requireContext(), "댓글이 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                                getData()
                                            }
                                        }
                                    }

                                    override fun onError(e: Throwable?) {
                                        e?.printStackTrace()
                                    }

                                })

                    }

                } else {
                    Toast.makeText(requireContext(), "답글을 작성해 주세요", Toast.LENGTH_SHORT).show()
                }


            }
        }

    }

    private fun getData() {
        review_idx?.let {
            apiService.select_review_detail(it, Utils.User_Idx).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<ReviewDetailDC>() {
                        override fun onSuccess(t: ReviewDetailDC?) {
                            t?.let { model ->
                                setReviewData(model)
                                getCommentsData()
                            }
                        }

                        override fun onError(e: Throwable?) {
                            e?.printStackTrace()
                        }

                    })
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setReviewData(model: ReviewDetailDC) {
        bining?.let {
            it.userNameTxv.text = model.user_name
            it.ratingbar.rating = model.review_score
            it.spotNameTxv.text = model.touristspot_name
            it.reviewContentTxv.text = model.review_contents
            it.likeCountTxv.text = "좋아요 ${model.interestCount}개"
            it.commentCountTxv.text = "댓글 ${model.commentCount}개"
            it.commentTxv.text = "댓글 ${model.commentCount}"

            val oldTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            val newTimeFormat = SimpleDateFormat("yyyy.MM.dd a H:mm", Locale.KOREA)
            val data = oldTimeFormat.parse(model.review_updatetime)
            val newDate: String = newTimeFormat.format(data)

            it.dataTxv.text = newDate
            if (model.interestStatus == 1) {
                likeState = true
                Glide.with(requireContext())
                        .load(R.drawable.full_heart_resize)
                        .error(R.drawable.heart_resize)
                        .into(it.likeImb)
            }

            Glide.with(requireContext())
                    .load("http://coratest.kr/imagefile/bsr/" + model.touristspot_img)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .error(R.drawable.sample_bg)
                    .into(it.spotImv)

            Glide.with(requireContext())
                    .load("http://coratest.kr/imagefile/bsr/" + model.user_profile)
                    .error(R.drawable.sample_profile_image)
                    .circleCrop()
                    .into(it.userProfileImv)
        }

    }


    private fun getCommentsData() {
        review_idx?.let {
            apiService.select_review_comments(it).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<List<ReveiwCommentsDC>>() {
                        override fun onSuccess(t: List<ReveiwCommentsDC>?) {
                            t?.let { list ->
                                initCommentsData(list)
                            }
                        }

                        override fun onError(e: Throwable?) {
                            e?.printStackTrace()
                        }

                    })
        }

    }

    private fun initCommentsData(list: List<ReveiwCommentsDC>) {
        bining?.commentsRecyclerview?.apply {
            mutableList.clear()
            mutableList.addAll(list)
            commentAdapter = ReviewCommentsAdapter(mutableList)
            adapter = commentAdapter
        }
    }

    companion object {

        private const val ARG_PARAM1 = "review_idx"

        @JvmStatic
        fun newInstance(review_idx: Int) =
                ReviewComentsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, review_idx)
                    }
                }
    }
}