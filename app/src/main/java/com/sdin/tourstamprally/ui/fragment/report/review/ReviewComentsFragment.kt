package com.sdin.tourstamprally.ui.fragment.report.review

import android.annotation.SuppressLint
import android.content.Context
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.ReviewCommentsAdapter
import com.sdin.tourstamprally.data.UserInfo
import com.sdin.tourstamprally.databinding.FragmentReviewComentsBinding
import com.sdin.tourstamprally.model.ReveiwCommentsDC
import com.sdin.tourstamprally.model.ReviewDetailDC
import com.sdin.tourstamprally.model.ReviewImg
import com.sdin.tourstamprally.ui.dialog.reviewcommnet.ReportDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReviewComentsFragment : BaseFragment() {

    private var review_idx: Int? = null
    private var reviewUserIdx : Int = 0
    private var binding: FragmentReviewComentsBinding? = null
    private var likeState = false
    private val mutableList = arrayListOf<ReveiwCommentsDC>()
    private val args: ReviewComentsFragmentArgs by navArgs()
    private lateinit var commentAdapter: ReviewCommentsAdapter
    private var originReviewDetailModel: ReviewDetailDC? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review_coments, container, false)

        review_idx = args.reviewIdx
        reviewUserIdx = args.reviewUserIdx

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        binding?.let { it ->
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
                    apiService.inordel_review_interest(it1, Utils.User_Idx)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Int>() {
                            @SuppressLint("SetTextI18n")
                            override fun onSuccess(t: Int?) {
                                t?.let { result ->
                                    if (result > 0) {
                                        originReviewDetailModel?.let {
                                            it.interestCount = if (likeState) {
                                                it.interestCount - 1
                                            } else {
                                                it.interestCount + 1
                                            }
                                            likeState = !likeState
                                            binding?.likeCountTxv?.text = "좋아요 ${it.interestCount}개"

                                        }
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

            it.writeImb.setOnClickListener { _ ->
                if (!TextUtils.isEmpty(it.writeCommentEdt.text) && it.writeCommentEdt.text.isNotEmpty()) {
                    review_idx?.let { reviewidx ->
                        apiService.insert_review_comment(
                            reviewidx,
                            Utils.User_Idx,
                            it.writeCommentEdt.text.toString()
                        )
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<Int>() {
                                override fun onSuccess(t: Int?) {
                                    t?.let { result ->
                                        if (result > 0) {
                                            it.writeCommentEdt.setText("")
                                            Toast.makeText(
                                                requireContext(),
                                                "댓글이 작성이 완료되었습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
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
                    Toast.makeText(requireContext(), "댓글을 작성해 주세요", Toast.LENGTH_SHORT).show()
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
        binding?.let {
            originReviewDetailModel = model
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
            data?.let { formatterDate ->
                val newDate: String = newTimeFormat.format(formatterDate)

                it.dataTxv.text = newDate
            }

            if (model.interestStatus == 1) {
                likeState = true
                Glide.with(requireContext())
                    .load(R.drawable.full_heart_resize)
                    .error(R.drawable.heart_resize)
                    .into(it.likeImb)
            }


            if (model.reviewImgs.isNullOrEmpty()) {
                val subImgsList = mutableListOf<ReviewImg>()
                subImgsList.add(ReviewImg(0, review_idx!!, model.touristspot_img, null, null))
                model.reviewImgs = subImgsList
            }

            initViewPager(model.reviewImgs!!.toMutableList())


            /*Glide.with(requireContext())
                .load("http://coratest.kr/imagefile/bsr/" + model.touristspot_img)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .error(R.drawable.sample_bg)
                .into(it.spotImv)*/

            Glide.with(requireContext())
                .load("http://coratest.kr/imagefile/bsr/" + model.user_profile)
                .error(R.drawable.sample_profile_image)
                .circleCrop()
                .into(it.userProfileImv)
        }

    }

    private fun initViewPager(images: MutableList<ReviewImg>) {
        binding?.reviewImgsViewpager?.apply {
            adapter = ImagesViewPager(images)
        }
    }


    private fun getCommentsData() {
        review_idx?.let {
            apiService.select_review_comments(it, Utils.User_Idx)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<ReveiwCommentsDC>?>() {
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
        binding?.commentsRecyclerview?.apply {
            mutableList.clear()
            mutableList.addAll(list)
            commentAdapter = ReviewCommentsAdapter(mutableList) { model ->
                ReportDialog { cause ->
                    Log.wtf("model.user_name", model.user_name)
                    val index = commentAdapter.list.indexOf(model)
                    mutableList.remove(model)
                    commentAdapter.removeList(index)
                    reportComment(model, cause)
                    Toast.makeText(requireContext(), cause, Toast.LENGTH_SHORT).show()

                }.show(requireActivity().supportFragmentManager, "")
            }
            adapter = commentAdapter
        }
    }

    private fun reportComment(model: ReveiwCommentsDC, cause: String) {

        apiService.insert_review_comment_report(
            model.review_comment_idx,
            Utils.User_Idx,
            cause,
            reviewUserIdx
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(t: Int) {
                    if (t > 0) {
                        Toast.makeText(requireContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }


    class ImagesViewPager(private val images: MutableList<ReviewImg>) :
        RecyclerView.Adapter<ImagesViewPager.ViewHolder>() {
        class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            fun onBind(model: ReviewImg) {
                val imageFilterView = view.findViewById<ImageFilterView>(R.id.imageFilterView)
                Glide.with(imageFilterView.context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.review_img_url)
                    .into(imageFilterView)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater =
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.review_detail_images, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(images[position])
        }

        override fun getItemCount(): Int = images.size
    }

}