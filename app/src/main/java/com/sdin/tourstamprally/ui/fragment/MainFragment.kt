package com.sdin.tourstamprally.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sdin.tourstamprally.*
import com.sdin.tourstamprally.databinding.FragmentMainBinding
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding
import com.sdin.tourstamprally.model.HistorySpotModel
import com.sdin.tourstamprally.model.RallyMapModel
import com.sdin.tourstamprally.model.StoreBannerCouponDTO
import com.sdin.tourstamprally.model.TopFourLocationModel
import com.sdin.tourstamprally.ui.dialog.GetStampDialog
import retrofit2.Callback


class MainFragment : BaseFragment() {

    private lateinit var viewBind: FragmentMainBinding
    private val tourList: MutableList<TopFourLocationModel> = mutableListOf()
    private var historySpotList: MutableList<HistorySpotModel> = mutableListOf()
    private val currentPosition = Int.MAX_VALUE / 2
    private val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }
    private var currentPage = 0
    private val thread = Thread(PagerRunnable())

    private var rallyMapModel: RallyMapModel? = null

    private val bannerCouponList: MutableList<StoreBannerCouponDTO> = mutableListOf()

    private fun filterModel(selectedIdx: Int): TopFourLocationModel {
        val paramModel = tourList.filter {
            it.location_idx == selectedIdx
        }
        return paramModel[0]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBind = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        viewBind.fragment = this@MainFragment



        initView()

        setProgressbar()

        getTop4Location()



        getCoupon()
        return viewBind.root
    }


    private fun initView() {

        val mainslideradapter = ViewPager2Adapter(this)

        mainslideradapter.addFragment(MainPic1Fragment())
        mainslideradapter.addFragment(MainPic2Fragment())
        mainslideradapter.addFragment(MainPic3Fragment())
        mainslideradapter.addFragment(MainPic4Fragment())

        viewBind.backgroundImg.adapter = mainslideradapter
        viewBind.backgroundImg.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val child = viewBind.backgroundImg.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
        viewBind.backgroundImg.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })
        if (!thread.isAlive) {
            thread.start()
        }

        viewBind.btnViewall.setOnClickListener {
            moreClick()
        }
        viewBind.btnGetstamp.setOnClickListener {

            val action = MainFragmentDirections.actionPageHomeToPageStamp()
            findNavController().navigate(action)
        }

        viewBind.btnRoadtour.setOnClickListener {
            clickmenu(viewBind.roadtourTvx.text.toString(), filterModel(1))
        }

        viewBind.btnTrackingtour.setOnClickListener {

            clickmenu(viewBind.trackingtourTvx.text.toString(), filterModel(3))
        }

        viewBind.btnHistorytour.setOnClickListener {
            clickmenu(viewBind.historytourTvx.text.toString(), filterModel(7))
        }

        viewBind.btnFindtreasure.setOnClickListener {
            clickmenu(viewBind.findtreasureTvx.text.toString(), filterModel(4))
        }

        viewBind.btnFestivaltour.setOnClickListener {
            clickmenu(viewBind.festivaltourTvx.text.toString(), filterModel(5))
        }

        viewBind.btnHardtour.setOnClickListener {
            clickmenu(viewBind.hardtourTvx.text.toString(), filterModel(2))
        }

        viewBind.btnWebtoontour.setOnClickListener {
            clickmenu(viewBind.webtoontourTvx.text.toString(), filterModel(6))
        }

        viewBind.circleIv.setOnClickListener {
            //Log.wtf("historySpotListsize", historySpotList?.get(0)?.touristspot_idx.toString())
            if (historySpotList.size > 0) {

                apiService.select_tour_location_for_spot_main(
                    Utils.User_Idx,
                    historySpotList[0].location_idx,
                    historySpotList[0].touristspot_idx
                )
                    .enqueue(object : Callback<List<RallyMapModel>> {
                        override fun onResponse(
                            call: retrofit2.Call<List<RallyMapModel>>,
                            response: retrofit2.Response<List<RallyMapModel>>
                        ) {
                            val result = response.body()
                            if (result != null) {

                                result[0].run {
                                    rallyMapModel = RallyMapModel(
                                        touristspot_idx,
                                        touristspot_name,
                                        touristspotpoint_idx,
                                        touristspot_latitude,
                                        touristspot_longitude,
                                        touristspot_address,
                                        allCount,
                                        myCount,
                                        touristspotpoint_name,
                                        touristspot_img,
                                        user_touristspot_interest_idx
                                    )
                                }


                                val action =
                                    MainFragmentDirections.actionPageHomeToFragmentTourSpotPoint(
                                        historySpotList[0].touristspot_name,
                                        rallyMapModel!!
                                    )
                                findNavController().navigate(action)
                            }
                        }

                        override fun onFailure(
                            call: retrofit2.Call<List<RallyMapModel>>,
                            t: Throwable
                        ) {
                            t.printStackTrace()
                        }

                    })

            } else {

                findNavController().navigate(R.id.action_page_home_to_page_store)
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        thread.interrupt()
    }

    fun setPage() {
        if (currentPage == 4)
            currentPage = 0
        viewBind.backgroundImg.setCurrentItem(currentPage, false)
        currentPage += 1
    }


    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().setStatusBarOrigin()
    }

    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                try {
                    Thread.sleep(2000)
                    handler.sendEmptyMessage(0)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    inner class ViewPager2Adapter(fragment: Fragment) : FragmentStateAdapter(requireActivity()) {
        private val fragmentlist: ArrayList<Fragment> = ArrayList()

        override fun getItemCount(): Int = fragmentlist.size

        override fun createFragment(position: Int): Fragment {

            return fragmentlist[position]
        }

        fun addFragment(fragment: Fragment) {
            fragmentlist.add(fragment)
            notifyItemInserted(fragmentlist.size - 1)
        }

    }

    //actionbar 제거
    fun Activity.setStatusBarTransparent() {

        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        /*if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }*/
    }

    fun Context.statusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else 0
    }

    fun Context.navigationHeight(): Int {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else 0
    }

    fun Activity.setStatusBarOrigin() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        if (Build.VERSION.SDK_INT >= 30) {    // API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
    }


    private fun getCoupon() {
        val list: MutableList<BannerModel> = mutableListOf()
        list.add(BannerModel(0, R.drawable.ic_main_ad))


        apiService.selectBannerCoupon().enqueue(object : Callback<List<StoreBannerCouponDTO>> {
            override fun onResponse(
                call: retrofit2.Call<List<StoreBannerCouponDTO>>,
                response: retrofit2.Response<List<StoreBannerCouponDTO>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result?.toMutableList()?.let { bannerCouponList.addAll(it) }
                    bannerCouponList.map {
                        list.add(BannerModel(it.store_coupon_idx, it.store_coupon_banner_img))
                    }
                    initViewPagerAdapter(list)

//                    Log.wtf("getCoupon", result?.get(0)?.store_coupon_banner_img)
                } else {
                    initViewPagerAdapter(list)
                }
            }

            override fun onFailure(call: retrofit2.Call<List<StoreBannerCouponDTO>>, t: Throwable) {
                t.printStackTrace()
                initViewPagerAdapter(list)
            }

        })
    }

    private fun initViewPagerAdapter(couponList: MutableList<BannerModel>) {
        val adapter = ViewPagerAdapter(requireContext(), couponList) { idx ->
            Log.wtf("initViewPagerAdapter", "idx 1111")
            Log.wtf("initViewPagerAdapter", "idx ? $idx")
            var paramModel: StoreBannerCouponDTO? = null
            for (model in bannerCouponList) {
                if (model.store_coupon_idx == idx) {
                    paramModel = model
                    Log.wtf("initViewPagerAdapter", "model.store_coupon_idx 1111")
                    Log.wtf("initViewPagerAdapter", "model.store_coupon_idx ? $idx")
                }
            }
            var action = MainFragmentDirections.actionPageHomeToCouponBannerFragment(
                "쿠폰 교환", paramModel!!
            )

            findNavController().navigate(action)
        }
        viewBind.viewpagerAd.adapter = adapter
        viewBind.viewpagerAd.clipToOutline = true
    }

    data class BannerModel(
        val sampleIdx: Int,
        val sampleImage: Any
    )

    //viewpageradapter
    inner class ViewPagerAdapter(
        private val context: Context,
        private val list: MutableList<BannerModel>,
        private val callBack: (idx: Int) -> Unit
    ) : PagerAdapter() {

        private var layoutInflater: LayoutInflater? = null

        override fun getCount(): Int {
            return list.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = layoutInflater!!.inflate(R.layout.fragment_imageslide, null)
            val img = v.findViewById<View>(R.id.imgview) as ImageView

            //img.setImageResource(Image[position])
            Glide.with(requireContext()).load(
                if (position == 0) {
                    list[position].sampleImage
                } else {
                    "http://coratest.kr/imagefile/bsr/coupon/" + list[position].sampleImage
                }
            ).into(img)


            img.setOnClickListener {
                if (position == 0) {

                    GetStampDialog(requireContext(), requireActivity()).show()
                } else {
                    callBack(list[position].sampleIdx)

                }
            }
            val vp = container as ViewPager
            vp.addView(v, 0)
            return v
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val vp = container as ViewPager
            val v = `object` as View
            vp.removeView(v)
        }
    }

    //progressbar 설정
    private fun setProgressbar() {

        apiService.getHistorySpot(Utils.User_Idx)
            .enqueue(object : Callback<List<HistorySpotModel>> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: retrofit2.Call<List<HistorySpotModel>>,
                    response: retrofit2.Response<List<HistorySpotModel>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {

                            historySpotList = result.toMutableList()
                            //historyspot이 존재할때
                            if (historySpotList?.size!! > 0) {

                                viewBind.circleIvBg.visibility = View.VISIBLE
                                viewBind.tvxNohistory.visibility = View.INVISIBLE

                                viewBind.tourTitleTxv.text = result[0].location_name
                                viewBind.tourContentTxv.text = result[0].touristspot_name

                                viewBind.progressCircle.progress =
                                    (result[0].myCount.toDouble() / result[0].allCount.toDouble() * 100).toInt()
                                viewBind.tourPercentTxv.text =
                                    (result[0].myCount.toDouble() / result[0].allCount.toDouble() * 100).toInt()
                                        .toString() + "%"

                                Glide.with(viewBind.circleIv.context)
                                    .load("http://coratest.kr/imagefile/bsr/" + result.get(0).touristspot_img)
                                    .placeholder(R.drawable.sample_profile_image)
                                    .error(R.drawable.sample_profile_image)
                                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                                    .into(viewBind.circleIv)


                            } else {
                                //historyspot이 존재하지않을때
                                viewBind.tvxNohistory.visibility = View.VISIBLE
                                viewBind.tourTitleTxv.visibility = View.INVISIBLE
                                viewBind.tourContentTxv.visibility = View.INVISIBLE

                                viewBind.progressCircle.progress = 0
                                viewBind.tourPercentTxv.text = "0%"
                                viewBind.tourPercentTxv.setTextColor(Color.rgb(101, 29, 230))
                                Glide.with(viewBind.circleIv.context)
                                    .load(R.drawable.bg_circle_white)
                                    .placeholder(R.drawable.sample_profile_image)
                                    .error(R.drawable.sample_profile_image)
                                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                                    .into(viewBind.circleIv)
                                viewBind.circleIvBg.visibility = View.GONE


                            }
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<HistorySpotModel>>, t: Throwable) {
                    t.printStackTrace()
                }

            })


    }

    private fun getAllTourList() {

        /*apiService.getTour(viewBind.roadtourCon.getTag() as Int)
            .enqueue(object : Callback<List<Tour_Spot>>{
                override fun onResponse(
                    call: Call<List<Tour_Spot>>,
                    response: Response<List<Tour_Spot>>
                ){

            })*/
    }


    private fun getTop4Location() {

        apiService.getFourLocations(Utils.User_Idx)
            .enqueue(object : Callback<List<TopFourLocationModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<TopFourLocationModel>>,
                    response: retrofit2.Response<List<TopFourLocationModel>>
                ) {
                    response.body()?.let {
                        //TODO List 처리
                        tourList.addAll(it)
                        //clickmenu(viewBind.roadtourTvx.text.toString(), viewBind.roadtourCon.getTag() as Int)
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<List<TopFourLocationModel>>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                }

            })

    }

    private fun clickmenu(name: String, model: TopFourLocationModel) {
//        Log.wtf("clickmenu", "tourList[locationIdx] " + tourList[locationIdx])
        val action =
            MainFragmentDirections.actionMainfragmentToFragmentLocation(name, model)
        findNavController().navigate(action)
    }

    private fun setData(list: MutableList<TopFourLocationModel>) {
        val adapter = RallyRecyclerviewAdapter(context = requireContext(), list)
        /*viewBind.rallyRecyclerview.apply {
            this.adapter = adapter
        }*/

    }
    /*private fun getAllReview() {
        apiService.select_all_review().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<AllReviewModel>>() {
                override fun onSuccess(t: List<AllReviewModel>) {
                    viewBind.reviewReProgressbar.visibility = View.GONE
                    initReviewData(arrayListOf<AllReviewModel>().apply {
                        addAll(t)
                    })
                }

                override fun onError(e: Throwable) {
                    viewBind.reviewReProgressbar.visibility = View.GONE
                }

            })
    }*/


    fun cuverClick() {
        //공지사항
        findNavController().navigate(
            R.id.notice,
            Bundle().apply {
                putInt("state", 3)
                putString("title", "공지사항")
            })
    }


    fun moreClick() {
        //더보기
        /*val action = MainFragmentDirections.actionMainfragmentToFragmentDirectionGuid(
            tourList.toTypedArray()
        )
        findNavController().navigate(action)*/

        val action = MainFragmentDirections.actionPageHomeToFragmentDirectionGuid(
            tourList.toTypedArray()
        )
        findNavController().navigate(action)

    }

    fun reviewMoreClick() {
        //리뷰보기
        val action = MainFragmentDirections.actionMainfragmentToFragmentMoreReview()
        findNavController().navigate(action)

    }

    /*private fun initReviewData(reviewDataList: MutableList<AllReviewModel>) {
        viewBind.mainReviewRecyclerview.apply {
            adapter =
                ReviewMainReAdapter() { model ->
                    val action = MainFragmentDirections.actionMainfragmentToFragmentReviewComents(
                        title = model.touristspot_name,
                        state = 4,
                        reviewIdx = model.review_idx,
                        reviewUserIdx = model.user_idx
                    )

                    findNavController().navigate(action)

                }.apply {
                    submitList(reviewDataList)
                }

            layoutManager = LinearLayoutManager(requireContext())

        }

    }*/


    inner class RallyRecyclerviewAdapter(
        val context: Context,
        private val adapterList: MutableList<TopFourLocationModel>
    ) : RecyclerView.Adapter<RallyRecyclerviewAdapter.ViewHolder>() {
        inner class ViewHolder(private val holderBinding: StepRallyLocationItemBinding) :
            RecyclerView.ViewHolder(holderBinding.root) {

            @SuppressLint("SetTextI18n")
            fun onBind(model: TopFourLocationModel) {
                holderBinding.location.text = model.location_name
                holderBinding.stepRallyBg.setOnClickListener {
                    val action = MainFragmentDirections.actionMainfragmentToFragmentLocation(
                        model.location_name + " 랠리 맵",
                        model
                    )


                    if (model.touristspotpoint_createtime == null) {
                        Toast.makeText(requireContext(), "등록된 관광지가 없습니다.", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    findNavController().navigate(action)
                }

                Glide.with(context)
                    .load("http://coratest.kr/imagefile/bsr/" + model.location_img)
                    .error(R.drawable.sample_bg)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            holderBinding.mainLayout.background = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) = Unit

                    })

                setPopular(model)

                val allCountd =
                    (model.myHistoryCount.toDouble() / model.allPointCount.toDouble() * 100).toInt()
                holderBinding.seekbar.max = model.allPointCount
                holderBinding.seekbar.progress = model.myHistoryCount
                holderBinding.seekTxv.text = "$allCountd%"

            }


            private fun setPopular(model: TopFourLocationModel) {
                if (model.popular > model.allPointCount) {
                    holderBinding.newsImv.visibility = View.VISIBLE
                    setGlide(holderBinding.newsImv, R.drawable.hot_icon)
                } else {
                    holderBinding.newsImv.visibility = View.VISIBLE
                    setGlide(holderBinding.newsImv, R.drawable.new_icon)
                }
            }


            private fun setGlide(target: ImageView, img: Int) {
                Glide.with(context).load(img).into(target)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                StepRallyLocationItemBinding.inflate(
                    LayoutInflater.from(requireContext()),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(adapterList[position])
        }

        override fun getItemCount(): Int = 4

    }


}