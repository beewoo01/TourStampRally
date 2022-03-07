package com.sdin.tourstamprally.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.Review_Main_ReAdapter
import com.sdin.tourstamprally.adapter.review.ReviewMainReAdapter
import com.sdin.tourstamprally.databinding.FragmentMainBinding
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding
import com.sdin.tourstamprally.model.AllReviewDTO
import com.sdin.tourstamprally.model.AllReviewModel
import com.sdin.tourstamprally.model.Location_four
import com.sdin.tourstamprally.model.TopFourLocationModel
import com.sdin.tourstamprally.ui.activity.MainActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment2 : BaseFragment() {

    private lateinit var viewBind: FragmentMainBinding
    private val tourList: MutableList<TopFourLocationModel> = mutableListOf()
    private val listener by lazy {
        requireActivity() as MainActivity
    }
    /*private ItemOnClick listener;*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBind = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        viewBind.fragment = this@MainFragment2
        viewBind.tourRallyPgb.visibility = View.VISIBLE
        viewBind.rallyRecyclerview.layoutManager = object : GridLayoutManager(requireContext(), 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        getTop4Location()
        getAllReview()
        return viewBind.root
    }

    private fun getTop4Location() {
        apiService.getFourLocations(Utils.User_Idx)
            .enqueue(object : Callback<List<TopFourLocationModel>> {
                override fun onResponse(
                    call: Call<List<TopFourLocationModel>>,
                    response: Response<List<TopFourLocationModel>>
                ) {
                    viewBind.tourRallyPgb.visibility = View.GONE
                    response.body()?.let {
                        tourList.addAll(it)
                        setData(tourList)
                    }

                }

                override fun onFailure(call: Call<List<TopFourLocationModel>>, t: Throwable) {
                    t.printStackTrace()
                    viewBind.tourRallyPgb.visibility = View.GONE
                }


            })
    }

    private fun setData(list: MutableList<TopFourLocationModel>) {
        val adapter = RallyRecyclerviewAdapter(context = requireContext(), list)
        viewBind.rallyRecyclerview.apply {
            this.adapter = adapter
        }
    }

    private fun getAllReview() {
        apiService.select_all_review().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<AllReviewModel>>() {
                override fun onSuccess(t: List<AllReviewModel>) {
                    viewBind.reviewReProgressbar.visibility = View.GONE
                    initReviewData(arrayListOf<AllReviewModel>().apply {
                        addAll(t)
                    })
                }

                override fun onError(e: Throwable) {
                    viewBind.reviewReProgressbar.visibility = View.GONE
                }

            })
    }

    fun cuverClick() {
        //공지사항
        findNavController().navigate(
            R.id.notice,
            Bundle().apply {
                putInt("state", 3)
                putString("title", "공지사항")
            })
    }

    fun moreClick() {
        //더보기
        val action = MainFragment2Directions.actionMainfragmentToFragmentDirectionGuid(
            tourList.toTypedArray()
        )
        findNavController().navigate(action)
        /*findNavController().navigate(R.id.action_mainfragment_to_fragment_direction_guid,
            Bundle().apply {
                putParcelableArrayList("model", ArrayList<Location_four>().apply {
                    addAll(tourList)
                })
            }
        )*/
    }

    fun reviewMoreClick() {
        //리뷰보기
        Log.wtf("reviewMoreClick", "reviewMoreClick")
        val action = MainFragment2Directions.actionMainfragmentToFragmentMoreReview("리뷰보기")
        findNavController().navigate(action)

        /*findNavController().navigate(
            R.id.action_mainfragment_to_fragment_more_review,
            Bundle().apply {
                putString("title", "리뷰보기")
            }
        )*/
    }

    private fun initReviewData(reviewDataList: MutableList<AllReviewModel>) {
        viewBind.mainReviewRecyclerview.apply {
            adapter =
                ReviewMainReAdapter() { model ->
                    val action = MainFragment2Directions.actionMainfragmentToFragmentReviewComents(
                        title = model.touristspot_name,
                        state = 4,
                        reviewIdx = model.review_idx
                    )

                    findNavController().navigate(action)

                }.apply {
                    submitList(reviewDataList)
                }

            layoutManager = LinearLayoutManager(requireContext())

        }

    }


    inner class RallyRecyclerviewAdapter(
        val context: Context,
        val adapterList: MutableList<TopFourLocationModel>
    ) : RecyclerView.Adapter<RallyRecyclerviewAdapter.ViewHolder>() {
        inner class ViewHolder(private val holderBinding: StepRallyLocationItemBinding) :
            RecyclerView.ViewHolder(holderBinding.root) {
            /*init {
                holderBinding.holder = this
            }*/
            @SuppressLint("SetTextI18n")
            fun onBind(model: TopFourLocationModel) {
                holderBinding.location.text = model.location_name
                holderBinding.stepRallyBg.setOnClickListener {
                    val action = MainFragment2Directions.actionMainfragmentToFragmentLocation(
                        model.location_name + " 랠리 맵",
                        model
                    )


                    if (model.touristspotpoint_createtime == null) {
                        Toast.makeText(requireContext(), "등록된 관광지가 없습니다.", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    findNavController().navigate(action)
                }

                Glide.with(context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.location_img)
                    .error(R.drawable.sample_bg)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            holderBinding.mainLayout.background = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) = Unit

                    })

                setPopular(model)
                if (model.allPointCount > 0) {
                    setFavorite(model)
                }

                holderBinding.dibsImv.setOnClickListener {
                    holderBinding.dibsImv.isEnabled = false
                    if (model.allSpotCount == model.myInterCount) {

                        Glide.with(it.context).load(R.drawable.heart_resize)
                            .into(holderBinding.dibsImv)

                        apiService.multipleDelDeaps(
                            Utils.User_Idx,
                            model.location_idx
                        ).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<Int>() {

                                override fun onSuccess(t: Int) {
                                    holderBinding.dibsImv.isEnabled = true
                                    model.myInterCount = 0
                                }

                                override fun onError(e: Throwable) {
                                    Glide.with(it.context).load(R.drawable.full_heart_resize)
                                        .into(holderBinding.dibsImv)
                                    holderBinding.dibsImv.isEnabled = true
                                    e.printStackTrace()
                                }
                            })
                    } else {
                        //DEAP 추가
                        Glide.with(it.context).load(R.drawable.full_heart_resize)
                            .into(holderBinding.dibsImv)

                        apiService.multipleInserDeaps(
                            Utils.User_Idx,
                            model.location_idx
                        ).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<Int>() {
                                override fun onSuccess(integer: Int) {
                                    model.myInterCount = model.allSpotCount
                                    holderBinding.dibsImv.isEnabled = true

                                }

                                override fun onError(e: Throwable) {
                                    Glide.with(it.context).load(R.drawable.heart_resize)
                                        .into(holderBinding.dibsImv)
                                    holderBinding.dibsImv.isEnabled = true
                                    e.printStackTrace()
                                }
                            })
                    }
                }

                val allCountd =
                    (model.myHistoryCount.toDouble() / model.allPointCount.toDouble() * 100).toInt()
                holderBinding.seekbar.max = model.allPointCount
                holderBinding.seekbar.progress = model.myHistoryCount
                holderBinding.seekTxv.text = "$allCountd%"

            }


            private fun setPopular(model: TopFourLocationModel) {
                if (model.popular > model.allPointCount) {
                    holderBinding.newsImv.visibility = View.VISIBLE
                    //Glide.with(context).load(R.drawable.hot_icon).into(holderBinding.newsImv)
                    setGlide(holderBinding.newsImv, R.drawable.hot_icon)
                } else {
                    holderBinding.newsImv.visibility = View.VISIBLE
                    setGlide(holderBinding.newsImv, R.drawable.new_icon)
                    //Glide.with(context).load(R.drawable.new_icon).into(holderBinding.newsImv)
                }
            }

            private fun setFavorite(model: TopFourLocationModel) {
                if (model.allSpotCount == model.myInterCount) {
                    setGlide(holderBinding.dibsImv, R.drawable.full_heart_resize)
                } else {
                    setGlide(holderBinding.dibsImv, R.drawable.heart_resize)
                }
            }


            private fun setGlide(target: ImageView, img: Int) {
                Glide.with(context).load(img).into(target)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                StepRallyLocationItemBinding.inflate(
                    LayoutInflater.from(requireContext()),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(adapterList[position])
        }

        override fun getItemCount(): Int = 4

    }


}