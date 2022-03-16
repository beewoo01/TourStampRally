package com.sdin.tourstamprally

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.sdin.tourstamprally.adapter.spinner.AllCouponLocationSpinnerAdapter
import com.sdin.tourstamprally.adapter.spinner.AllCouponSpotSpinnerAdapter
import com.sdin.tourstamprally.adapter.storecoupon.AllCouponAdapter
import com.sdin.tourstamprally.databinding.FragmentAllCouponBinding
import com.sdin.tourstamprally.model.LocationJoinTouristSpot
import com.sdin.tourstamprally.model.StoreAllCouponModel
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.recyclerveiew.CustomItemDecoration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.collections.HashMap

class AllCouponFragment : BaseFragment() {

    private var binding: FragmentAllCouponBinding? = null
    private var couponListState = 0 /*0 : 전체, 1 : 주변, 2 : 코스별*/
    private var couponListCategory = 3 /*0 : 식당 , 1 : 카페, 2 : 숙박시설, 3: 미선택*/
    private lateinit var allStoreCouponList: MutableList<StoreAllCouponModel>
    private val categoryArr = arrayOf(R.id.restaurant_txv, R.id.cafe_txv, R.id.accommodation_txv)
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private var allCouponAdapter: AllCouponAdapter? = null
    private val allSpinnerList: MutableList<Triple<Int, String, MutableList<Pair<Int, String>>?>> by lazy {
        mutableListOf()
    }
    private lateinit var locationList: MutableList<Pair<Int, String>>
    private var selectedTouristSpotIdx: Int = 0

    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllCouponBinding.inflate(inflater, container, false)
        binding?.fragment = this@AllCouponFragment
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        initView()
        getData()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        fusedLocationClient = null
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun getData() {
        apiService.selectAllStoreCoupon()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<StoreAllCouponModel>>() {
                override fun onSuccess(result: List<StoreAllCouponModel>) {
                    allStoreCouponList = result.toMutableList()
                    setRecyclerView(result.toMutableList())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    private fun getLocationJoinTouristSpot() {
        apiService.selectLocationJoinTouristSpot()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<LocationJoinTouristSpot>>() {
                override fun onSuccess(result: List<LocationJoinTouristSpot>) {
                    initSpinner(result.toMutableList())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun initSpinner(list: MutableList<LocationJoinTouristSpot>) {


        val locationMap: MutableMap<Int, String> = HashMap()
        val touristSpotMap: MutableMap<Int, String> = HashMap()
        for (model in list) {
            locationMap[model.location_idx] = model.location_name
        }
        locationList = locationMap.toList().toMutableList()
        for ((index, locationModel) in locationList.withIndex()) {
            allSpinnerList.add(
                Triple(
                    locationModel.first,
                    locationModel.second,
                    mutableListOf()
                )
            )

            for (model in list) {
                if (allSpinnerList[index].first == model.location_idx) {
                    allSpinnerList[index].third
                        ?.add(
                            Pair(
                                model.touristspot_idx, model.touristspot_name
                            )
                        )
                }

            }
        }

        allSpinnerList.add(Triple(0, "구 선택", null))
        //touristspotList.add(Pair(0, "코스 선택"))

        binding?.spinnerLocation?.apply {
            adapter = AllCouponLocationSpinnerAdapter(requireContext(), allSpinnerList)
            onItemSelectedListener = onLocationSpItemSelectedListener
            setSelection(adapter.count)
        }

        binding?.spinnerCourse?.apply {
            val emptyList = mutableListOf<Pair<Int, String>>()
            emptyList.add(Pair(0, ""))
            emptyList.add(Pair(0, "코스 선택"))
            adapter =
                AllCouponSpotSpinnerAdapter(requireContext(), emptyList)
            setSelection(adapter.count)
        }


    }

    private fun setTouristSpotSpinner(list: MutableList<Pair<Int, String>>?) {
        if (list != null) {
            val subList = mutableListOf<Pair<Int, String>>()
            subList.addAll(list)
            subList.add(Pair(0, "코스선택"))
            binding?.spinnerCourse?.apply {

                adapter = AllCouponSpotSpinnerAdapter(requireContext(), subList)
                setSelection(adapter.count)
                onItemSelectedListener = onTourSpItemSelectedListener
            }
        }

    }

    private val onTourSpItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                selectedTouristSpotIdx =
                    (parent?.getItemAtPosition(position) as Pair<Int, String>).first

                changeList()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        }

    private val onLocationSpItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setTouristSpotSpinner(
                    (parent?.getItemAtPosition(position) as Triple<*, *, MutableList<Pair<Int, String>>>).third
                )

            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        }

    private fun initView() {
        with(binding!!) {
            checkPermission()
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position: Int? = tab?.position
                    position?.let {
                        couponListState = it
                        changeList()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                override fun onTabReselected(tab: TabLayout.Tab?) = Unit

            })
        }
    }

    private fun checkPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient?.apply {
            lastLocation.addOnSuccessListener { location: Location? ->
                currentLatitude = location?.latitude
                currentLongitude = location?.longitude
            }

            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (20 * 1000).toLong()

            requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    currentLongitude = location.longitude
                    currentLatitude = location.latitude
                }
            }
        }
    }

