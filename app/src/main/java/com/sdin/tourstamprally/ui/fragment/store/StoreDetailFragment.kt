package com.sdin.tourstamprally.ui.fragment.store

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.sdin.tourstamprally.adapter.StoreDetailHashTagReAdapter
import com.sdin.tourstamprally.databinding.FragmentStoreDetailBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.model.StoreSubDTO
import com.sdin.tourstamprally.ui.dialog.ReadyDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.lang.Exception

class StoreDetailFragment : BaseFragment() {

    private var binding: FragmentStoreDetailBinding? = null
    private lateinit var argModel: StoreModel
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_store_detail, container, false)

        binding?.fragment = this@StoreDetailFragment
        val args: StoreDetailFragmentArgs by navArgs()
        argModel = args.storeModel
        return binding?.root!!
    }

    override fun onResume() {
        super.onResume()
        initView()
        initData()
    }

    private fun initView() = with(argModel) {
        binding?.apply {
            storeNameTxv.text = store_name
            storeInfoTxv.text = store_info
            tourContentTxv.text = store_description
            locationAddressTxv.text = store_address
            Glide.with(bgImv.context)
                .load("http://coratest.kr/imagefile/bsr/$store_curver_img")
                .placeholder(R.drawable.sample_bg)
                .into(bgImv)
            setMapView(this@with)
        }

    }


    override fun onPause() {
        super.onPause()
        binding?.mapviewLayout?.removeView(mapView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setMapView(model: StoreModel) {
        mapView = MapView(requireActivity()).apply {
            setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(
                    model.store_latitude.toDouble(),
                    model.store_longitude.toDouble()
                ), 2, true
            )

            val customMarker = MapPOIItem()
            customMarker.apply {
                itemName = model.store_name
                mapPoint = MapPoint.mapPointWithGeoCoord(
                    model.store_latitude.toDouble(),
                    model.store_longitude.toDouble()
                )
                markerType = MapPOIItem.MarkerType.CustomImage
                customImageResourceId = R.drawable.arrive_ic
                isCustomImageAutoscale = true
                setCustomImageAnchor(0.5f, 1.5f)
            }

            addPOIItem(customMarker)

            setOnTouchListener { _, _ -> true }
        }
        binding?.mapviewLayout?.addView(mapView)
    }

    private fun initData() {
        apiService.selectStoreDetail(argModel.store_idx).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<StoreSubDTO>() {
                override fun onSuccess(model: StoreSubDTO) {
                    //this@StoreDetailFragment.model = model
                    argModel.storeSubDto = model
                    initHashTag()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })

    }

    private fun initHashTag() {
        binding?.tagRecyclerview?.apply {
            adapter = null
            adapter = StoreDetailHashTagReAdapter().apply {
                submitList(argModel.storeSubDto?.storeHashTagList)
            }
            if (itemDecorationCount == 0) {
                addItemDecoration(StoreDetailHashTagReAdapter.PhOffsetItemDecoration(10))
            }

        }
    }


    fun onCallClick() {
        argModel.store_number?.let {
            requireActivity().startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it")))
        } ?: run {
            Toast.makeText(requireContext(), "해당 매장은 등록된 전화번호가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun onNaviClick() {
        argModel.run {

            if (NaviClient.instance.isKakaoNaviInstalled(requireContext())) {
                requireActivity().startActivity(
                    NaviClient.instance.navigateIntent(
                        Location(
                            store_name,
                            store_latitude,
                            store_longitude
                        ),
                        NaviOption(CoordType.WGS84)
                    )
                )
            } else {
                openWithDefault(
                    requireContext(), NaviClient.instance.navigateWebUrl(
                        Location(
                            store_name,
                            store_latitude,
                            store_longitude
                        ),
                        NaviOption(CoordType.WGS84)
                    )
                )

            }
        }


    }

    fun onShareClick() {
        showReadyDialog()
    }

    fun onDetailClick() {
        StoreDetailDialog(requireContext(), argModel).show()
    }

    fun onVideoClick() {
        try {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(argModel.store_link)
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showReadyDialog() {
        ReadyDialog(requireContext()).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}