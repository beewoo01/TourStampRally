package com.sdin.tourstamprally.ui.fragment.store

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.StoreReAdapter
import com.sdin.tourstamprally.databinding.FragmentSelectGuidStoreBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.CurrentCourseSingleton.UserCurrentCourseSpotIdx
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.*

class SelectGuidStoreFragment : BaseFragment() {

    private var binding: FragmentSelectGuidStoreBinding? = null

    private lateinit var mapView: MapView

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var storeReAdapter: StoreReAdapter? = null

    private lateinit var allStoreList: List<StoreModel>
    private val categoryArr = arrayOf(R.id.restaurant_txv, R.id.cafe_txv, R.id.accommodation_txv)
    private var storeListState = 0 /*0 : 전체, 1 : 주변, 2 : 코스별*/
    private var storeListCategory = 3 /*0 : 식당 , 1 : 카페, 2 : 숙박시설, 3: 미선택*/


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_guid_store, container, false)

        binding?.fragment = this@SelectGuidStoreFragment

        return binding?.root
    }

    override fun onResume() {
        super.onResume()

        initView()
        getData()
    }


    private fun initView() {

        binding?.apply {
            mapView = MapView(requireActivity())
            mapLayout.addView(mapView, 0)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            if (!checkLocationService()) {
                showDialogForLocationServiceSetting()
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position: Int? = tab?.position
                    position?.let {
                        storeListState = it
                        changeList()
                    }

                    Log.wtf(
                        this@SelectGuidStoreFragment.javaClass.name,
                        "tab?.position?? ${tab?.position}"
                    )

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                override fun onTabReselected(tab: TabLayout.Tab?) = Unit

            })

        }
    }

    /*private fun changeListToAll() { *//*전체*//*
        storeReAdapter?.submitList(allStoreList)
        mapView.removeAllPOIItems()
        addMapMarkers(allStoreList.toMutableList())
    }*/

    private fun changeListToAround(): MutableList<StoreModel> { /*주변*/
        val myLocation = Location("")
        myLocation.latitude = mapView.mapCenterPoint.mapPointGeoCoord.latitude
        myLocation.longitude = mapView.mapCenterPoint.mapPointGeoCoord.longitude
        val currentList = allStoreList.toMutableList()
        val removeItems = arrayListOf<StoreModel>()
        currentList.run {
            var i1 = 0
            var i2 = 0
            forEach { model ->
                val location = Location("")
                location.latitude = model.store_latitude.toDouble()
                location.longitude = model.store_longitude.toDouble()
                val distance = myLocation.distanceTo(location)

                if (distance > 300) {
                    //30M 초과
                    i1++
                    removeItems.add(model)
                }
            }

            removeItems.forEach { model ->
                i2++
                currentList.remove(model)
            }

            /*storeReAdapter?.submitList(this@run)
            mapView.removeAllPOIItems()
            addMapMarkers(this@run)*/
        }

        return currentList
    }

    private fun changeListToCourse(): MutableList<StoreModel> { /*코스별*/
        Log.wtf("changeListToCourse", "changeListToCourse")
        val courseList = mutableListOf<StoreModel>()
        when {
            UserCurrentCourseSpotIdx != null -> {
                allStoreList.forEach { model ->

                    if (model.store_touristspot_idx == UserCurrentCourseSpotIdx) {
                        Log.wtf("changeListToCourse", "UserCurrentCourseSpotIdx")
                        courseList.add(model)
                    }
                }
            }
            UserCurrentCourseSpotIdx == null -> {
                Toast.makeText(requireContext(), "선택된 코스가 없습니다.", Toast.LENGTH_SHORT).show()
            }
            courseList.size <= 0 -> {
                Toast.makeText(requireContext(), "현재 코스는 등록된 매장이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        return courseList
        /*storeReAdapter?.submitList(courseList)
        mapView.removeAllPOIItems()
        addMapMarkers(courseList)*/
    }

    private fun changeList() {
        Log.wtf("test", "test")
        val stateList = mutableListOf<StoreModel>()

        when (storeListState) {
            0 -> {/*전체*/
                stateList.addAll(allStoreList)

            }

            1 -> {/*주변*/
                stateList.addAll(changeListToAround())

            }

            2 -> {/*코스별*/
                stateList.addAll(changeListToCourse())

            }
        }

        val deepCopyList = mutableListOf<StoreModel>()
        if (storeListCategory == 3) {
            deepCopyList.addAll(stateList)

        } else {
            for (storeModel in stateList) {
                if (storeModel.store_type == storeListCategory) {
                    deepCopyList.add(storeModel)
                }
            }
        }


        storeReAdapter?.submitList(deepCopyList)
        changeMapMarkers(deepCopyList)

    }


    fun setCategory(view: View) {
        val tag = view.tag.toString().toInt()
        storeListCategory = if (storeListCategory == tag) {
            3
        } else {
            tag
        }

        for (txv in categoryArr) {
            if (view.id == txv && storeListCategory < 3) {
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

    private fun setCategoryList() {
        val subList = mutableListOf<StoreModel>()
        val currentList = storeReAdapter?.currentList
        if (currentList != null) {
            for (storeModel in currentList) {
                if (storeListCategory == storeModel.store_type) {
                    subList.add(storeModel)
                }
            }
        }
        storeReAdapter?.submitList(subList)
    }


    private fun permissionDialog() {
        AlertDialog.Builder(requireContext())
            .apply {
                setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")
                val listener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            backgroundPermission()
                        }
                    }
                }

                setPositiveButton("네", listener)
                setNegativeButton("아니오", null)
            }.show()
    }

    private fun showDialogForLocationServiceSetting() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("위치 서비스 비활성화")
            setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.")
            setCancelable(true)
            setPositiveButton(
                "설정"
            ) { _, _ ->


                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                locationRequest.launch(intent)
            }
        }.create().show()
    }

    private val locationRequest: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->

    }

    private fun startTracking() {
        mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
    }

    private fun checkLocationService(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onPause() {
        super.onPause()
        mapView.removeAllPOIItems()
        binding?.mapLayout?.removeView(mapView)
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        fusedLocationClient = null

    }

    private fun getData() {
        apiService.selectAllStore().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<StoreModel>>() {
                override fun onSuccess(list: List<StoreModel>) {
                    allStoreList = list

                    initItem(allStoreList.toMutableList())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun permissionCheck2() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            return
        }

        fusedLocationClient?.apply {
            lastLocation.addOnSuccessListener { location ->

                location?.let {

                    mapView.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            location.latitude,
                            location.longitude
                        ), true
                    )

                }
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
                    mapView.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            location.latitude,
                            location.longitude
                        ), true
                    )
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.all { it.value }) {
                permissionCheck2()
            }
        }


    private fun backgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), 2
            )
        } else {
            return
        }
    }

    private fun initItem(list: MutableList<StoreModel>) {
        binding?.run {

            bottomSheetIncl.storeRe.apply {

                addItemDecoration(DividerItemDecoration(requireContext(), 1))
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }

                storeReAdapter = StoreReAdapter(callback = {
                    val action =
                        SelectGuidStoreFragmentDirections.actionPageStoreToStoreDetailFragment(
                            it.store_name,
                            it
                        )
                    findNavController().navigate(action)

                })
                storeReAdapter?.submitList(list)
                adapter = storeReAdapter

            }

            mapView.removeAllPOIItems()
            addMapMarkers(list = list)

        }


    }

    private fun changeMapMarkers(list : MutableList<StoreModel>) {
        mapView.removeAllPOIItems()
        val marker = arrayOfNulls<MapPOIItem>(list.size).apply {
            repeat(size) {
                val model = list[it]
                this[it] = MapPOIItem().apply {
                    itemName = model.store_name
                    markerType = MapPOIItem.MarkerType.CustomImage
                    customImageResourceId = R.drawable.marker_icon_success
                    isCustomImageAutoscale = true
                    mapPoint = MapPoint.mapPointWithGeoCoord(
                        model.store_latitude.toDouble(),
                        model.store_longitude.toDouble()
                    )
                }
            }
        }

        mapView.addPOIItems(marker)
    }


    private fun addMapMarkers(list: MutableList<StoreModel>) {

        val marker = arrayOfNulls<MapPOIItem>(list.size).apply {
            repeat(size) {
                val model = list[it]
                this[it] = MapPOIItem().apply {
                    itemName = model.store_name
                    markerType = MapPOIItem.MarkerType.CustomImage
                    customImageResourceId = R.drawable.marker_icon_success
                    isCustomImageAutoscale = true
                    mapPoint = MapPoint.mapPointWithGeoCoord(
                        model.store_latitude.toDouble(),
                        model.store_longitude.toDouble()
                    )
                }
            }
        }

        mapView.addPOIItems(marker)
    }


}