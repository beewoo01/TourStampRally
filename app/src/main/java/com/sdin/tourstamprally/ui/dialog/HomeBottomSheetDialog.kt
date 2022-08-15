package com.sdin.tourstamprally.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sdin.tourstamprally.*
import com.sdin.tourstamprally.data.PreferenceUtil
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class HomeBottomSheetDialog(val itemClick : (Int) -> Unit)
    : BottomSheetDialogFragment() {


    private lateinit var viewPager2 : ViewPager2
    private lateinit var prefs : PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = PreferenceUtil(requireContext())
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.wtf("bottomsee", "onDestroy")
        // 0 -> 다시 보지 않기  1 -> Dismiss
        itemClick(1)
        //itemClick.
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_mainpop, container, false)
        viewPager2 = view.findViewById(R.id.fragPager)
        val indicator = view.findViewById<DotsIndicator>(R.id.dots_indicator)

        val fragmentList = mutableListOf<Fragment>()
        fragmentList.add(MainPop1Fragment.newInstance(0))
        fragmentList.add(MainPop2Fragment.newInstance(1))
        fragmentList.add(MainPop3Fragment.newInstance(2))

        val btn_dontshow = view.findViewById<Button>(R.id.btn_never_saw)
        btn_dontshow.setOnClickListener {
            //preferencemanager
            prefs.setString(Utils.User_Idx.toString(),"never")
            dialog?.dismiss()
        }
        val btn_close = view.findViewById<Button>(R.id.btn_close)
        btn_close.setOnClickListener {
            dialog?.dismiss()
        }

        viewPager2.adapter = ViewPagerAdapter(fragmentList)
        indicator.setViewPager2(viewPager2)

        return view
    }

    inner class ViewPagerAdapter(private val list : MutableList<Fragment>)
        : FragmentStateAdapter(requireActivity()) {

        override fun getItemCount(): Int {
            return list.count()
        }

        override fun createFragment(position: Int): Fragment {
            return list[position]
        }

    }
    }