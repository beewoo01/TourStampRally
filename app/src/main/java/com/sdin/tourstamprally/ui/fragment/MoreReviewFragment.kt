package com.sdin.tourstamprally.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.More_Frag_LocationAdapter
import com.sdin.tourstamprally.adapter.review.MoreFragReviewAdapter

import com.sdin.tourstamprally.databinding.FragmentMoreReviewBinding
import com.sdin.tourstamprally.model.AllReviewDTO
import com.sdin.tourstamprally.model.AllReviewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MoreReviewFragment : BaseFragment() {

    private var binding: FragmentMoreReviewBinding? = null
    private var selectedLocationIdx = 0
    private var reviewAdapter: MoreFragReviewAdapter? = null
    private var locationAdapter: More_Frag_LocationAdapter? = null
    private val reviewList = ArrayList<AllReviewModel>()
    //private var listener : ItemOnClick? = null


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


        binding?.searchEdt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun afterTextChanged(s: Editable?) {
                search(binding?.searchEdt?.text.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        binding?.searchBtn?.setOnClickListener {
            search(binding?.searchEdt?.text.toString())
        }
    }

    private fun getData() {

        apiService.select_all_review().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<AllReviewModel>>() {
                override fun onSuccess(result: List<AllReviewModel>?) {
                    result?.let {


                        /*val arrayList = arrayListOf<AllReviewModel>()
                        arrayList.addAll(result)
                        reviewList.addAll(result)*/
                        initData(it)

                    }
                }

                override fun onError(e: Throwable?) {
                    e?.printStackTrace()
                }

            })

    }

    private fun initData(list: List<AllReviewModel>) {

        val map = HashMap<Int, String>()

        for (i in list) {
            map[i.location_idx] = i.location_name
        }


        val locationList: MutableList<Pair<Int, String>> = mutableListOf()
        for ((k, v) in map) {
            locationList.add(Pair(k, v))
        }

        val reviewList: MutableList<AllReviewModel> = mutableListOf()
        //reviewList.addAll(list)
        //reviewAdapter = ReviewMainReAdapter(requireContext(), reviewList)
        binding?.reviewRecyclerview?.apply {
            reviewAdapter = MoreFragReviewAdapter() { allReviewModel ->
                val action =
                    MoreReviewFragmentDirections.actionFragmentMoreReviewToFragmentReviewComents(
                        title = allReviewModel.touristspot_name,
                        state = 4,
                        reviewIdx = allReviewModel.review_idx
                    )

                findNavController().navigate(action)

            }.apply {
                submitList(list)
            }

            adapter = reviewAdapter

        }


        locationAdapter = More_Frag_LocationAdapter(locationList, requireContext())
        locationAdapter?.apply {

            locationAdapter?.setMoreLocationListener(object
                : More_Frag_LocationAdapter.MoreLocationItemListener {

                override fun onItemClick(item: Int) {
                    //val adList : MutableList<AllReviewDTO> = MutableList()
                    val paramList = mutableListOf<AllReviewModel>()

                    if (selectedLocationIdx == item) {

                        paramList.addAll(reviewList)

                        selectedLocationIdx = 0

                    } else {
                        selectedLocationIdx = item

                        for (i in reviewList) {
                            if (i.location_idx == item) {
                                paramList.add(i)
                            }

                        }
                    }

                    reviewAdapter?.submitList(paramList)
                    //reviewAdapter?.changeList(paramList)
                }

            })


            binding?.locationRecyclerview?.adapter = this
        }

    }

    private fun search(data: String) {
        val arrayList = mutableListOf<AllReviewModel>()

        for (i in reviewList) {
            if (data.isNotEmpty()) {

                if (i.location_name.lowercase(Locale.getDefault()).contains(data)) {

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
                }

            } else {
                if (selectedLocationIdx != 0 &&
                    reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx
                ) {

                    arrayList.add(i)
                } else if (selectedLocationIdx == 0) {
                    arrayList.add(i)
                }
            }

        }

        reviewAdapter?.submitList(arrayList)

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            MoreReviewFragment()
    }


}