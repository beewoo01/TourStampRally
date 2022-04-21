package com.sdin.tourstamprally.ui.fragment.tour

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.tour.SelectLocationAdapter
import com.sdin.tourstamprally.adapter.tour.direction.DirectionGuidAdapter
import com.sdin.tourstamprally.adapter.tour.direction.DirectionGuidTagAdapter
import com.sdin.tourstamprally.databinding.FragmentDirectionGuidBinding
import com.sdin.tourstamprally.model.TopFourLocationModel
import com.sdin.tourstamprally.model.TourTagModelDC
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashSet

class DirectionGuidFragment : BaseFragment() {

    private var binding: FragmentDirectionGuidBinding? = null
    private val args: DirectionGuidFragmentArgs by navArgs()
    private lateinit var paramList: MutableList<TopFourLocationModel>
    private val hashTagList = mutableListOf<TourTagModelDC>()
    private lateinit var directionGuidAdapter: DirectionGuidAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_direction_guid, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        paramList = args.locationModelList.toMutableList()

        initView()
        getData()


    }

    private fun initView() {


        binding?.apply {

            searchEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    Unit

                override fun afterTextChanged(s: Editable?) {
                    search(searchData = searchEdt.text.toString())
                }

            })



            searchBtn.setOnClickListener {
                search(searchData = searchEdt.text.toString())
            }

            locationRe.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())

                directionGuidAdapter = DirectionGuidAdapter(requireContext()) { locationModel ->
                    val action =
                        DirectionGuidFragmentDirections.actionFragmentDirectionGuidToFragmentLocation(
                            locationModel.location_name + " 랠리 맵",
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

        apiService.hashTag.enqueue(object : Callback<List<TourTagModelDC>> {
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
                                        Log.wtf("str $index", str)
                                        if (str.trim() != "") {
                                            hashTagList.add(TourTagModelDC(str, model.location_idx))
                                        }

                                    }

                                    /*val strArr: Array<List<String>> = arrayOf(model.hashTag.split("#"))
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
                                    }*/
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

        })
    }

    private fun setHashTag() {
        if (context == null) {
            return
        }

        binding?.tagRe?.apply {
            adapter = DirectionGuidTagAdapter() { model ->
                model.hashTag?.let { search(it) }
            }.apply {
                submitList(hashTagList)
            }

            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        }

        binding?.directionGuidPgb?.visibility = View.GONE
    }

    private val selectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (paramList != null) {
                var searchData = parent?.selectedItem.toString()
                if (searchData == "전체") {
                    searchData = ""
                }

                search(searchData)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    }

    private fun search(searchData: String) {
        val searchList = mutableListOf<TopFourLocationModel>()
        val locationIndexes: HashSet<Int> = hashSetOf()

        if (searchData.isEmpty()) {
            searchList.addAll(paramList)
            //directionGuidAdapter.submitList(searchList)

        } else {

            for (tagModel in hashTagList) {
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
            }

        }

        directionGuidAdapter.submitList(searchList)
    }

}