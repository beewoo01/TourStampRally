package com.sdin.tourstamprally.ui.fragment.report

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.adapter.report.CategoryAdapter
import com.sdin.tourstamprally.adapter.report.VisitAdapter
import com.sdin.tourstamprally.adapter.swipe.SwipeHelperCallback
import com.sdin.tourstamprally.databinding.FragmentVisithistoryBinding
import com.sdin.tourstamprally.model.CouponModel
import com.sdin.tourstamprally.model.HistorySpotModel
import com.sdin.tourstamprally.model.ReviewWriter
import com.sdin.tourstamprally.model.around.AllAroundEntity
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.dialog.DialogEventJoinSuccess
import com.sdin.tourstamprally.ui.dialog.event.TicketDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.listener.VisitItemClickListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitHistoryFragment : BaseFragment(), VisitItemClickListener {
    private var binding: FragmentVisithistoryBinding? = null
    private var historySpotList: MutableList<HistorySpotModel>? = null
    private lateinit var visitAdapter: VisitAdapter
    private var currentHistorySpotModel: HistorySpotModel? = null
    private var currentPosition = -1
    private var location = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_visithistory, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)

        binding?.apply {
            progressBar.visibility = View.VISIBLE
            setCategory()
            if(location == 0){
                location = 1
                setVisitInit(0)
            }
//            setVisitInit()


            btnGetstamp.setOnClickListener {
                val action = VisitHistoryFragmentDirections.actionPageReportToPageStamp()
                findNavController().navigate(action)
            }


            searchEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    Unit

                override fun afterTextChanged(s: Editable?) {
                    search(binding?.searchEdt?.text.toString())
                }

            })
        }
    }

    private fun setVisitInit(locationx : Int) {
        apiService.getHistorySpotVisit(Utils.User_Idx,locationx)
            .enqueue(object : Callback<List<HistorySpotModel>> {
                @SuppressLint("ClickableViewAccessibility")
                override fun onResponse(
                    call: Call<List<HistorySpotModel>>,
                    response: Response<List<HistorySpotModel>>
                ) {
                    binding?.progressBar?.visibility = View.GONE
                    if (response.isSuccessful) {

                        val result = response.body()
                        if (result != null) {
                            historySpotList = result.toMutableList()

                            val callback = SwipeHelperCallback()
                            callback.setClamp(-200F)

                            val itemTouchHelper = ItemTouchHelper(callback)
                            itemTouchHelper.attachToRecyclerView(binding?.visitRecyclerView)

                            val list = mutableListOf<HistorySpotModel>()
                            list.addAll(historySpotList!!)

                            visitAdapter = VisitAdapter(reviewWriterCallback = {
                                onWriteReviewClick(it)
                            }, visitItemClickListener = this@VisitHistoryFragment).apply {
                                submitList(list)
                            }

                            binding?.visitRecyclerView?.apply {
                                layoutManager = LinearLayoutManager(requireContext())
                                adapter = visitAdapter
//                                addItemDecoration(ItemDecoration())
                                setOnTouchListener { _, _ ->
                                    callback.removePreviousClamp(this)
                                    false
                                }
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<List<HistorySpotModel>>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }


    private fun onWriteReviewClick(model: ReviewWriter) {
        val action = VisitHistoryFragmentDirections.actionFragmentVisithistoryToFragmentWriteReview(
            writerModel = model,
            title = "리뷰작성"
        )
        findNavController().navigate(action)
    }


    private fun setCategory() {
        apiService.allLocation.enqueue(object : Callback<List<AllAroundEntity>> {
            override fun onResponse(
                call: Call<List<AllAroundEntity>>,
                response: Response<List<AllAroundEntity>>
            ) {
                val res = response.body()
                res?.let { result ->

                    binding?.categoryRecyclerView?.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                            .apply {
                                orientation = LinearLayoutManager.HORIZONTAL
                            }

                        adapter = CategoryAdapter(callback = {

                            location = it.location_idx

                            setVisitInit(location)

                        }).apply {
                            //setViewType(0)
                            val categoryList = mutableListOf<AllAroundEntity>()
                            val entity = AllAroundEntity(0, "전체보기")
                            categoryList.add(entity)
                            categoryList.addAll(result)
                            //setLocationCount(categoryList.count())
                            submitList(categoryList)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<AllAroundEntity>>, t: Throwable) {
                t.printStackTrace()
            }


        })

        val arr: Array<String> = resources.getStringArray(R.array.area_direction)

        binding?.categoryRecyclerView?.apply {
            adapter = CategoryAdapter { location ->
                //var paramData = location
                if (location.location_name == "전체") {
                    //paramData.hashTa = "";
                }
                //search(paramData)
            }.apply {

            }
        }

    }

    private fun getlocationspot(locationidx : Int){

    }

    private fun search(searchData: String) {
        val mutableList = mutableListOf<HistorySpotModel>()
        historySpotList?.let {
            if (searchData.isEmpty()) {
                mutableList.addAll(it)
            } else {

                for (model in it) {
                    if (model.location_name.lowercase().contains(searchData)) {
                        mutableList.add(model)
                    }

                    if (model.touristspot_name.lowercase().contains(searchData)) {
                        mutableList.add(model)
                    }

                }
            }

            visitAdapter.submitList(mutableList)
        }

    }

    private fun showToast(msg: String) {
        Toast.makeText(
            requireContext(), msg, Toast.LENGTH_SHORT
        ).show()
    }

    private fun insertInterest(touristSpotIdx: Int) {
        apiService.insert_intest(Utils.User_Idx, touristSpotIdx).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    showToast("찜하기에 성공하였습니다.")
                } else {
                    showToast("찜하기에 실패하였습니다.")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                t.printStackTrace()
                showToast("찜하기에 실패하였습니다.")
            }

        })
    }

    override fun deapClick(position: Int, model: HistorySpotModel) {
        apiService.select_interest_status(Utils.User_Idx, model.touristspot_idx)
            .enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null && result != 0) {
                            insertInterest(model.touristspot_idx)
                        }
                    } else {
                        showToast("찜하기에 실패하였습니다.")
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    showToast("찜하기에 실패하였습니다.")
                    t.printStackTrace()
                }

            })
    }

    override fun deleteReview(reviewIdx: Int, position: Int) {
        apiService.deleteReview(reviewIdx).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(t: Int) {
                    if (t > 0) {
                        visitAdapter.setChange(position = position)
                        showToast("리뷰 삭제에 성공하였습니다.")
                    }

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    override fun clearClick(model: HistorySpotModel, position: Int) {
        currentPosition = position
        currentHistorySpotModel = model

        model.coupon_idx?.let {
            val couponModel = CouponModel(
                it,
                Utils.User_Idx,
                model.coupon_number!!,
                model.coupon_status!!,
                model.touristspot_idx,
                model.coupon_createtime!!,
                model.coupon_createtime!!
            )

            TicketDialog(requireContext(), couponModel, model.couponCount) { pair ->
                if (pair.first == 0) {
                    //다시도전
                    showResetAlertDialog()
                } else if (pair.first == 1) {
                    //응모하기
                    pair.second?.let { paramModel ->
                        joinEvent(paramModel)
                    }
                }
            }.show()
        }


        //PopUp_Image(requireContext(), model).show()
        /*apiService.selectCoupon(tourSpotIdx, Utils.User_Idx).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<CouponModel>(){
                override fun onSuccess(model : CouponModel) {
                    try {
                        if (model != null) {
                            PopUp_Image(requireContext(), model).show()
                        } else {
                            showToast("해당 관광지의 쿠폰이 발급되지 않았습니다.")
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    showToast("해당 관광지의 쿠폰이 발급되지 않았습니다.")
                }

            })*/
    }

    private fun showResetAlertDialog() {
        DefaultBSRDialog(
            context = requireContext(),
            title = "스탬프 기록이 초기화 됩니다.",
            content = "응모하기를 하지 않을 시\n현재 쿠폰으로 응모가 불가합니다.\n다시 도전하시겠습니까?",
            isSpecial = false,
            isSwitchBtn = false,
            leftBtnStr = "도전",
            rightBtnStr = "응모하기"
        ) { callback ->

            if (callback) {
                //초기화

                if (currentHistorySpotModel != null) {
                    apiService.resetTourSpot(
                        currentHistorySpotModel!!.touristspot_idx,
                        Utils.User_Idx
                    )
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Int>() {
                            override fun onSuccess(result: Int) {
                                if (result > 0) {
                                    // 삭제 성공

                                    val list = visitAdapter.currentList.toMutableList()
                                    list.removeAt(currentPosition)
                                    visitAdapter.submitList(list)

                                    currentHistorySpotModel = null
                                }
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                            }
                        })
                }

            }
            else{
                //joinEvent

                currentHistorySpotModel?.coupon_idx?.let {
                    val couponModel = CouponModel(
                        it,
                        Utils.User_Idx,
                        currentHistorySpotModel?.coupon_number!!,
                        currentHistorySpotModel?.coupon_status!!,
                        currentHistorySpotModel?.touristspot_idx!!,
                        currentHistorySpotModel?.coupon_createtime!!,
                        currentHistorySpotModel?.coupon_createtime!!
                    )

                    joinEvent(couponModel)

                }
            }

        }.show()
    }

    private fun showSuccessEventJoin() {
        DialogEventJoinSuccess(requireContext()) {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse("http://bsrp.co.kr/list.php?bbs_id=main")
                )
            )

        }.show()
    }

    private fun joinEvent(model: CouponModel) {
        model.coupon_touristspot_idx
        if (model.coupon_status > 0) {
            //참여 완료 팝업
            showSuccessEventJoin()

        } else {
            apiService.insertEvent(Utils.User_Idx, model.coupon_idx, model.coupon_number)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Int>() {
                    override fun onSuccess(result: Int) {
                        when {
                            result >= 0 -> {
                                showSuccessEventJoin()
                            }
                            result > 0 -> {
                                //참여 완료 팝업
                                showSuccessEventJoin()
                            }
                            else -> {
                                //이벤트 창여 실패
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        }

    }


}