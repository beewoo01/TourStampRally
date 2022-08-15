package com.sdin.tourstamprally.ui.fragment.tour

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.tour.direction.DirectionGuidAdapter
import com.sdin.tourstamprally.databinding.FragmentDirectionGuid2Binding
import com.sdin.tourstamprally.model.LocationJoinTouristSpot
import com.sdin.tourstamprally.model.TopFourLocationModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.listener.Observer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class DirectionGuid2Fragment : BaseFragment(), Observer {

    private var binding: FragmentDirectionGuid2Binding? = null

    //private val args: DirectionGuid2FragmentArgs by navArgs()
    private val paramList: MutableList<TopFourLocationModel> = mutableListOf()
    private val spotLocationMatchList = mutableListOf<LocationJoinTouristSpot>()
    private lateinit var directionGuidAdapter: DirectionGuidAdapter

    fun newInstance(paramList: MutableList<TopFourLocationModel>): DirectionGuid2Fragment {
        val args = Bundle()
        val paramStr = Gson().toJson(paramList)
        args.putString("paramList", paramStr)
        val fragment = DirectionGuid2Fragment()
        fragment.arguments = args
        return fragment
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_direction_guid2, container, false)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        //paramList = args.locationModelList.toMutableList()

        initView()
        getData()


    }

    private fun initView() {


        binding?.apply {
            val param = arguments?.getString("paramList", "")
            val list: MutableList<TopFourLocationModel> =
                Gson().fromJson(param, Array<TopFourLocationModel>::class.java)
                    .toMutableList()

            paramList.addAll(list.toMutableList())

            locationRe.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())

                directionGuidAdapter = DirectionGuidAdapter(requireContext()) { locationModel ->
                    val action =
                        DirectionGuidFragmentDirections.actionFragmentDirectionGuidToFragmentLocation(
                            locationModel.location_name,
                            locationModel
                        )


                    if (locationModel.touristspotpoint_createtime == null) {
                        Toast.makeText(requireContext(), "등록된 관광지가 없습니다.", Toast.LENGTH_SHORT)
                            .show()
                        return@DirectionGuidAdapter
                    }

                    findNavController().navigate(action)


                }.apply {
                    submitList(paramList)
                }

                adapter = directionGuidAdapter
            }
        }
    }

    private fun getData() {
        binding?.directionGuidPgb?.visibility = View.VISIBLE


        apiService.tourSpotLocationMatchSearch.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<LocationJoinTouristSpot>>() {
                override fun onSuccess(t: List<LocationJoinTouristSpot>) {
                    spotLocationMatchList.addAll(t.toMutableList())
                    binding?.directionGuidPgb?.visibility = View.GONE
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    binding?.directionGuidPgb?.visibility = View.GONE
                }


            })

        /*apiService.hashTag.enqueue(object : Callback<List<TourTagModelDC>> {
            override fun onResponse(
                call: Call<List<TourTagModelDC>>,
                response: Response<List<TourTagModelDC>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()

                    if (!result.isNullOrEmpty()) {
                        for (model in result) {
                            if (model != null) {
                                model.hashTag?.let {
                                    val arr: List<String> = it.split("#")
                                    val set = arr.toSet()


                                    for ((index, str) in set.withIndex()) {
                                        //Log.wtf("str $index", str)
                                        if (str.trim() != "") {
                                            hashTagList.add(TourTagModelDC(str, model.location_idx))
                                        }

                                    }

                                    *//*val strArr: Array<List<String>> = arrayOf(model.hashTag.split("#"))
                                    if (strArr != null) {

                                        for ((index, str) in strArr.withIndex()) {
                                            Log.wtf("srt $index", str[index])
                                            hashTagList.add(
                                                TourTagModelDC(
                                                    str[index],
                                                    model.location_idx
                                                )
                                            )
                                        }
                                    }*//*
                                }
                            }
                        }

                        setHashTag()
                    }
                }
            }

            override fun onFailure(call: Call<List<TourTagModelDC>>, t: Throwable) {
                t.printStackTrace()
            }

        })*/
    }

    private fun setHashTag() {
        if (context == null) {
            return
        }

        /*binding?.tagRe?.apply {
            adapter = DirectionGuidTagAdapter() { model ->
                model.hashTag?.let { search(it) }
            }.apply {
                submitList(hashTagList)
            }

            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        }*/

        binding?.directionGuidPgb?.visibility = View.GONE
    }


    private fun search(searchData: String) {
        val searchList = mutableListOf<TopFourLocationModel>()
        val locationIndexes: HashSet<Int> = hashSetOf()

        if (searchData.isEmpty()) {
            Log.wtf(DirectionGuidFragment::class.java.name, "searchData.isEmpty()")
            searchList.addAll(paramList)
            //directionGuidAdapter.submitList(searchList)

        } else {
            Log.wtf(DirectionGuidFragment::class.java.name, "search()!!")

            for (model in paramList) {
                if (model.location_name.contains(searchData)) {
                    locationIndexes.add(model.location_idx)
                }
            }
            for(idx in locationIndexes){
                for(model in paramList){
                    if(idx == model.location_idx){
                        searchList.add(model)
                    }
                }
            }
            /*for (tagModel in hashTagList) {
                if (tagModel.hashTag?.lowercase()?.contains(searchData) == true) {
                    locationIndexes.add(tagModel.location_idx)
                }
            }

            for (model in paramList) {
                if (model.location_name.lowercase() == searchData) {
                    locationIndexes.add(model.location_idx)
                }
            }

            for (idx in locationIndexes) {
                for (model in paramList) {
                    if (idx == model.location_idx) {
                        searchList.add(model)
                    }
                }
            }*/

        }

        directionGuidAdapter.submitList(searchList)
    }



    override fun update(search: String) {
        search(search)
        Log.wtf("DirectionGuid2Fragment", "search? $search")
    }


}