package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.gson.Gson;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.LocationAdapter;
import com.sdin.tourstamprally.adapter.SelectLocationAdapter;
import com.sdin.tourstamprally.databinding.FragmentTourRecordBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourRecordFragment extends BaseFragment {


    private FragmentTourRecordBinding binding;
    private List<Tour_Spot> list = new ArrayList<>();
    private List<Tour_Spot> adpaterList = new ArrayList<>();
    private Button[] buttons;
    public static final int POPULARFORM = 0;
    public static final int DATEFORM = 1;
    public static final int NEARFORM = 2;
    private Button selectedCateGory;

    private LocationAdapter locationAdapter;

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
        binding.setFragment(this);

        initData();
        initView();


        return binding.getRoot();
    }


    private void initView(){
        buttons = new Button[]{binding.popularBtn , binding.recentBtn ,binding.nearBtn};

        /*locationAdapter = new LocationAdapter(list, requireContext());*/

        SelectLocationAdapter spinnerAdapter = new SelectLocationAdapter(requireContext(), Arrays.asList(requireContext().getResources().getStringArray(R.array.area)));
        //ArrayAdapter spinnerAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,requireContext().getResources().getStringArray(R.array.area));
        binding.spinnerTourRecord.setAdapter(spinnerAdapter);
        binding.spinnerTourRecord.setOnItemSelectedListener(selectedListener);
        selectedCateGory = binding.popularBtn;

    }

    //어댑터 리스트 정렬
    public void setSort(View view){
        selectedCateGory = (Button) view;
        setSortBtnBackground(view.getId());
        adpaterList = locationAdapter.sortList(view.getId());
        //list = locationAdapter.sortList(view.getId());
    }

    private void setSortBtnBackground(int id){

        for (Button button :buttons){
            if (button.getId() == id){
                button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_category_selected));
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            }else {
                button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_category));
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.mainColor));
            }
        }
    }


    private void initData(){
        /*user_idx = Utils.User_Idx*/
        locationAdapter = new LocationAdapter(adpaterList, requireContext());
        apiService.getTour(21).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    Log.d("?????", list.get(0).toString());
                    //locationAdapter = new LocationAdapter(list, requireContext());
                    binding.recyclerviewTourRecord.setAdapter(locationAdapter);
                    locationSort(binding.spinnerTourRecord.getSelectedItem().toString());
                    //setSort(binding.popularBtn);

                    /*Log.d("onResponse", "isSuccessful");
                    Log.d("LocationName!!", response.body().get(0).getLocation_name());*/

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void locationSort(String location){
        adpaterList.clear();
        for (Tour_Spot model : list){
            if (model.getLocation_name().equals(location)){
                adpaterList.add(model);
                Log.d("locationSort" , model.getLocation_name());
            }
        }
        locationAdapter.locaitonSort(adpaterList, selectedCateGory.getId());
        setSortBtnBackground(selectedCateGory.getId());
        //locationAdapter = new LocationAdapter(adpaterList, requireContext());
        //setSort(selectedCateGory);
    }


    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("getSelectedItem", parent.getSelectedItem().toString());

            locationSort(parent.getSelectedItem().toString());
            switch (parent.getSelectedItem().toString()){
                case "동구" :
                    Log.d("getSelectedItem", "동구옴");

                    break;

                case "영도구" :

                    Log.d("getSelectedItem", "영도구옴");
                    break;

                case "부산진구" :

                    Log.d("getSelectedItem", "부산진구옴");
                    break;

                case "남구" :

                    Log.d("getSelectedItem", "남구옴");
                    break;

                case "북구" :

                    Log.d("getSelectedItem", "북구옴");
                    break;

                case "해운대구" :

                    Log.d("getSelectedItem", "해운대구옴");
                    break;

                case "사하구" :

                    Log.d("getSelectedItem", "사하구옴");
                    break;

                case "금정구" :

                    Log.d("getSelectedItem", "금정구옴");
                    break;

                case "강서구" :

                    Log.d("getSelectedItem", "강서구옴");
                    break;

                case "연제구" :

                    Log.d("getSelectedItem", "연재구옴");
                    break;


                case "수영구" :

                    Log.d("getSelectedItem", "수영구옴");
                    break;

                case "사상구" :

                    Log.d("getSelectedItem", "사상구옴");
                    break;


                case "기장군" :

                    Log.d("getSelectedItem", "기장군옴");
                    break;


                case "기타" :
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}