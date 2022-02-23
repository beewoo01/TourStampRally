package com.sdin.tourstamprally.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.StoreReAdapter
import com.sdin.tourstamprally.databinding.FragmentSelectGuidStoreBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.v2.RecycleItemOnClick
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapView

class SelectGuidStoreFragment : BaseFragment2(), RecycleItemOnClick<StoreModel> {

    private var binding: FragmentSelectGuidStoreBinding? = null
    private val mapViewContainer : ViewGroup by lazy {
        binding?.mapLayout as ViewGroup
    }

    private val mapView : MapView by lazy {
        MapView(requireContext())
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
        initView()
        getData()
    }

    private fun initView() {
        binding?.apply {
            mapViewContainer.addView(mapView)
        }
    }

    override fun onPause() {
        super.onPause()
        mapViewContainer.removeView(mapView)

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

    private fun initItem(list : MutableList<StoreModel>) {
        binding?.storeRe?.apply {
            adapter = StoreReAdapter(list = list, this@SelectGuidStoreFragment)
            addItemDecoration(DividerItemDecoration(requireContext(), 1))
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    override fun onItemClickListener(model: StoreModel, position: Int) {

    }
}