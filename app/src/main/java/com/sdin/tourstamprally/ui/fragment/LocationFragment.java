package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentLocationBinding;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private String location_name;

    public static LocationFragment newInstance(String location_name) {

        Bundle args = new Bundle();

        LocationFragment fragment = new LocationFragment();
        args.putString("", location_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false);
        return binding.getRoot();
    }


}