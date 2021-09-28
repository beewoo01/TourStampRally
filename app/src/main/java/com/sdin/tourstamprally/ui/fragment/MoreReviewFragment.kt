package com.sdin.tourstamprally.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.More_Frag_LocationAdapter
import com.sdin.tourstamprally.adapter.swipe.More_Frag_ReviewAdapter
import com.sdin.tourstamprally.databinding.FragmentMoreReviewBinding
import com.sdin.tourstamprally.model.AllReviewDTO
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MoreReviewFragment : BaseFragment() {

    private var binding: FragmentMoreReviewBinding? = null
    private val selectedLocationIdx = arrayListOf<Int>()
    var reviewAdapter: More_Frag_ReviewAdapter? = null
    var locationAdapter: More_Frag_LocationAdapter? = null
    private var textint = 0
    private val reviewList = arrayListOf<AllReviewDTO>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_review, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        searchEdtObservable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {
                Log.wtf("onSubscribe", d.toString())
            }

            override fun onNext(t: String?) {
                t?.let {
                    Log.wtf("myObservable", "onNext $t")
                    // TODO: 9/28/21 검색
                }

            }

            override fun onError(e: Throwable?) {
                Log.wtf("subscribe", "onError")
                e?.printStackTrace()
            }

            override fun onComplete() = Unit

        })



    }

    val searchEdtObservable = Observable.create { emitter: ObservableEmitter<String>? ->
        binding?.searchEdt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun afterTextChanged(s: Editable?) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emitter?.onNext(s.toString())
            }
        })
    }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())


    private fun getData() {

        apiService.select_all_review().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<AllReviewDTO>>() {
                    override fun onSuccess(t: List<AllReviewDTO>?) {
                        t?.let {
                            val arrayList = arrayListOf<AllReviewDTO>()
                            arrayList.addAll(t)
                            reviewList.addAll(t)
                            initData(arrayList)

                        }
                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                })

    }

    private fun initData(list: List<AllReviewDTO>) {

        val map = HashMap<Int, String>()

        for (i in list) {
            map[i.location_idx] = i.location_name
        }


        val locationList: MutableList<Pair<Int, String>> = mutableListOf()
        for ((k, v) in map) {
            locationList.add(Pair(k, v))
        }

        val reviewList: MutableList<AllReviewDTO> = mutableListOf()
        reviewList.addAll(list)
        reviewAdapter = More_Frag_ReviewAdapter(requireContext(), reviewList)
        reviewAdapter?.apply {
            setListener(object : More_Frag_ReviewAdapter.MoreReviewListener {
                override fun onItemClick(data: AllReviewDTO) {
                    // TODO: 9/28/21 상세페이지로 ㄱㄱ
                    Log.wtf("ReviewAdpaterdata", data.toString())

                }
            })
            binding?.reviewRecyclerview?.adapter = this
        }





        locationAdapter = More_Frag_LocationAdapter(locationList, requireContext())
        locationAdapter?.apply {

            locationObserver?.subscribe( object : Observer<Int>{
                override fun onSubscribe(d: Disposable?) {

                }

                override fun onNext(t: Int?) {
                    t?.let {
                        Log.wtf("locationIDX??", t.toString())
                        //val adList : MutableList<AllReviewDTO> = MutableList()


                        val paramList = ArrayList<AllReviewDTO>()
                        for (i in reviewList){
                            if (i.location_idx == t){
                                Log.wtf("iiiiii idx??", i.location_idx.toString())
                                Log.wtf("iiiiii name??", i.location_name.toString())
                                paramList.add(i)
                            }

                        }

                        reviewAdapter?.changeList(paramList)

                    }

                }

                override fun onError(e: Throwable?) {
                    Log.wtf("rs", "onError")
                    e?.printStackTrace()
                }

                override fun onComplete() {

                }

            })


            binding?.locationRecyclerview?.adapter = this
        }

    }

    override fun onStop() {
        super.onStop()
        //locationObserver.doOnDispose()
    }

    val locationObserver = Observable.create { emitter: ObservableEmitter<Int>? ->
        locationAdapter?.setMoreLocationListener(object : More_Frag_LocationAdapter.MoreLocationItemListener {
            override fun onItemClick(item: Int) {

                emitter?.onNext(item)

            }

        })

    }.subscribeOn(Schedulers.io())


    companion object {

        @JvmStatic
        fun newInstance() =
                MoreReviewFragment()
    }


}