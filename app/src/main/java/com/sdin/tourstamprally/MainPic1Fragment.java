package com.sdin.tourstamprally;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class MainPic1Fragment extends Fragment {


    public static MainPic1Fragment newInstance(int num) {
        MainPic1Fragment fragment = new MainPic1Fragment();
        Bundle args = new Bundle();
        args.putInt("slidenum", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int number = getArguments().getInt("slidenum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_pic1, container, false);
    }
}