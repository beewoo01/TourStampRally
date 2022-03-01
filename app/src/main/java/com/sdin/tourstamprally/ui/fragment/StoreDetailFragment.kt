package com.sdin.tourstamprally.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakao.sdk.common.util.KakaoCustomTabsClient.openWithDefault
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.StoreDetailHashTagReAdapter
import com.sdin.tourstamprally.databinding.FragmentStoreDetailBinding
import com.sdin.tourstamprally.model.StoreHashtag
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.model.StoreSubDTO
import com.sdin.tourstamprally.ui.dialog.ReadyDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.lang.Exception

class StoreDetailFragment : BaseFragment() {

    private var binding: FragmentStoreDetailBinding? = null
    private lateinit var model: StoreSubDTO
    private var argModel: StoreModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_store_detail, container, false)

        binding?.fragment = this@StoreDetailFragment

        val args: StoreDetailFragmentArgs by navArgs()
        argModel = args.storeModel


        //argModel = StoreDetailFragmentArgs.fromBundle(arguments).storeModel
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        initView()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initView() {
        argModel?.let {
            binding?.apply {
                storeNameTxv.text = it.store_name
                storeInfoTxv.text = it.store_info
                tourContentTxv.text = it.store_description
                locationAddressTxv.text = it.store_address
                Glide.with(bgImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + it.store_curver_img).into(bgImv)
                mapviewLayout.addView(setMapView(it))
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setMapView(model: StoreModel): MapView {
        return MapView(requireActivity()).apply {
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
    }

    private fun initData() {
        argModel?.store_idx?.let {
            apiService.selectStoreDetail(it).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StoreSubDTO>() {
                    override fun onSuccess(model: StoreSubDTO) {
                        //this@StoreDetailFragment.model = model
                        argModel?.storeSubDto = model
                        initHashTag()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        }
    }

    private fun initHashTag() {
        binding?.tagRecyclerview?.apply {
            adapter = StoreDetailHashTagReAdapter().apply {
                submitList(argModel?.storeSubDto?.storeHashTagList)
            }

            addItemDecoration(StoreDetailHashTagReAdapter.PhOffsetItemDecoration(10))
        }
    }

    fun onCallClick() {
        argModel?.store_number?.let {
            Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))
        } ?: run {
            Toast.makeText(requireContext(), "해당 매장은 등록된 전화번호가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun onNaviClick() {
        argModel?.let {
            if (NaviClient.instance.isKakaoNaviInstalled(requireContext())) {
                requireActivity().startActivity(
                    NaviClient.instance.navigateIntent(
                        Location(it.store_name, it.store_latitude, it.store_longitude),
                        NaviOption(CoordType.WGS84)
                    )
                )
            } else {
                openWithDefault(
                    requireContext(), NaviClient.instance.navigateWebUrl(
                        Location(it.store_name, it.store_latitude, it.store_longitude),
                        NaviOption(CoordType.WGS84)
                    )
                )

            }
        }

    }

    fun onShareClick() {
        showReadydialog()
    }

    fun onDetailClick() {

    }

    fun onVideoClick() {
        try {
            argModel?.store_link?.let {
                requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showReadydialog() {
        ReadyDialog(requireContext()).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    /*class StoreDetailHashTagReAdapter :
        ListAdapter<StoreHashtag, StoreDetailHashTagReAdapter.ItemViewHolder>(differ) {
        inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            @SuppressLint("SetTextI18n")
            fun bind(model: StoreHashtag) {
                val tagTxv = view.findViewById<TextView>(R.id.tag_txv)
                tagTxv.text = "#${model.store_hashtag_tag}"
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ItemViewHolder(inflater.inflate(R.layout.re_item_store_hashtag, parent, false))
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(currentList[position])
        }

        companion object {

            val differ = object : DiffUtil.ItemCallback<StoreHashtag>() {

                override fun areItemsTheSame(
                    oldItem: StoreHashtag,
                    newItem: StoreHashtag
                ): Boolean {
                    return oldItem.store_hashtag_idx == newItem.store_hashtag_idx
                }

                override fun areContentsTheSame(
                    oldItem: StoreHashtag,
                    newItem: StoreHashtag
                ): Boolean {
                    return oldItem == newItem
                }

            }
        }

    }*/

}