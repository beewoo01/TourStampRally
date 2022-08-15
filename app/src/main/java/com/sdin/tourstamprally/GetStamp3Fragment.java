package com.sdin.tourstamprally;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class GetStamp3Fragment extends Fragment {


    public static GetStamp3Fragment newInstance(int num) {
        GetStamp3Fragment fragment = new GetStamp3Fragment();
        Bundle args = new Bundle();
        args.putInt("stampnum", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int number = getArguments().getInt("stampnum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_stamp3, container, false);
    }
}