    private fun setRecyclerView(list: MutableList<StoreAllCouponModel>) {
        binding?.couponRe?.apply {
            allCouponAdapter = AllCouponAdapter { responseModel ->
                DefaultBSRDialog(requireContext(),
                    title = "쿠폰 발급은 계정당 하나만 \n가능합니다.",
                    content = "쿠폰을 발급 받을시 다른 쿠폰은 받을수 없습니다.\n 발급받으시겠습니까?",
                    isSpecial = false,
                    isSwitchBtn = false,
                    leftBtnStr = "받기",
                    rightBtnStr = "취소",

                    callback = {
                        if (it) {
                            apiService.getCouponFromFree(
                                responseModel.store_coupon_idx,
                                Utils.User_Idx
                            )
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object : DisposableSingleObserver<Int>() {
                                    override fun onSuccess(result: Int) {
                                        if (result == 0) {
                                            showToast("쿠폰 발급에 실패하였습니다.")
                                        } else {

                                            showToast("쿠폰 발급에 성공하였습니다.")
                                            successGetStoreCoupon(responseModel)
                                        }
                                    }

                                    override fun onError(e: Throwable) {
                                        e.printStackTrace()
                                    }

                                })
                        }
                    }).show()
            }.apply {
                submitList(list)
            }
            adapter = allCouponAdapter
            addItemDecoration(
                CustomItemDecoration(
                    topPadding = 20, bottomPadding = 20, null, null
                )
            )

            layoutManager = LinearLayoutManager(requireContext())
        }

        getLocationJoinTouristSpot()


    }

    private fun successGetStoreCoupon(model: StoreAllCouponModel) {
        allStoreCouponList.remove(model)
        if (allCouponAdapter == null) {
        } else {
            allCouponAdapter?.let {
                val newList = it.currentList.toMutableList()
                newList.remove(model)
                it.submitList(newList)
            }

        }

    }

    private fun changeList() {
        val stateList = mutableListOf<StoreAllCouponModel>()

        when (couponListState) {
            0 -> {
                binding?.spinnerGroup?.visibility = View.GONE
                stateList.addAll(allStoreCouponList)
            }

            1 -> {
                binding?.spinnerGroup?.visibility = View.GONE
                stateList.addAll(changeListToAround())
            }

            2 -> {
                binding?.spinnerGroup?.visibility = View.VISIBLE
                stateList.addAll(setCourse(selectedTouristSpotIdx))
            }
        }

        val deepCopyList = mutableListOf<StoreAllCouponModel>()
        if (couponListCategory == 3) {
            deepCopyList.addAll(stateList)
        } else {
            for (couponModel in stateList) {
                if (couponModel.store_type == couponListCategory) {
                    deepCopyList.add(couponModel)
                }
            }
        }


        allCouponAdapter?.submitList(deepCopyList)
        binding?.allCountTxv?.text = deepCopyList.size.toString()
    }

    private fun setCourse(touristSpotIdx: Int): MutableList<StoreAllCouponModel> {
        val subList = mutableListOf<StoreAllCouponModel>()
        if (touristSpotIdx > 0) {
            for (model in allStoreCouponList) {
                if (model.store_touristspot_idx == touristSpotIdx) {
                    subList.add(model)
                }
            }
        } else {
            subList.addAll(allStoreCouponList)
        }

        return subList
    }

    fun setCategory(view: View) {
        val tag = view.tag.toString().toInt()
        couponListCategory = if (couponListCategory == tag) {
            3
        } else {
            tag
        }

        for (txv in categoryArr) {
            if (view.id == txv && couponListCategory < 3) {
                view.background =
                    ContextCompat.getDrawable(view.context, R.drawable.bg_rounded_category_selected)

                (view as TextView).setTextColor(ContextCompat.getColor(view.context, R.color.white))
            } else {
                binding?.root?.findViewById<TextView>(txv)?.let {
                    it.background =
                        ContextCompat.getDrawable(view.context, R.drawable.bg_rounded_category)
                    it.setTextColor(ContextCompat.getColor(view.context, R.color.mainColor))
                }
            }
        }

        changeList()

    }

    private fun changeListToAround(): MutableList<StoreAllCouponModel> {
        val myLocation = Location("")
        val allList = allStoreCouponList.toMutableList()

        if (currentLatitude != null && currentLongitude != null) {
            myLocation.latitude = currentLatitude!!
            myLocation.longitude = currentLongitude!!


            val removeItems = arrayListOf<StoreAllCouponModel>()
            allList.forEach { model ->
                val location = Location("")
                location.latitude = model.store_latitude
                location.longitude = model.store_longitude
                val distance = myLocation.distanceTo(location)
                if (distance > 300) {
                    //30M 초과
                    removeItems.add(model)
                }

            }
            removeItems.forEach { model ->
                allList.remove(model)
            }
        } else {
            showToast("현재 위치를 받아올 수 없습니다.")
        }

        return allList

    }


    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    class LocationSpinnerAdapter(
        val context: Context,
        val list: MutableList<Pair<Int, String>>?
    ) : BaseAdapter() {


        override fun getCount(): Int {
            return if (list != null) {
                list.size - 1
            } else {
                0
            }
        }

        override fun getItem(position: Int): Any {
            return list?.get(position) ?: 0
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view: View? = convertView
            if (view == null) {
                view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.store_coupon_spinner_layout, parent, false)
            }

            if (list != null) {
                if (position == count) {
                    view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                        text = ""
                        hint = list[count].second
                    }
                } else {
                    val text: String = list[position].second

                    view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                        this.text = text
                        parent?.context?.let {
                            this.setTextColor(ContextCompat.getColor(it, R.color.black))
                        }

                    }
                }

            }

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view: View? = convertView

            if (view == null) {
                view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.store_coupon_spinner_layout, parent, false)
            }

            if (list != null) {
                if (position == count) {
                    view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                        text = ""
                        hint = list[count].second
                    }

                } else {
                    val text: String = list[position].second
                    view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                        this.text = text
                        parent?.context?.let {
                            this.setTextColor(ContextCompat.getColor(it, R.color.black))
                        }

                    }
                }

            }
            return view
        }

    }

}