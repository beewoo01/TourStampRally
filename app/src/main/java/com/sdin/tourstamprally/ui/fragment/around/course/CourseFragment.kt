package com.sdin.tourstamprally.ui.fragment.around.course

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.around.AroundCourseAdapter
import com.sdin.tourstamprally.databinding.FragmentCourseBinding
import com.sdin.tourstamprally.model.AroundTouristSpot
import com.sdin.tourstamprally.model.RallyMapModel
import com.sdin.tourstamprally.model.around.coursePoint.AroundCoursePoint
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


class CourseFragment : BaseFragment() {

    private var binding: FragmentCourseBinding? = null
    private var mapView: MapView? = null
    private lateinit var aroundCourseAdapter: AroundCourseAdapter
    private val courseList = mutableListOf<AroundTouristSpot>()
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var noSettingMapView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_course, container, false)
        Log.wtf("CourseFragment", "onCreateView")
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        Log.wtf("CourseFragment", "onResume")
        noSettingMapView = findNavController().currentDestination?.label == "fragment_tour_detail"
        initMapView()
        getData()

    }


    fun onResetFragment() {
        Log.wtf("CourseFragment", "onResetFragment")
        noSettingMapView = false
        initMapView()
    }


    private fun initMapView() {

        binding?.apply {

            if (mapView == null && !noSettingMapView) {
                mapView = MapView(requireActivity())
                mapLayout.addView(mapView)
            }

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
        }
    }

    private fun changeList(position: Int): MutableList<AroundTouristSpot> {
        val subList = mutableListOf<AroundTouristSpot>()

        val tour: Int = when (position) {
            R.id.road_tour_chip -> {
                1
            }

            R.id.hard_tour_chip -> {
                2
            }

            R.id.tracking_tour_chip -> {
                3
            }

            R.id.treasure_tour_chip -> {
                4
            }

            R.id.festival_tour_chip -> {
                5
            }

            R.id.webtoon_tour_chip -> {
                6
            }

            R.id.history_tour_chip -> {
                7
            }

            else -> {
                0
            }
        }


        if (tour == 0) {
            return courseList
        }

        for (model in courseList) {
            if (model.location_idx == tour) {
                subList.add(model)
            }
        }

        return subList

    }

    private fun getData() {
        apiService.selectAroundTouristSpot()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<AroundTouristSpot>>() {
                override fun onSuccess(t: List<AroundTouristSpot>) {
                    courseList.addAll(t.toMutableList())
                    initTourAdapter(t.toMutableList())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun initTourAdapter(aroundTouristSpotList: MutableList<AroundTouristSpot>) {
        binding?.bottomSheetIncl?.tourRe?.apply {

            aroundCourseAdapter = AroundCourseAdapter({ model ->

                mapView?.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(
                        model.touristspot_latitude.toDouble(),
                        model.touristspot_longitude.toDouble()
                    ), true
                )

                setMapViewCircle(
                    model.touristspot_latitude.toDouble(),
                    model.touristspot_longitude.toDouble()
                )

                apiService.selectAroundCoursePoint(Utils.User_Idx, model.touristspot_idx)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<List<AroundCoursePoint>>() {
                        override fun onSuccess(t: List<AroundCoursePoint>) {
                            setMapMarkers(t.toMutableList(), model)
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                    })


            },
                { model ->
                    //action_page_store_to_fragment_tour_spot_point
                    val paramModel = RallyMapModel(
                        touristspot_idx = model.touristspot_idx,
                        touristspot_name = model.touristspot_name,
                        touristspot_latitude = model.touristspot_latitude.toDouble(),
                        touristspot_longitude = model.touristspot_longitude.toDouble(),
                        touristspot_img = model.touristspot_img
                    )

                    val action = AroundFragmentDirections.actionPageStoreToFragmentTourSpotPoint(
                        model.touristspot_name,
                        paramModel
                    )
                    setNullMapView()
                    //listener?.onCourseDetail(model.touristspot_name, paramModel)
                    findNavController().navigate(action)

                }, latitude, longitude)

            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }


            aroundCourseAdapter.submitList(aroundTouristSpotList)
            adapter = aroundCourseAdapter

        }

        binding?.courseChipGroup?.setOnCheckedStateChangeListener { _, checkedIds ->
            aroundCourseAdapter.submitList(
                changeList(checkedIds[0])
            )
        }
    }

    private fun setMapMarkers(
        pointList: MutableList<AroundCoursePoint>,
        currentSelectedModel: AroundTouristSpot
    ) {
        mapView?.removeAllPOIItems()

        with(currentSelectedModel) {
            val currentTourSpotMarker = MapPOIItem()
            val currentSpotMapPoint =
                MapPoint.mapPointWithGeoCoord(
                    touristspot_latitude.toDouble(), touristspot_longitude.toDouble()
                )

            currentTourSpotMarker.apply {
                mapPoint = currentSpotMapPoint
                itemName = touristspot_name
                markerType = MapPOIItem.MarkerType.CustomImage

                customImageResourceId = R.drawable.marker_icon_success
            }

            mapView?.addPOIItem(currentTourSpotMarker)
        }


        val markers = arrayOfNulls<MapPOIItem>(pointList.size)
        val mapPoints = mutableListOf<MapPoint>()

        for ((index, point) in pointList.withIndex()) {
            mapPoints.add(
                MapPoint.mapPointWithGeoCoord(
                    point.spotpoint_latitude.toDouble(),
                    point.spotpoint_longitude.toDouble()
                )
            )
            markers[index] = MapPOIItem()
            markers[index]?.apply {
                mapPoint = mapPoints[index]
                itemName = point.spotpoint_name
                markerType = MapPOIItem.MarkerType.CustomImage

                customImageResourceId = setMarkerImage(point.course_num)


                isCustomImageAutoscale = true
            }

        }

        mapView?.addPOIItems(markers)
    }


    private fun setMarkerImage(courseNumber: Int): Int =
        when (courseNumber) {
            1 -> {
                R.drawable.around_course_marker1
            }

            2 -> {
                R.drawable.around_course_marker2
            }

            3 -> {
                R.drawable.around_course_marker3
            }

            4 -> {
                R.drawable.around_course_marker4
            }

            5 -> {
                R.drawable.around_course_marker5
            }

            6 -> {
                R.drawable.around_course_marker6
            }

            7 -> {
                R.drawable.around_course_marker7
            }

            8 -> {
                R.drawable.around_course_marker8
            }

            9 -> {
                R.drawable.around_course_marker9
            }

            10 -> {
                R.drawable.around_course_marker10
            }

            11 -> {
                R.drawable.around_course_marker11
            }

            12 -> {
                R.drawable.around_course_marker12
            }

            13 -> {
                R.drawable.around_course_marker13
            }

            14 -> {
                R.drawable.around_course_marker14
            }

            15 -> {
                R.drawable.around_course_marker15
            }

            16 -> {
                R.drawable.around_course_marker16
            }

            17 -> {
                R.drawable.around_course_marker17
            }

            18 -> {
                R.drawable.around_course_marker18
            }

            19 -> {
                R.drawable.around_course_marker19
            }

            20 -> {
                R.drawable.around_course_marker20
            }

            21 -> {
                R.drawable.around_course_marker21
            }

            22 -> {
                R.drawable.around_course_marker22
            }

            23 -> {
                R.drawable.around_course_marker23
            }

            24 -> {
                R.drawable.around_course_marker24
            }

            25 -> {
                R.drawable.around_course_marker25
            }

            26 -> {
                R.drawable.around_course_marker26
            }

            27 -> {
                R.drawable.around_course_marker27
            }

            28 -> {
                R.drawable.around_course_marker28
            }

            29 -> {
                R.drawable.around_course_marker29
            }

            else -> {
                R.drawable.around_course_marker30
            }
        }


    private fun checkLocationService(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.all { it.value }) {
                permissionCheck()
            }
        }

    private fun permissionCheck() {
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

        val gpsTracker = GpsTracker(requireContext())
        latitude = gpsTracker.latitude
        longitude = gpsTracker.longitude

        mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true)

        //mapcircle
        val mapCircle = MapCircle(
            MapPoint.mapPointWithGeoCoord(latitude, longitude),
            50,
            Color.argb(0, 0, 0, 0),
            Color.argb(30, 89, 14, 222)
        )

        val currentLocationMarker = MapPOIItem()
        val currentSpotMapPoint =
            MapPoint.mapPointWithGeoCoord(latitude, longitude)

        currentLocationMarker.apply {
            mapPoint = currentSpotMapPoint
            markerType = MapPOIItem.MarkerType.CustomImage
            itemName = "Current Location"
            customImageResourceId = R.drawable.current_location_marker
            isCustomImageAutoscale = true
        }

        mapView?.addPOIItem(currentLocationMarker)

        mapView?.addCircle(mapCircle)

    }

    private fun setMapViewCircle(latitude: Double, longitude: Double) {
        mapView?.run {
            removeAllCircles()
            addCircle(
                MapCircle(
                    MapPoint.mapPointWithGeoCoord(latitude, longitude),
                    300,
                    Color.argb(0, 0, 0, 0),
                    Color.argb(20, 255, 89, 61)
                )

            )
        }
    }

    private fun setNullMapView() {
        mapView?.removeAllPOIItems()
        binding?.mapLayout?.removeView(mapView)
        mapView = null
    }

    override fun onPause() {
        super.onPause()

        setNullMapView()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        /** 중요! 제거 안되고 onPause()로 다시 Up 되므로
         * onDestroyView 시 onDestroy()로 바로 이동*/

        onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null) {
            setNullMapView()
        }
        Log.wtf("CourseFragment", "onDestroy")
        binding = null
    }



    companion object {


        fun newInstance(): CourseFragment{

            return CourseFragment()
        }
    }



}