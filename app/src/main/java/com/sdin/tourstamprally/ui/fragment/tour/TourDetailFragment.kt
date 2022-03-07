package com.sdin.tourstamprally.ui.fragment.tour

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kakao.sdk.common.util.KakaoCustomTabsClient.openWithDefault
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.FragmentTourDetailBinding
import com.sdin.tourstamprally.model.TouristSpotPointDC
import com.sdin.tourstamprally.model.TouristSpotPointImg
import com.sdin.tourstamprally.ui.dialog.DetailDialog
import com.sdin.tourstamprally.ui.dialog.ReadyDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class TourDetailFragment : BaseFragment() {

    private var binding: FragmentTourDetailBinding? = null
    private lateinit var touristSpotPointModel: TouristSpotPointDC

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_detail, container, false)
        Log.wtf("TourDetailFragment", "onCreateView")
        val model: TourDetailFragmentArgs by navArgs()
        touristSpotPointModel = model.touristSpotPointDC
        Log.wtf("onCreateView", "touristSpotPointModel")
        binding?.fragment = this@TourDetailFragment
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        binding?.apply {
            tourNameTxv.text = touristSpotPointModel.touristspotpoint_name
            tourContentTxv.text = touristSpotPointModel.touristspotpoint_explan

            Glide.with(bgImv.context)
                .load("http://coratest.kr/imagefile/bsr/" + touristSpotPointModel.touristSpotPointCurverImg)
                .placeholder(R.drawable.sample_bg)
                .into(bgImv)


            locationAddressTxv.text = getAddress()

            getData()
            setMapView()

        }

    }

    private fun getData() {
        apiService.selectTourSpotPointImages(touristSpotPointModel.touristspotpoint_idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<TouristSpotPointImg>>() {
                override fun onSuccess(result: List<TouristSpotPointImg>) {
                    if (result != null && result.isNotEmpty()) {
                        touristSpotPointModel.touristSpotPointImgModelList = result
                    } else {
                        touristSpotPointModel.touristSpotPointImgModelList = mutableListOf()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }


    fun phoneClick() {
        val pattern1 = Pattern.compile("\\d{3}-\\d{4}-\\d{4}")
        val pattern2 = Pattern.compile("\\d{3}-\\d{3}-\\d{4}")
        val pattern3 = Pattern.compile("\\d{11}")
        val pattern4 = Pattern.compile("\\d{10}")

        touristSpotPointModel.touristspotpoint_contactinfo?.let {
            val isVaild = when {
                pattern1.matcher(it).matches() -> {
                    true
                }
                pattern2.matcher(it).matches() -> {
                    true
                }
                pattern3.matcher(it).matches() -> {
                    true
                }
                else -> {
                    pattern4.matcher(it).matches()
                }
            }

            if (isVaild) {
                try {
                    requireActivity().startActivity(
                        Intent(
                            Intent.ACTION_DIAL, Uri.parse("tel:$it")
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "요청중 에러 발생", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "해당 관광지는 등록된 전화번호가 없습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "해당 관광지는 등록된 전화번호가 없습니다.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun showDetailPopup() {
        TourDetailDialog(requireContext(), touristSpotPointModel).show()
        //DetailDialog(requireContext(), touristSpotPointModel).show()
    }

    fun watchMovie() {
        try {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(touristSpotPointModel.touristspotpoint_videolink)
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun naviClick() = with(touristSpotPointModel) {
        if (NaviClient.instance.isKakaoNaviInstalled(requireContext())) {
            requireActivity().startActivity(
                NaviClient.instance.navigateIntent(
                    Location(
                        touristspotpoint_name,
                        touristspotpoint_longitude.toString(),
                        touristspotpoint_latitude.toString()
                    ),
                    NaviOption(CoordType.WGS84)
                )
            )
        } else {
            val uri = NaviClient.instance.navigateWebUrl(
                Location(
                    touristspotpoint_name,
                    touristspotpoint_longitude.toString(),
                    touristspotpoint_latitude.toString()
                ),
                NaviOption(CoordType.WGS84)
            )
            openWithDefault(requireContext(), uri)
        }
    }

    fun kakaoLink() {
        showReadyDialog()
    }

    private fun showReadyDialog() {
        ReadyDialog(requireContext()).show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setMapView() {
        mapView = MapView(requireActivity())
        mapView.apply {
            setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(
                    touristSpotPointModel.touristspotpoint_latitude,
                    touristSpotPointModel.touristspotpoint_longitude
                ), 2, true
            )

            val customMarker = MapPOIItem()

            customMarker.apply {
                itemName = touristSpotPointModel.touristspotpoint_name
                mapPoint = MapPoint.mapPointWithGeoCoord(
                    touristSpotPointModel.touristspotpoint_latitude,
                    touristSpotPointModel.touristspotpoint_longitude
                )
                markerType = MapPOIItem.MarkerType.CustomImage; // 마커타입을 커스텀 마커로 지정.
                customImageResourceId = R.drawable.arrive_ic; // 마커 이미지.
                isCustomImageAutoscale = true
                // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.

                setCustomImageAnchor(
                    0.5f,
                    1.0f
                ) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
            }

            addPOIItem(customMarker)
            setOnTouchListener { _, _ -> true }
        }

        binding?.mapviewLayout?.addView(mapView)
    }

    private fun getAddress(): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(
                touristSpotPointModel.touristspotpoint_latitude,
                touristSpotPointModel.touristspotpoint_longitude,
                7
            )

            if (addresses == null || addresses.size == 0) {
                Toast.makeText(requireContext(), "주소를 발견하지 못했습니다.", Toast.LENGTH_SHORT).show()

                return touristSpotPointModel.touristspotpoint_name
            }

            val address = addresses[0]
            return address.getAddressLine(0)
        } catch (e: IOException) {
            e.printStackTrace()
            return touristSpotPointModel.touristspotpoint_name
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return touristSpotPointModel.touristspotpoint_name
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.mapviewLayout?.removeView(mapView)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}