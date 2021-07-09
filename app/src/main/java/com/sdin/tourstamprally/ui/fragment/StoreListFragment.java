package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.adapter.LocationAdapter;
import com.sdin.tourstamprally.databinding.FragmentStoreListBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.utill.DecoRation;

import java.util.ArrayList;


public class StoreListFragment extends Fragment {


    private FragmentStoreListBinding binding;
    private ArrayList<Tour_Spot> list;

    public StoreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store_list, container, false);
        list = new ArrayList<>();
        initData();

        binding.recyclerviewTourRecord.setAdapter(new LocationAdapter(list, getContext()));
        //binding.recyclerviewTourRecord.addItemDecoration(new DecoRation(30,30));

        return binding.getRoot();
    }


    private void initData(){
        Tour_Spot spot1, spot2, spot3, spot4, spot5, spot6;
        spot1 = new Tour_Spot(0, 0, "선창횟집", "213.1231", "213.321421", "", "", true, "2021.07.21", "21.07.21");
        spot2 = new Tour_Spot(1, 0, "디에이블 관안점", "213.1231", "213.321421", "", "", false);
        spot3 = new Tour_Spot(2, 0, "다리집", "213.1231", "213.321421", "", "", false);
        spot4 = new Tour_Spot(3, 0, "초량밀면", "213.1231", "213.321421", "", "", true, "2021.07.21", "21.07.21");
        spot5 = new Tour_Spot(4, 0, "돼지국밥", "213.1231", "213.321421", "", "", false);
        spot6 = new Tour_Spot(5, 0, "원조전복죽", "213.1231", "213.321421", "", "", false);

        list.add(spot1);
        list.add(spot2);
        list.add(spot3);
        list.add(spot4);
        list.add(spot5);
        list.add(spot6);
    }
}