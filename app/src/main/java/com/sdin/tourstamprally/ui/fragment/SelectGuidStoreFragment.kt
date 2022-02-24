package com.sdin.tourstamprally.ui.fragment

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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.StoreReAdapter
import com.sdin.tourstamprally.databinding.FragmentSelectGuidStoreBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.utill.listener.RecycleItemOnClick
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.*
import java.lang.Exception

class SelectGuidStoreFragment : BaseFragment2(), RecycleItemOnClick<StoreModel> {

    private var binding: FragmentSelectGuidStoreBinding? = null
    private val mapViewContainer: ViewGroup by lazy {
        binding?.mapLayout as ViewGroup
    }

    private var mapView: MapView? = null

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_guid_store, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        Log.wtf("onResume", "onResume")
        initView()
        getData()
    }

    private fun initView() {

        binding?.apply {

            mapView = if (mapView != null) {
                mapViewContainer.removeView(mapView)
                null
            }else {
                MapView(requireContext())
            }
            mapViewContainer.addView(mapView)

            if (!checkLocationService()) {
                Log.wtf("checkLocationService", "false")
                showDialogForLocationServiceSetting()
            } else {
                Log.wtf("checkLocationService", "true")
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
        }



            /*if (permissionCheck()) {
                Log.wtf("permissionCheck", "true")

                mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
            } else {
                permissionDialog()
                Log.wtf("permissionCheck", "false")

            }*/
        }
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
        Log.wtf("showDialogForLocationServiceSetting", "showDialogForLocationServiceSetting")
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
        mapView?.currentLocationTrackingMode =
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
        Log.wtf("onPause", "onPause")
        mapViewContainer.removeView(mapView)
        mapView = null

    }

    private fun getData() {
        apiService.selectAllStore().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<StoreModel>>() {
                override fun onSuccess(list: List<StoreModel>) {
                    initItem(list.toMutableList())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
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
            Log.wtf("permissionCheck2", "false")
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            return
        }
        Log.wtf("permissionCheck2", "true")

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.wtf("permissionCheck2", "addOnSuccessListener")
            location?.let {
                Log.wtf("permissionCheck2", "location notNull")
                mapView?.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(
                        location.latitude,
                        location.longitude
                    ), true
                )
            }

        }

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 20 * 1000
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult == null) {
                return
            }

            for (location in locationResult.locations) {
                if (location != null) {
                    mapView?.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            location.latitude,
                            location.longitude
                        ), true
                    )
                }
            }
        }
    }


    private fun permissionCheck(): Boolean {

        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                mapView?.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
            } catch (e: Exception) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    mapView?.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            location.latitude,
                            location.longitude
                        ), true
                    )
                }
            }


            true
        } else {


            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    1
                )
            }*/
            false
        }
    }

    /*when (PackageManager.PERMISSION_GRANTED) {
        (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )) and (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )) and (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ))
        -> {
            Log.wtf("permissionCheck", "ACCESS_BACKGROUND_LOCATION true")
            //permissionDialog()
            try {
                mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
            } catch (e: Exception) {
                e.printStackTrace()
            }

            true
        }


        else -> {


            requestPermissionLauncher.launch(versionCheck())
            permissionDialog()
            false
        }
    }*/


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.all { it.value == true }) {
                Log.wtf("requestPermissionLauncher", "true")
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

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun initItem(list: MutableList<StoreModel>) {
        binding?.run {
            storeRe.apply {
                adapter = StoreReAdapter(list = list, this@SelectGuidStoreFragment)
                addItemDecoration(DividerItemDecoration(requireContext(), 1))
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
            }

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

            mapView?.addPOIItems(marker)

        }


    }

    override fun onItemClickListener(model: StoreModel, position: Int) {

    }
}