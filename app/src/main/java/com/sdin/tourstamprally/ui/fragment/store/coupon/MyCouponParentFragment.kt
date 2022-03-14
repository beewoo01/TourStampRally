package com.sdin.tourstamprally.ui.fragment.store.coupon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentMyCouponParentBinding
import com.sdin.tourstamprally.model.store_coupon.StoreMyCouponModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.listener.observe.Observer
import com.sdin.tourstamprally.utill.listener.observe.Subject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class MyCouponParentFragment : BaseFragment(), Subject<MutableList<StoreMyCouponModel>>{

    private var binding: FragmentMyCouponParentBinding? = null
    private var allMyCouponList: MutableList<StoreMyCouponModel>? = null
    private var normalList: MutableList<StoreMyCouponModel>? = null
    private var usedList: MutableList<StoreMyCouponModel>? = null
    private var overList: MutableList<StoreMyCouponModel>? = null
    private var myCouponFragment : MyCouponFragment = MyCouponFragment()
    private var myFinishCouponFragment: MyFinishCouponFragment = MyFinishCouponFragment()
    private val observers = mutableListOf<Observer<MutableList<StoreMyCouponModel>>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_coupon_parent, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding!!) {
        viewpager.apply {
            adapter = ViewPagerAdapter(requireActivity())


        }

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            Log.wtf("TabLayoutMediator", "position $position")
        }
        getData()

    }

    private fun getData() {
        apiService.selectMyCoupon(Utils.User_Idx).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<StoreMyCouponModel>>() {
                override fun onSuccess(list: List<StoreMyCouponModel>) {
                    allMyCouponList = mutableListOf()
                    allMyCouponList?.addAll(list.toMutableList())
                    initData()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun initData() {

        allMyCouponList?.let { it ->
            normalList = mutableListOf()
            usedList = mutableListOf()
            overList = mutableListOf()
            //0 -> 정상 1 -> 사용완료 2 -> 기간초과
            for (model in it) {
                if (model.store_mycoupon_state == 0) {
                    normalList?.add(model)
                }else if (model.store_mycoupon_state == 1) {
                    usedList?.add(model)

                }else if (model.store_mycoupon_state == 2) {
                    overList?.add(model)
                }

            }


            attach(myCouponFragment)
            attach(myFinishCouponFragment)
            normalList?.let { list ->
                if (list.size > 0) {
                    notifyUpdate(normalList!!, 0)
                }
            }

            if (usedList != null && overList != null) {
                notifyTwiceUpdate(usedList!!, overList!!,1)
            }



        }



    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    myCouponFragment
                }
                else -> {
                    myFinishCouponFragment
                }
            }
        }

    }

    override fun attach(observer: Observer<MutableList<StoreMyCouponModel>>) {
        observers.add(observer)
    }

    override fun detach(observer: Observer<MutableList<StoreMyCouponModel>>) {
        observers.remove(observer)
    }

    override fun notifyUpdate(massage: MutableList<StoreMyCouponModel>, position: Int) {
        observers[position].update1(massage)
        /*for (observer in observers){
            observer.update(massage)
        }*/
    }

    override fun notifyTwiceUpdate(
        massage1: MutableList<StoreMyCouponModel>,
        massage2: MutableList<StoreMyCouponModel>,
        position: Int
    ) {
        observers[position].update2(massage1, massage2)
    }

}