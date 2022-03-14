package com.sdin.tourstamprally.ui.fragment.store.coupon

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentCouponMainBinding
import com.sdin.tourstamprally.databinding.FragmentMyCouponBinding
import com.sdin.tourstamprally.databinding.FragmentMyCouponParentBinding
import com.sdin.tourstamprally.databinding.MycouponItemBinding
import com.sdin.tourstamprally.model.StoreModel
import com.sdin.tourstamprally.model.store_coupon.StoreMyCouponModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.listener.observe.Observer
import com.sdin.tourstamprally.utill.recyclerveiew.CustomItemDecoration
import com.sdin.tourstamprally.view.TabIndicatorRectF
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class MyCouponFragment : BaseFragment(), Observer<MutableList<StoreMyCouponModel>> {

    private var binding: FragmentMyCouponBinding? = null
    private var list: MutableList<StoreMyCouponModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_coupon, container, false)

        binding?.storeCouponRe?.apply {
            adapter = MyCouponAdapter()
        }

        return binding?.root
    }

    private fun initView() = with(binding!!) {
        storeCouponRe.apply {
            adapter = MyCouponAdapter().apply {
                submitList(list)
            }
            addItemDecoration(
                CustomItemDecoration(
                    topPadding = 20, bottomPadding = 20, null, null
                )
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(list: MutableList<StoreMyCouponModel>) =
            MyCouponFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("list", ArrayList<Parcelable>(list))
                }
            }

    }

    class MyCouponAdapter : ListAdapter<StoreMyCouponModel, MyCouponAdapter.ViewHolder>(differ) {
        inner class ViewHolder(private val binding: MycouponItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun onBind(model: StoreMyCouponModel) = with(binding) {
                Glide.with(logoImv.context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.store_logo_icon)
                    .placeholder(R.drawable.sample_profile_image)
                    .error(R.drawable.sample_profile_image)
                    .into(logoImv)

                couponName.text = model.store_coupon_name
                startDataTxv.text = model.store_coupon_expiration_startDate
                endDataTxv.text = model.store_coupon_expiration_endDate
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                MycouponItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(currentList[position])
        }

        companion object {
            val differ = object : DiffUtil.ItemCallback<StoreMyCouponModel>() {
                override fun areItemsTheSame(
                    oldItem: StoreMyCouponModel,
                    newItem: StoreMyCouponModel
                ): Boolean {
                    return oldItem.store_mycoupon_idx == newItem.store_mycoupon_idx
                }

                override fun areContentsTheSame(
                    oldItem: StoreMyCouponModel,
                    newItem: StoreMyCouponModel
                ): Boolean {
                    return oldItem == newItem
                }

            }
        }


    }

    override fun update1(message: MutableList<StoreMyCouponModel>) {
        list = message
        initView()
    }

    override fun update2(
        message1: MutableList<StoreMyCouponModel>,
        message2: MutableList<StoreMyCouponModel>
    ) = Unit


}