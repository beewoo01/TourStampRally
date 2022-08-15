package com.sdin.tourstamprally.ui.fragment.report.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.MoreFragLocationAdapter
import com.sdin.tourstamprally.adapter.review.MoreFragReviewAdapter
import com.sdin.tourstamprally.databinding.FragmentMoreReviewBinding
import com.sdin.tourstamprally.model.AllReviewModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.ui.fragment.tour.DirectionGuidFragmentDirections
import com.sdin.tourstamprally.utill.GridSpacingItemDecoration
import com.sdin.tourstamprally.utill.listener.Observer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


class MoreReviewFragment : BaseFragment(), Observer {

    private var binding: FragmentMoreReviewBinding? = null
    private var selectedLocationIdx = 0
    private var reviewAdapter: MoreFragReviewAdapter? = null
    private var locationAdapter: MoreFragLocationAdapter? = null
    private val reviewList: MutableList<AllReviewModel> = mutableListOf()
    //private var listener : ItemOnClick? = null

    var locationidx = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_review, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()


        /*binding?.searchEdt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun afterTextChanged(s: Editable?) {
                search(binding?.searchEdt?.text.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        binding?.searchBtn?.setOnClickListener {
            search(binding?.searchEdt?.text.toString())
        }*/
    }

    private fun getData() {

        apiService.select_all_review().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<AllReviewModel>>() {
                override fun onSuccess(result: List<AllReviewModel>?) {
                    result?.let {
                        reviewList.addAll(result.toMutableList())
                        initData()

                    }
                }

                override fun onError(e: Throwable?) {
                    e?.printStackTrace()
                }

            })

    }

    private fun initData() {

        val map = HashMap<Int, String>()

        for (i in reviewList) {
            map[i.location_idx] = i.location_name
        }


        val locationList: MutableList<Pair<Int, String>> = mutableListOf()
        for ((k, v) in map) {
            locationList.add(Pair(k, v))
        }
        //search
        /*val childobserve = DirectionGuidFragment.Observerparent()
        childobserve.parentObserver.add{newvalue ->

        }*/

        binding?.reviewRecyclerview?.apply {
            reviewAdapter = MoreFragReviewAdapter { allReviewModel ->
                /*Log.wtf("allReviewModel.touristspot_name", allReviewModel.touristspot_name)
                Log.wtf("allReviewModel.review_idx", allReviewModel.review_idx.toString())
                Log.wtf("allReviewModel.user_idx", allReviewModel.user_idx.toString())*/

                val action = DirectionGuidFragmentDirections.actionFragmentDirectionGuidToFragmentReviewComents(
                    title = allReviewModel.touristspot_name,
                    state = 4,
                    reviewIdx = allReviewModel.review_idx,
                    reviewUserIdx = allReviewModel.user_idx
                )

                findNavController().navigate(action)
                /*val action =
                    MoreReviewFragmentDirections.actionFragmentMoreReviewToFragmentReviewComents(
                        title = allReviewModel.touristspot_name,
                        state = 4,
                        reviewIdx = allReviewModel.review_idx,
                        reviewUserIdx = allReviewModel.user_idx
                    )

                findNavController().navigate(action)*/

            }.apply {
                submitList(reviewList)
                addItemDecoration(GridSpacingItemDecoration(2, 20, false))
            }

            adapter = reviewAdapter

        }


        locationAdapter = MoreFragLocationAdapter(locationList, requireContext())
        locationAdapter?.apply {
            locationAdapter?.setMoreLocationListener(object
                : MoreFragLocationAdapter.MoreLocationItemListener {

                override fun onItemClick(item: Int) {
                    //val adList : MutableList<AllReviewDTO> = MutableList()
                    val paramList = mutableListOf<AllReviewModel>()

                    if (selectedLocationIdx == item) {
                        paramList.addAll(reviewList)
                        selectedLocationIdx = 0

                    } else {
                        selectedLocationIdx = item
                        Log.wtf("selectedLocationIdx",selectedLocationIdx.toString())

                        for (i in reviewList) {
                            if (i.location_idx == item) {
                                paramList.add(i)
                                Log.wtf("reviewListlocation_idx",i.location_idx.toString())
                            }

                        }

                    }

                    reviewAdapter?.submitList(paramList)
                }

            })


            binding?.locationRecyclerview?.adapter = this
        }

    }

    private fun search(data: String?) {
        val arrayList = mutableListOf<AllReviewModel>()

        if (data.isNullOrEmpty()) {
            arrayList.addAll(reviewList)
        } else {
            for (i in reviewList) {
                if (data.isNotEmpty()) {
                    if (i.location_name.contains(data)){
                        if (selectedLocationIdx != 0 &&
                            reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx
                        ) {
                            arrayList.add(i)
                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }
                    }
                    if (i.review_contents.lowercase(Locale.getDefault()).contains(data)) {

                        if (selectedLocationIdx != 0 &&
                            reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx
                        ) {
                            arrayList.add(i)
                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }
                    }

                    /*if (i.location_name.lowercase(Locale.getDefault()).contains(data)) {

                        if (selectedLocationIdx != 0 &&
                            reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx
                        ) {
                            arrayList.add(i)
                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }

                    }

                    if (i.touristspot_name.lowercase(Locale.getDefault()).contains(data)) {

                        if (selectedLocationIdx != 0 &&
                            reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx
                        ) {
                            arrayList.add(i)

                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }
                    }

                    if (i.review_contents.lowercase(Locale.getDefault()).contains(data)) {

                        if (selectedLocationIdx != 0 &&
                            reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx
                        ) {
                            arrayList.add(i)
                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }
                    }*/

                }
            }
        }

        reviewAdapter?.submitList(arrayList)

    }

    override fun update(search: String) {
        search(search)
        Log.wtf("MoreReviewFragment", "search? $search")
    }


}