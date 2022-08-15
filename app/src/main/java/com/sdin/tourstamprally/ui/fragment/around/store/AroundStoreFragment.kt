package com.sdin.tourstamprally.ui.fragment.around.store

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.StoreReAdapter
import com.sdin.tourstamprally.databinding.FragmentAroundStoreBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.ui.fragment.around.AroundFragmentDirections
import com.sdin.tourstamprally.utill.GpsTracker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class AroundStoreFragment : BaseFragment() {

    private var binding: FragmentAroundStoreBinding? = null
    private var mapView: MapView? = null
    private val allStoreList: MutableList<StoreModel> = mutableListOf()
    private lateinit var storeReAdapter: StoreReAdapter
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var noSettingMapView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_around_store,
                container,
                false
            )

        return binding!!.root
    }

    fun onResetFragment() {
        noSettingMapView = false
        initMapView()
        binding?.storeChipGroup?.checkedChipId?.let { setCheckedChip(it) }
    }

    override fun onResume() {
        super.onResume()
        noSettingMapView = findNavController().currentDestination?.label == "fragment_store_detail"

        initMapView()
        getData()
    }

    private fun initMapView() {
        binding?.apply {

            if (mapView == null && !noSettingMapView) {
                mapView = MapView(requireActivity())
                mapLayout.addView(mapView)
            }

            val gpsTracker = GpsTracker(requireContext())
            latitude = gpsTracker.latitude
            longitude = gpsTracker.longitude

            mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true)

            val mapCircle = MapCircle(
                MapPoint.mapPointWithGeoCoord(latitude, longitude),
                50,
                Color.argb(0, 0, 0, 0),
                Color.argb(30, 89, 14, 222)
            )

            val currentLocationMarker = MapPOIItem()
            val currentMapPoint =
                MapPoint.mapPointWithGeoCoord(latitude, longitude)

            currentLocationMarker.apply {
                mapPoint = currentMapPoint
                markerType = MapPOIItem.MarkerType.CustomImage
                itemName = "Current Location"
                customImageResourceId = R.drawable.current_location_marker
                isCustomImageAutoscale = true
            }

            mapView?.addPOIItem(currentLocationMarker)

            mapView?.addCircle(mapCircle)




            storeChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->

                storeReAdapter.submitList(
                    changeList(
                        when (checkedIds[0]) {
                            R.id.restaurant_chip -> {
                                0
                            }

                            R.id.cafe_chip -> {
                                1
                            }

                            R.id.accommodation_chip -> {
                                2
                            }

                            else -> {
                                3
                            }
                        }
                    )
                )
            }

        }
    }

    private fun setCheckedChip(checkedChipId: Int) {
        storeReAdapter.submitList(
            changeList(
                when (checkedChipId) {
                    R.id.restaurant_chip -> {
                        0
                    }

                    R.id.cafe_chip -> {
                        1
                    }

                    R.id.accommodation_chip -> {
                        2
                    }

                    else -> {
                        3
                    }
                }
            )
        )

    }

    private fun getData() {
        apiService.selectAllStore(Utils.User_Idx).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<StoreModel>>() {
                override fun onSuccess(list: List<StoreModel>) {

                    allStoreList.addAll(list.toMutableList())
                    initItem(allStoreList.toMutableList())
                    setStoreMarkers(allStoreList.toMutableList())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun setStoreMarkers(list: MutableList<StoreModel>) {

        val markers = arrayOfNulls<MapPOIItem>(list.size)
        val mapPoints = mutableListOf<MapPoint>()

        mapView?.removeAllPOIItems()

        val currentLocationMarker = MapPOIItem()
        val currentMapPoint =
            MapPoint.mapPointWithGeoCoord(latitude, longitude)

        currentLocationMarker.apply {
            mapPoint = currentMapPoint
            markerType = MapPOIItem.MarkerType.CustomImage
            itemName = "Current Location"
            customImageResourceId = R.drawable.current_location_marker
            isCustomImageAutoscale = true
        }

        mapView?.addPOIItem(currentLocationMarker)

        for ((index, point) in list.withIndex()) {
            mapPoints.add(
                MapPoint.mapPointWithGeoCoord(
                    point.store_latitude.toDouble(),
                    point.store_longitude.toDouble()
                )
            )
            markers[index] = MapPOIItem()
            markers[index]?.apply {
                mapPoint = mapPoints[index]
                itemName = point.store_name
                markerType = MapPOIItem.MarkerType.CustomImage

                customImageResourceId = R.drawable.around_store_red_dot_marker


                isCustomImageAutoscale = true
            }

        }

        mapView?.addPOIItems(markers)
    }

    private fun setNullMapView() {
        mapView?.removeAllPOIItems()
        binding?.mapLayout?.removeView(mapView)
        mapView = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroy()
    }

    override fun onPause() {
        super.onPause()
        setNullMapView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initItem(list: MutableList<StoreModel>) {

        binding?.run {


            bottomSheetIncl.storeRe.apply {

                //addItemDecoration(DividerItemDecoration(requireContext(), 1))
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }


                storeReAdapter = StoreReAdapter(callback = {
                    val action =
                        AroundFragmentDirections.actionPageStoreToStoreDetailFragment(
                            it.store_name,
                            it
                        )

                    setNullMapView()
                    findNavController().navigate(action)

                }, interCallback = { model ->

                    /*if (model.user_store_interest_idx > 0) {

                        apiService.remove_intest(model.user_store_interest_idx, 2)
                            .enqueue(object : Callback<Int> {
                                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                    if (response.isSuccessful) {
                                        val result = response.body()
                                        if (result != null) {
                                            if (result > 0) {
                                                val currentList: MutableList<StoreModel> =
                                                    mutableListOf()
                                                currentList.addAll(storeReAdapter.currentList)
                                                val position = currentList.indexOf(model)
                                                model.user_store_interest_idx = 0
                                                currentList[position] = model
                                            }
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    t.printStackTrace()
                                }

                            })

                    } else {

                        apiService.insert_store_inter(Utils.User_Idx, model.store_idx)
                            .enqueue(object : Callback<Int> {
                                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                    if (response.isSuccessful) {
                                        val result = response.body()
                                        if (result != null) {
                                            if (result > 0) {

                                                val currentList: MutableList<StoreModel> =
                                                    mutableListOf()
                                                currentList.addAll(storeReAdapter.currentList)
                                                val position = currentList.indexOf(model)
                                                model.user_store_interest_idx = result
                                                currentList[position] = model

                                            }
                                        }


                                    }
                                }

                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    t.printStackTrace()
                                }

                            })


                    }*/

                }, locationCallback = { model ->

                    //리사이클러뷰 아이템 선택시 마커
                    mapView?.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            model.store_latitude.toDouble(),
                            model.store_longitude.toDouble()
                        ), true
                    )
                    selectMapMarker(model)
                }, latitude, longitude)
                storeReAdapter.submitList(list)
                adapter = storeReAdapter

            }

            mapView?.removeAllPOIItems()
            binding?.bottomSheetIncl?.layoutCon?.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_MOVE -> {
                        /*addMapMarkers(list = list)
                                    getCurrentCourse()*/
                    }

                }
                true
            }


        }


    }

    private fun selectMapMarker(model: StoreModel) {
        mapView?.removeAllPOIItems()

        val currentLocationMarker = MapPOIItem()
        val currentMapPoint =
            MapPoint.mapPointWithGeoCoord(latitude, longitude)

        currentLocationMarker.apply {
            mapPoint = currentMapPoint
            markerType = MapPOIItem.MarkerType.CustomImage
            itemName = "Current Location"
            customImageResourceId = R.drawable.current_location_marker
            isCustomImageAutoscale = true
        }

        mapView?.addPOIItem(currentLocationMarker)

        val selectedStoreLocationMarker = MapPOIItem()
        val selectedStoreMapPoint =
            MapPoint.mapPointWithGeoCoord(
                model.store_latitude.toDouble(),
                model.store_longitude.toDouble()
            )

        selectedStoreLocationMarker.apply {
            mapPoint = selectedStoreMapPoint
            markerType = MapPOIItem.MarkerType.CustomImage
            itemName = model.store_name
            customImageResourceId = R.drawable.around_store_red_pin_merker
            isCustomImageAutoscale = true
        }
        mapView?.addPOIItem(selectedStoreLocationMarker)
    }

    private fun changeList(position: Int): MutableList<StoreModel> {

        if (position == 3) {
            setStoreMarkers(allStoreList)
            return allStoreList
        }

        val subList = mutableListOf<StoreModel>()

        for (model in allStoreList) {
            if (model.store_type == position) {
                subList.add(model)
            }
        }

        setStoreMarkers(subList)
        return subList
        //storeReAdapter.submitList(subList)


    }

    override fun onDestroy() {
        if (mapView != null) {
            setNullMapView()
        }
        super.onDestroy()
        binding = null
    }

    companion object {

        fun newInstance(): AroundStoreFragment {
            return AroundStoreFragment()
        }
    }


}