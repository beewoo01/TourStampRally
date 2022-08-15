package com.sdin.tourstamprally;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPop3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPop3Fragment extends Fragment {

    public static MainPop3Fragment newInstance(int num) {
        MainPop3Fragment fragment = new MainPop3Fragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            int number = getArguments().getInt("num");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_pop3, container, false);
    }
}