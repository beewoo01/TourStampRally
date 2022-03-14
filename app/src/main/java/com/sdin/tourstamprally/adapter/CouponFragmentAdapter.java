package com.sdin.tourstamprally.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sdin.tourstamprally.ui.fragment.store.coupon.MyCouponFragment;


public class CouponFragmentAdapter extends FragmentStateAdapter {
    public int mCount;

    public CouponFragmentAdapter(FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);


        switch (index) {
            case 0:
                return MyCouponFragment.newInstance(null);
            default:
                return null;
        }


    }


    @Override
    public int getItemCount() {
        return mCount;
    }

    public int getRealPosition(int position) {
        return position % mCount;
    }



}
