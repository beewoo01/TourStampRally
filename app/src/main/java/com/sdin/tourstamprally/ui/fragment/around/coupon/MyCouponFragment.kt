package com.sdin.tourstamprally.ui.fragment.around.coupon

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentMyCouponBinding
import com.sdin.tourstamprally.databinding.MycouponItemBinding
import com.sdin.tourstamprally.model.store_coupon.StoreMyCouponModel
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.listener.observe.Observer
import com.sdin.tourstamprally.utill.recyclerveiew.CustomItemDecoration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class MyCouponFragment : BaseFragment(), Observer<MutableList<StoreMyCouponModel>> {

    private var binding: FragmentMyCouponBinding? = null
    private var list: MutableList<StoreMyCouponModel>? = null
    private var reVisibilityState = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_coupon, container, false)

        getData()

        return binding?.root
    }

    private fun getData() {
        apiService.getMyStampCount(Utils.User_Idx).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(result: Int) {
                    binding?.mystampCountTxv?.text = "$result"
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }


    private fun initView() = with(binding!!) {


        couponListTxv.setOnClickListener {
            if (!reVisibilityState) {
                storeCouponRe.visibility = View.VISIBLE

                Glide.with(it.context)
                    .load(R.drawable.ic_arrow_down)
                    .into(couponArrowImb)


                reVisibilityState = true

            } else {
                storeCouponRe.visibility = View.GONE
                Glide.with(it.context)
                    .load(R.drawable.ic_arrow_up)
                    .into(couponArrowImb)

                reVisibilityState = false
            }
        }

        storeCouponRe.apply {
            mycouponCountTxv.text = list?.size.toString()

            adapter = MyCouponAdapter() {

                val bundle = Bundle().apply {
                    putParcelable("model", it)
                    putString("title", it.store_coupon_name)
                }
                findNavController().navigate(R.id.couponDetailFragment, bundle)
            }.apply {
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

    class MyCouponAdapter(private val callBack: (StoreMyCouponModel) -> Unit) :
        ListAdapter<StoreMyCouponModel, MyCouponAdapter.ViewHolder>(differ) {

        inner class ViewHolder(private val binding: MycouponItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun onBind(model: StoreMyCouponModel) = with(binding) {
                Glide.with(logoImv.context)
                    .load("http://coratest.kr/imagefile/bsr/store_logo/" + model.store_logo_icon)
                    .placeholder(R.drawable.sample_profile_image)
                    .error(R.drawable.sample_profile_image)
                    .into(logoImv)

                idxTxv.text = (absoluteAdapterPosition + 1).toString()
                couponName.text = model.store_coupon_name
                startDataTxv.text = model.store_coupon_expiration_startDate
                endDataTxv.text = model.store_coupon_expiration_endDate

                itemContainer.setOnClickListener {
                    callBack(model)
                }
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