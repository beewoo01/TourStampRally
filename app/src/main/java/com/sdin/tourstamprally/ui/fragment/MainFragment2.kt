package com.sdin.tourstamprally.ui.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.Review_Main_ReAdapter
import com.sdin.tourstamprally.databinding.FragmentMainBinding
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding
import com.sdin.tourstamprally.model.AllReviewDTO
import com.sdin.tourstamprally.model.Location_four
import com.sdin.tourstamprally.model.Tour_Spot
import com.sdin.tourstamprally.ui.activity.MainActivity
import com.sdin.tourstamprally.utill.ItemOnClick
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment2 : BaseFragment() {

    companion object {
        private const val param1 = "param1"
        private const val param2 = "param2"
    }

    private lateinit var viewBind: FragmentMainBinding
    private val tourList_test: MutableList<Location_four> = mutableListOf()
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
        //viewBind.fragment = this@MainFragment2

        viewBind.rallyRecyclerview.layoutManager = object : GridLayoutManager(requireContext(), 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }


        /*binding.rallyRecyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2) {

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        getTop4Location();
        getAllReview();*/
        return viewBind.root
    }

    private fun getTop4Location() {
        apiService.getFourLocations(Utils.User_Idx).enqueue(object : Callback<List<Location_four>> {
            override fun onResponse(
                call: Call<List<Location_four>>,
                response: Response<List<Location_four>>
            ) {
                if (tourList_test != null) {
                    //setData()
                }
            }

            override fun onFailure(call: Call<List<Location_four>>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun setData(list: MutableList<Location_four>) {
        //val adapter = RallyRecyclerviewAdapter()
    }

    private fun getAllReview() {
        apiService.select_all_review().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<AllReviewDTO>>() {
                override fun onSuccess(t: List<AllReviewDTO>) {
                    viewBind.reviewReProgressbar.visibility = View.GONE;
                    initReviewData(arrayListOf<AllReviewDTO>().apply {
                        addAll(t)
                    })
                }

                override fun onError(e: Throwable) {
                    viewBind.reviewReProgressbar.visibility = View.GONE
                }

            })
    }

    private fun initReviewData(reviewDataList: MutableList<AllReviewDTO>) {
        viewBind.mainReviewRecyclerview.apply {
            adapter =
                Review_Main_ReAdapter(arrayListOf<AllReviewDTO>().apply { addAll(reviewDataList) })
                    .apply {
                        setListener { model ->
                            listener.reviewItemClick(model.review_idx, model.touristspot_name)
                        }
                    }

            layoutManager = LinearLayoutManager(requireContext())

        }

    }


    class RallyRecyclerviewAdapter(
        val context: Context,
        val adapterList: MutableList<Location_four>
    ) : RecyclerView.Adapter<RallyRecyclerviewAdapter.ViewHolder>() {
        inner class ViewHolder(private val holderBinding: StepRallyLocationItemBinding) :
            RecyclerView.ViewHolder(holderBinding.root) {
            /*init {
                holderBinding.holder = this
            }*/
            fun onBind(model: Location_four) {
              //  holderBinding.item = model
                holderBinding.location.text = model.location_name

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

            }

            fun deapClicl(model: Location_four) {
                holderBinding.dibsImv.isEnabled = false
                if (model.allSpotCount == model.myInterCount) {
                    setGlide(holderBinding.dibsImv, R.drawable.heart_resize)

                }
            }

            private fun setPopular(model: Location_four) {
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

            private fun setFavorite(model: Location_four) {
                if (model.allSpotCount == model.myInterCount) {
                    setGlide(holderBinding.dibsImv, R.drawable.full_heart_resize)
                } else {
                    setGlide(holderBinding.dibsImv, R.drawable.heart_resize)
                }
            }

            private val deapClick: View.OnClickListener =
                View.OnClickListener {

                }

            private fun deapClick() {

            }


            private fun setGlide(target: ImageView, img: Int) {
                Glide.with(context).load(img).into(target)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(adapterList[position])
        }

        override fun getItemCount(): Int = 4

    }


}