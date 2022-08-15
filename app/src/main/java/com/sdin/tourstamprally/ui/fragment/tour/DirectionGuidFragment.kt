package com.sdin.tourstamprally.ui.fragment.tour

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.FragmentDirectionGuidBinding
import com.sdin.tourstamprally.model.TopFourLocationModel
import com.sdin.tourstamprally.model.TourTagModelDC
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.ui.fragment.report.review.MoreReviewFragment
import com.sdin.tourstamprally.utill.listener.Observer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DirectionGuidFragment : BaseFragment(), com.sdin.tourstamprally.utill.listener.Observable {

    private var binding: FragmentDirectionGuidBinding? = null
    private val args: DirectionGuidFragmentArgs by navArgs()
    private lateinit var paramList: MutableList<TopFourLocationModel>
    private val hashTagList = mutableListOf<TourTagModelDC>()
    //private lateinit var directionGuidAdapter: DirectionGuidAdapter

    private val observerList = arrayListOf<Observer>()
    private val guidFragment by lazy {
        DirectionGuid2Fragment().newInstance(paramList)
        //DirectionGuid2Fragment.newInstance(paramList)
    }

    private val moreReviewFrag by lazy {
        MoreReviewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_direction_guid, container, false)

        binding?.fragment = this

        binding?.btnGetstamp?.setOnClickListener {
            val action = DirectionGuidFragmentDirections.actionFragmentDirectionGuidToPageStamp()
            findNavController().navigate(action)
        }




        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        paramList = args.locationModelList.toMutableList()

        initView()
        getData()


    }
    inner class ViewPagerAdapter(private val list : MutableList<Fragment>)
        : FragmentStateAdapter(requireActivity()) {

        override fun getItemCount(): Int {
            return list.count()
        }

        override fun createFragment(position: Int): Fragment {
            return list[position]
        }

    }

    private fun initView() {


        binding?.apply {

            //viewpager
            val fragmentList = mutableListOf<Fragment>()
            //val guidFragment = DirectionGuid2Fragment.newInstance(paramList)
            fragmentList.add(guidFragment)
            fragmentList.add(moreReviewFrag)
            registerObserver(guidFragment)
            registerObserver(moreReviewFrag)


            viewpagerguide.adapter = ViewPagerAdapter(fragmentList)
            TabLayoutMediator(binding?.tabLayout!!, binding?.viewpagerguide!!){ tab, position ->
                when(position){
                    0 -> tab.text = "투어"
                    1 -> tab.text = "리뷰"
                }

            }.attach()

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

            /*locationRe.apply {

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
            }*/
        }
    }

    private fun getData() {
//        binding?.directionGuidPgb?.visibility = View.VISIBLE

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
                                        //Log.wtf("str $index", str)
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

                        //setHashTag()
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

        /*binding?.tagRe?.apply {
            adapter = DirectionGuidTagAdapter() { model ->
                model.hashTag?.let { search(it) }
            }.apply {
                submitList(hashTagList)
            }

            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        }

        binding?.directionGuidPgb?.visibility = View.GONE*/
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

    /*class Observerparent{
        val parentObserver = mutableListOf<(String)->Unit>()
        var observerdata : String by Delegates.observable(""){property, oldValue, newValue ->
            parentObserver.forEach { it(newValue) }

        }
    }*/

    private fun search(searchData: String) {
        val searchList = mutableListOf<TopFourLocationModel>()

        if (searchData.isEmpty()) {
            searchList.addAll(paramList)

        } else {

            notifyObservers(searchData)
        }

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            MoreReviewFragment()
    }

    override fun registerObserver(observer: Observer) {
        observerList.add(observer)
    }

    override fun unregisterObserver(observer: Observer) {
        observerList.remove(observer)
    }

    override fun notifyObservers(search: String) {
        observerList.forEach {
            it.update(search)

        }
    }


}