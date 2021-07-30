package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.adapter.CouponFragmentAdapter;
import com.sdin.tourstamprally.databinding.FragmentCouponMainBinding;
import com.sdin.tourstamprally.view.TabIndicatorRectF;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponMainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  FragmentCouponMainBinding binding;
    private FragmentStateAdapter pagerAdapter;
    private int numPage = 2;
    private int nowPage = 0;
    // TODO: Rename and change types of parameters
    private int mParam1;

    public CouponMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param num Parameter 1.
     * @return A new instance of fragment CouponMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CouponMainFragment newInstance(int num) {
        CouponMainFragment fragment = new CouponMainFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt("num", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coupon_main, container, false);
        View root = binding.getRoot();
        new TabIndicatorRectF(
                new TabIndicatorRectF.FixedWidthModifier(
                        getResources().getDimension(R.dimen._90sdp)))
                .replaceBoundsRectF(binding.tabCouponTabMenu);

        binding.tabCouponTabMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        pagerAdapter = new CouponFragmentAdapter(getActivity(), numPage);

        /*가로스크롤유무*/
        binding.vpPager.setUserInputEnabled(true);

        binding.vpPager.setAdapter(pagerAdapter);
        binding.vpPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.vpPager.setCurrentItem(2);
        binding.vpPager.setOffscreenPageLimit(2);
        binding.vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    //   vp_boardFaqPager.setCurrentItem(position);
                    binding.tabCouponTabMenu.getTabAt(position).select();

                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                nowPage = position;

            }

        });

        binding.vpPager.setCurrentItem(0);




        return root;
    }
    /*타입별 프래그먼트 컨트롤*/
    public void setFragment(int num) {

        /**
         *  0   기본정보 수정 프래그먼트
         *  1   비밀번호 변경 프래그먼트
         */

        switch (num) {

            case 0:

                binding.vpPager.setCurrentItem(0);

                break;

            case 1:

                binding.vpPager.setCurrentItem(1);

                break;


        }

    }
}