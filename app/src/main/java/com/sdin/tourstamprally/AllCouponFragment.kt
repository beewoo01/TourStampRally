package com.sdin.tourstamprally

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.sdin.tourstamprally.databinding.FragmentAllCouponBinding
import com.sdin.tourstamprally.databinding.MycouponItemBinding
import com.sdin.tourstamprally.model.StoreAllCouponModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.recyclerveiew.CustomItemDecoration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapPoint
import java.util.*
import kotlin.collections.ArrayList

class AllCouponFragment : BaseFragment() {

    private var binding: FragmentAllCouponBinding? = null
    private var couponListState = 0 /*0 : 전체, 1 : 주변, 2 : 코스별*/
    private var couponListCategory = 3 /*0 : 식당 , 1 : 카페, 2 : 숙박시설, 3: 미선택*/
    private lateinit var allStoreCouponList: List<StoreAllCouponModel>
    private val categoryArr = arrayOf(R.id.restaurant_txv, R.id.cafe_txv, R.id.accommodation_txv)
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private var allCouponAdapter: AllCouponAdapter? = null
    private lateinit var locationArray : Array<String>

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
                    allStoreCouponList = result
                    setRecyclerView(result.toMutableList())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    private fun initView() {
        with(binding!!) {
            checkPermission()
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position: Int? = tab?.position
                    position?.let {
                        Log.wtf("tabLayout selected", "position? $position")
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
            allCouponAdapter = AllCouponAdapter().apply {
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

        /*locationArray = arrayOfNulls(10) {"", "", "", "" ,"" ,"", "", "", "",""}
        val map :
        for (model in list) {

        }*/


        binding?.spinnerLocation?.apply {
            adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    locationArray
                    )

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
                stateList.addAll(changeListToAround())
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


    class AllCouponAdapter : ListAdapter<StoreAllCouponModel, AllCouponAdapter.ViewHolder>(differ) {
        inner class ViewHolder(private val binding: MycouponItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun onBind(model: StoreAllCouponModel) {
                with(binding) {
                    if (model.store_logo_icon != null) {
                        Glide.with(logoImv.context)
                            .load("http://coratest.kr/imagefile/bsr/store_logo/" + model.store_logo_icon)
                            .placeholder(R.drawable.sample_profile_image)
                            .error(R.drawable.sample_profile_image)
                            .into(logoImv)
                    } else {
                        Glide.with(logoImv.context)
                            .load(R.drawable.sample_profile_image)
                            .into(logoImv)
                    }

                    couponName.text = model.store_coupon_name
                    startDataTxv.text = model.store_coupon_expiration_startDate
                    endDataTxv.text = model.store_coupon_expiration_endDate


                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                MycouponItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(currentList[position])
        }


        companion object {
            val differ = object : DiffUtil.ItemCallback<StoreAllCouponModel>() {
                override fun areItemsTheSame(
                    oldItem: StoreAllCouponModel,
                    newItem: StoreAllCouponModel
                ): Boolean {
                    return oldItem.store_coupon_idx == newItem.store_coupon_idx
                }

                override fun areContentsTheSame(
                    oldItem: StoreAllCouponModel,
                    newItem: StoreAllCouponModel
                ): Boolean {
                    return oldItem == newItem
                }

            }
        }
    }

}