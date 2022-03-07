package com.sdin.tourstamprally.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.More_Frag_LocationAdapter
import com.sdin.tourstamprally.adapter.swipe.MoreFragReviewAdapter
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
    var reviewAdapter: MoreFragReviewAdapter? = null
    var locationAdapter: More_Frag_LocationAdapter? = null
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
                override fun onSuccess(t: List<AllReviewModel>?) {
                    t?.let {
                        val arrayList = arrayListOf<AllReviewModel>()
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

    private fun initData(list: List<AllReviewModel>) {

        val map = HashMap<Int, String>()

        for (i in list) {
            map[i.location_idx] = i.location_name
        }


        val locationList: MutableList<Pair<Int, String>> = mutableListOf()
        for ((k, v) in map) {
            locationList.add(Pair(k, v))
        }

        val reviewList: ArrayList<AllReviewDTO> = ArrayList()
        //reviewList.addAll(list)
        reviewAdapter = MoreFragReviewAdapter(requireContext(), reviewList)
        reviewAdapter?.apply {
            setListener(object : MoreFragReviewAdapter.MoreReviewListener {
                override fun onItemClick(data: AllReviewDTO) {
                    /*listener = requireActivity() as MainActivity
                    (listener as MainActivity).reviewItemClick(data.review_idx, data.touristspot_name)*/
                    findNavController().navigate(
                        R.id.action_fragment_more_review_to_fragment_review_coments,
                        Bundle().apply {
                            putInt("review_idx", data.review_idx)
                            putString("title", data.touristspot_name)
                            putInt("state", 4)
                        }
                    )
                }
            })
            binding?.reviewRecyclerview?.adapter = this
        }


        locationAdapter = More_Frag_LocationAdapter(locationList, requireContext())
        locationAdapter?.apply {

            locationAdapter?.setMoreLocationListener(object
                : More_Frag_LocationAdapter.MoreLocationItemListener {

                override fun onItemClick(item: Int) {
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
        /*val arrayList = ArrayList<AllReviewDTO>()

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

        reviewAdapter?.changeList(arrayList)
*/
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            MoreReviewFragment()
    }


}