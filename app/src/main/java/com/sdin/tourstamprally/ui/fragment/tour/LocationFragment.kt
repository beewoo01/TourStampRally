package com.sdin.tourstamprally.ui.fragment.tour

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentLocationBinding
import com.sdin.tourstamprally.databinding.ItemReRallyMapBinding
import com.sdin.tourstamprally.model.RallyMapModel
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.model.TopFourLocationModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class LocationFragment : BaseFragment() {
    private var binding: FragmentLocationBinding? = null
    private lateinit var model: TopFourLocationModel
    private lateinit var list: List<RallyMapModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        val args: LocationFragmentArgs by navArgs()
        model = args.topFourModel
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        binding?.apply {
            locationTxv.text = model.location_name

            Glide.with(topLayout.context)
                .load("http://coratest.kr/imagefile/bsr/" + model.location_img)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        topLayout.background = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })
        }

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        binding!!.locationPgb.visibility = View.VISIBLE
        apiService.getTourLocation_for_spot(Utils.User_Idx, model.location_idx)
            .enqueue(object : Callback<List<RallyMapModel>> {
                override fun onResponse(
                    call: Call<List<RallyMapModel>>,
                    response: Response<List<RallyMapModel>>
                ) {
                    val result = response.body()
                    if (result != null) {
                        list = result
                    }

                    setRecyclerViewAdapter()
                }

                override fun onFailure(call: Call<List<RallyMapModel>>, t: Throwable) {}
            })
    }

    private fun setRecyclerViewAdapter() {
        binding?.recyclerviewLocationRe?.apply {
            adapter = LocationFragAdapter(callback = { selectedModel ->
                val action =
                    LocationFragmentDirections.actionFragmentLocationToFragmentTourSpotPoint(
                        selectedModel.touristspot_name,
                        selectedModel
                    )


                findNavController().navigate(action)

            }).apply {
                submitList(list)
            }
            layoutManager = GridLayoutManager(requireContext(), 2)
            setProgress()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProgress() = with(binding!!) {
        /*var allLocationPercent = 0
        var myLocationPercent = 0
        for (model in list) {
            model.allCount?.let {
                allLocationPercent += it
                myLocationPercent += it
            }

        }*/

        /*val allCount: Int =
            (((myLocationPercent.toDouble() / allLocationPercent.toDouble())) * 100).roundToInt()*/
        val myCount =
            ((model.myHistoryCount.toDouble()) / model.allPointCount.toDouble() * 100).roundToInt()

        seekBarLocation.max = 100
        seekBarLocation.progress = myCount
        Log.wtf("setProgress", "allPointCount ${model.allPointCount}")
        Log.wtf("setProgress", "myHistoryCount ${model.myHistoryCount}")
        Log.wtf("setProgress", "myCount $myCount")
        seekPercentTxv.text = "${myCount}%"
        locationPgb.visibility = View.GONE

    }


    class LocationFragAdapter(private val callback: (RallyMapModel) -> Unit) :
        ListAdapter<RallyMapModel, LocationFragAdapter.ViewHolder>(differ) {
        inner class ViewHolder(private val viewHolderBinding: ItemReRallyMapBinding) :
            RecyclerView.ViewHolder(viewHolderBinding.root) {

            fun onBind(model: RallyMapModel) {
                with(viewHolderBinding) {
                    Glide.with(locationImv.context)
                        .load(
                            if (absoluteAdapterPosition % 2 == 0)
                                R.drawable.icon_deep_blue
                            else R.drawable.icon_sky_blue
                        ).into(locationImv)

                    Glide.with(logoImv.context)
                        .load(
                            if (setProgress(model = model))
                                R.drawable.stemp_ic
                            else R.drawable.logo_gray
                        ).into(logoImv)

                    spotName.text = model.touristspot_name

                    if (!model.touristspot_address.isNullOrEmpty()) {
                        explanTxv.text = model.touristspot_address
                    }

                    Glide.with(spotImv.context)
                        .load("http://coratest.kr/imagefile/bsr/" + model.touristspot_img)
                        .error(R.drawable.sample_bg)
                        .into(spotImv)

                    topLayout.setOnClickListener {
                        callback(model)
                    }


                }
            }

            private fun setProgress(model: RallyMapModel): Boolean {
                if (model.allCount != null && model.myCount != null) {
                    return model.allCount <= model.myCount
                }
                return false

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemReRallyMapBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(currentList[position])

        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<RallyMapModel>() {
            override fun areItemsTheSame(oldItem: RallyMapModel, newItem: RallyMapModel): Boolean {
                return oldItem.touristspot_idx == newItem.touristspot_idx
            }

            override fun areContentsTheSame(
                oldItem: RallyMapModel,
                newItem: RallyMapModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}