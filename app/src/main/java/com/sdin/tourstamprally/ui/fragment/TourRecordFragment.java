package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.adapter.LocationAdapter;
import com.sdin.tourstamprally.databinding.FragmentTourRecordBinding;
import com.sdin.tourstamprally.databinding.TourRecordItemReBinding;
import com.sdin.tourstamprally.utill.DecoRation;

import java.util.ArrayList;

public class TourRecordFragment extends Fragment {


    private FragmentTourRecordBinding binding;
    private ArrayList list;

    public TourRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_record, container, false);
        list = new ArrayList<>();
        initData();

        binding.recyclerviewTourRecord.setAdapter(new LocationAdapter(list, getContext()));
        binding.recyclerviewTourRecord.addItemDecoration(new DecoRation(getContext()));
        return binding.getRoot();
    }

    private void initData(){

    }
}