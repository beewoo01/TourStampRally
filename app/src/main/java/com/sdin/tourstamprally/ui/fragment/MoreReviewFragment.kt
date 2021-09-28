package com.sdin.tourstamprally.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.More_Frag_LocationAdapter
import com.sdin.tourstamprally.adapter.swipe.More_Frag_ReviewAdapter
import com.sdin.tourstamprally.databinding.FragmentMoreReviewBinding
import com.sdin.tourstamprally.model.AllReviewDTO
import com.sdin.tourstamprally.ui.activity.MainActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import com.sdin.tourstamprally.utill.ItemOnClick

import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MoreReviewFragment : BaseFragment() {

    private var binding: FragmentMoreReviewBinding? = null
    private var selectedLocationIdx = 0
    var reviewAdapter: More_Frag_ReviewAdapter? = null
    var locationAdapter: More_Frag_LocationAdapter? = null
    private var textint = 0
    private val reviewList = ArrayList<AllReviewDTO>()
    private var listener : ItemOnClick? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                search(binding?.searchEdt?.text.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        binding?.searchBtn?.setOnClickListener {

            search(binding?.searchEdt?.text.toString())
        }
    }

    private fun getData() {

        apiService.select_all_review().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<AllReviewDTO>>() {
                    override fun onSuccess(t: List<AllReviewDTO>?) {
                        t?.let {
                            val arrayList = arrayListOf<AllReviewDTO>()
                            arrayList.addAll(t)
                            reviewList.addAll(t)
                            initData(arrayList)

                        }
                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                })

    }

    private fun initData(list: List<AllReviewDTO>) {

        val map = HashMap<Int, String>()

        for (i in list) {
            Log.wtf("touristspot_name", i.touristspot_name)
            map[i.location_idx] = i.location_name
        }


        val locationList: MutableList<Pair<Int, String>> = mutableListOf()
        for ((k, v) in map) {
            locationList.add(Pair(k, v))
        }

        val reviewList: ArrayList<AllReviewDTO> = ArrayList()
        reviewList.addAll(list)
        reviewAdapter = More_Frag_ReviewAdapter(requireContext(), reviewList)
        reviewAdapter?.apply {
            setListener(object : More_Frag_ReviewAdapter.MoreReviewListener {
                override fun onItemClick(data: AllReviewDTO) {
                    listener = requireActivity() as MainActivity
                    (listener as MainActivity).reviewItemClick(data.review_idx, data.touristspot_name)
                }
            })
            binding?.reviewRecyclerview?.adapter = this
        }


        locationAdapter = More_Frag_LocationAdapter(locationList, requireContext())
        locationAdapter?.apply {

            locationAdapter?.setMoreLocationListener(object
                : More_Frag_LocationAdapter.MoreLocationItemListener {

                override fun onItemClick(item: Int) {
                    Log.wtf("locationIDX??", item.toString())
                    //val adList : MutableList<AllReviewDTO> = MutableList()
                    val paramList = ArrayList<AllReviewDTO>()

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

                    reviewAdapter?.changeList(paramList)
                }

            })


            binding?.locationRecyclerview?.adapter = this
        }

    }

    private fun search(data: String) {
        val arrayList = ArrayList<AllReviewDTO>()

            for (i in reviewList) {
                if (data.isNotEmpty()) {
                    if (i.location_name.toLowerCase(Locale.ROOT).contains(data)) {

                        if (selectedLocationIdx != 0 &&
                                reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx) {
                            arrayList.add(i)
                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }

                    }

                    if (i.touristspot_name.toLowerCase(Locale.ROOT).contains(data)) {
                        Log.wtf("search", "touristspot_name")

                        if (selectedLocationIdx != 0 &&
                                reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx) {
                            arrayList.add(i)

                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }
                    }

                    if (i.review_contents.toLowerCase(Locale.ROOT).contains(data)) {
                        Log.wtf("search", "review_contents")

                        if (selectedLocationIdx != 0 &&
                                reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx) {
                            arrayList.add(i)
                        } else if (selectedLocationIdx == 0) {
                            arrayList.add(i)
                        }
                    }

                }else {
                    if (selectedLocationIdx != 0 &&
                            reviewList[reviewList.indexOf(i)].location_idx == selectedLocationIdx){

                        arrayList.add(i)
                    }else if (selectedLocationIdx == 0){
                        arrayList.add(i)
                    }
                }

            }

        reviewAdapter?.changeList(arrayList)

    }


    companion object {

        @JvmStatic
        fun newInstance() =
                MoreReviewFragment()
    }


}