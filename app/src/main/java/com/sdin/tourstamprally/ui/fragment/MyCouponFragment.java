package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdin.tourstamprally.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCouponFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCouponFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int frag_num;

    // TODO: Rename and change types of parameters

    public MyCouponFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param num Parameter 1.
     * @return A new instance of fragment MyCouponFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCouponFragment newInstance(int num) {
        MyCouponFragment fragment = new MyCouponFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            frag_num = getArguments().getInt("num", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_coupon, container, false);
    }
}