package com.sdin.tourstamprally.ui.fragment.tour

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentTourSpotPointBinding
import com.sdin.tourstamprally.databinding.LocationReItemBinding
import com.sdin.tourstamprally.model.RallyMapModel
import com.sdin.tourstamprally.model.TouristSpotPointDC
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TourSpotPointFragment : BaseFragment() {

    private var binding: FragmentTourSpotPointBinding? = null
    private lateinit var list: List<TouristSpotPointDC>
    private lateinit var rallyMapModel: RallyMapModel
    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tour_spot_point, container, false)
        val model: TourSpotPointFragmentArgs by navArgs()
        rallyMapModel = model.rallyMapModel

        binding?.btnGetstamp?.setOnClickListener {

            val action = TourSpotPointFragmentDirections.actionFragmentTourSpotPointToPageStamp()
            findNavController().navigate(action)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        binding?.tourSpotPointPgb?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapLayout?.removeView(mapView)
        mapView = null
    }


    private fun getData() {
        val map = HashMap<String, Int>()
        map["user_idx"] = Utils.User_Idx
        map["touristspot_idx"] = rallyMapModel.touristspot_idx

        apiService.getTourLocation_spotpoint(map)
            .enqueue(object : Callback<List<TouristSpotPointDC>> {
                override fun onResponse(
                    call: Call<List<TouristSpotPointDC>>,
                    response: Response<List<TouristSpotPointDC>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            list = result
                            Log.wtf("getData", "getData")
                            setMap()
                            initRecyclerView()
                        }
                    }
                }

                override fun onFailure(call: Call<List<TouristSpotPointDC>>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun initRecyclerView() {
        with(binding!!) {
            bottomSheetIncl.recyclerviewLocationRe.apply {
                adapter = TourSpotPointAdapter(callback = {
                    val action =
                        TourSpotPointFragmentDirections.actionFragmentTourSpotPointToFragmentTourDetail(
                            it.touristspotpoint_name,
                            it
                        )
                    findNavController().navigate(action)

                }).apply {
                    submitList(list)
                }
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setMap() {
        if (mapView == null) {
            mapView = MapView(requireActivity())
        }

        mapView?.setMapCenterPointAndZoomLevel(
            MapPoint.mapPointWithGeoCoord(
                rallyMapModel.touristspot_latitude,
                rallyMapModel.touristspot_longitude
            ), 2, true
        )
        /*mapView.apply {
            setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(
                    rallyMapModel.touristspot_latitude,
                    rallyMapModel.touristspot_longitude
                ), 2, true
            )

            zoomIn(true)
            zoomOut(true)
        }*/

        binding?.mapLayout?.addView(mapView)

        val pointList = mutableListOf<MapPoint>()
        val markers = arrayOfNulls<MapPOIItem>(list.size)

        for ((index, model) in list.withIndex()) {
            pointList.add(
                MapPoint.mapPointWithGeoCoord(
                    model.touristspotpoint_latitude,
                    model.touristspotpoint_longitude
                )
            )

            markers[index] = MapPOIItem()
            markers[index]?.apply {
                mapPoint = pointList[index]
                itemName = list[index].touristspotpoint_name
                markerType = MapPOIItem.MarkerType.CustomImage


                customImageResourceId =
                    if (model.touristhistory_idx > 1) {
                        R.drawable.marker_icon_success
                    } else {
                        getCourseNumberMarkerImage(model.touristspotpoint_course_number)
                        //R.drawable.marker_icon_prev
                    }

                isCustomImageAutoscale = true
            }
        }

        mapView?.addPOIItems(markers)
        binding?.tourSpotPointPgb?.visibility = View.GONE
    }

    private fun getCourseNumberMarkerImage(courseNumber : Int) : Int =
        when(courseNumber) {
            1 -> R.drawable.around_course_marker1
            2 -> R.drawable.around_course_marker2
            3 -> R.drawable.around_course_marker3
            4 -> R.drawable.around_course_marker4
            5 -> R.drawable.around_course_marker5
            6 -> R.drawable.around_course_marker6
            7 -> R.drawable.around_course_marker7
            8 -> R.drawable.around_course_marker8
            9 -> R.drawable.around_course_marker9
            10 -> R.drawable.around_course_marker10
            11 -> R.drawable.around_course_marker11
            12 -> R.drawable.around_course_marker12
            13 -> R.drawable.around_course_marker13
            14 -> R.drawable.around_course_marker14
            15 -> R.drawable.around_course_marker15
            16 -> R.drawable.around_course_marker16
            17 -> R.drawable.around_course_marker17
            18 -> R.drawable.around_course_marker18
            19 -> R.drawable.around_course_marker19
            20 -> R.drawable.around_course_marker20
            21 -> R.drawable.around_course_marker21
            22 -> R.drawable.around_course_marker22
            23 -> R.drawable.around_course_marker23
            24 -> R.drawable.around_course_marker24
            25 -> R.drawable.around_course_marker25
            26 -> R.drawable.around_course_marker26
            27 -> R.drawable.around_course_marker27
            28 -> R.drawable.around_course_marker28
            29 -> R.drawable.around_course_marker29
            else -> R.drawable.around_course_marker30
        }

    class TourSpotPointAdapter(private val callback: (TouristSpotPointDC) -> Unit) :
        ListAdapter<TouristSpotPointDC, TourSpotPointAdapter.ViewHolder>(differ) {
        inner class ViewHolder(private val binding: LocationReItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun onBind(model: TouristSpotPointDC) {
                with(binding) {

                    topLine.visibility = if (absoluteAdapterPosition == 0) {
                        View.INVISIBLE
                    } else {
                        View.VISIBLE
                    }

                    if (absoluteAdapterPosition == currentList.size - 1) {
                        bottomLine.visibility = View.GONE
                        bottomLayout.visibility = View.GONE
                    } else {
                        bottomLine.visibility = View.VISIBLE
                        bottomLayout.visibility = View.VISIBLE
                    }

                    Glide.with(locationImv.context)
                        .load(
                            if (absoluteAdapterPosition % 2 == 0) {
                                R.drawable.location_puple
                            } else {
                                R.drawable.location_sky
                            }
                        ).into(locationImv)

                    Glide.with(logoImv.context)
                        .load(
                            if (model.touristhistory_idx > 0) {
                                R.drawable.mainlogo
                            } else {
                                R.drawable.logo_gray
                            }
                        ).into(logoImv)

                    spotName.text = model.touristspotpoint_name
                    explanTxv.text = model.touristspotpoint_explan
                    if(model.touristhistory_idx > 0){
                        topLayout.setBackgroundResource(R.drawable.bg_rounded_03_ivory)

                    }else{
                        topLayout.setBackgroundResource(R.drawable.bg_rounded_03_white)
                    }
                    topLayout.setOnClickListener {
                        callback(model)
                    }



                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LocationReItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(currentList[position])
        }

        companion object {
            val differ = object : DiffUtil.ItemCallback<TouristSpotPointDC>() {
                override fun areItemsTheSame(
                    oldItem: TouristSpotPointDC,
                    newItem: TouristSpotPointDC
                ): Boolean {
                    return oldItem.touristspotpoint_idx == newItem.touristspotpoint_idx
                }

                override fun areContentsTheSame(
                    oldItem: TouristSpotPointDC,
                    newItem: TouristSpotPointDC
                ): Boolean {
                    return oldItem == newItem
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}