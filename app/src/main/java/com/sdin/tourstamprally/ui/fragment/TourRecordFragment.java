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

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.LocationAdapter;
import com.sdin.tourstamprally.databinding.FragmentTourRecordBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourRecordFragment extends BaseFragment {


    private FragmentTourRecordBinding binding;
    private ArrayList<Tour_Spot> list = new ArrayList<>();
    private Button[] buttons;
    public static final int POPULARFORM = 0;
    public static final int DATEFORM = 1;
    public static final int NEARFORM = 2;

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

        locationAdapter = new LocationAdapter(list, requireContext());
        binding.recyclerviewTourRecord.setAdapter(locationAdapter);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,requireContext().getResources().getStringArray(R.array.area));
        binding.spinnerTourRecord.setAdapter(spinnerAdapter);
        binding.spinnerTourRecord.setOnItemSelectedListener(selectedListener);
    }

    //어댑터 리스트 정렬
    public void setSort(View view){
        setSortBtnBackground(view.getId());
        list = locationAdapter.sortList(view.getId());
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

        apiService.getTour(Utils.User_Idx).enqueue(new Callback<Tour_Spot>() {
            @Override
            public void onResponse(Call<Tour_Spot> call, Response<Tour_Spot> response) {

            }

            @Override
            public void onFailure(Call<Tour_Spot> call, Throwable t) {

            }
        });

        Tour_Spot spot1, spot2, spot3, spot4, spot5, spot6;
        spot1 = new Tour_Spot(0, 0, "해동용궁사", "213.1231", "213.321421", "", "", true, "2021.01.01 15:11:15", "2021.01.01 15:11:15");
        spot2 = new Tour_Spot(1, 0, "광안리", "213.1231", "213.321421", "", "", false, "2021.01.02 11:11:13", "2021.01.02 11:11:13");
        spot3 = new Tour_Spot(2, 0, "해운대", "213.1231", "213.321421", "", "", false, "2021.01.03 15:21:31", "2021.01.03 15:21:31");
        spot4 = new Tour_Spot(3, 0, "용두산공원", "213.1231", "213.321421", "", "", true, "2021.01.04", "2021.01.04 16:41:00");
        spot5 = new Tour_Spot(4, 0, "감천문화마을", "213.1231", "213.321421", "", "", false, "2021.01.05", "2021.01.05 20:11:55");
        spot6 = new Tour_Spot(5, 0, "이기대공원", "213.1231", "213.321421", "", "", false, "2021.01.06", "2021.01.06 21:11:15");

        list.add(spot1);
        list.add(spot2);
        list.add(spot3);
        list.add(spot4);
        list.add(spot5);
        list.add(spot6);
    }


    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("getSelectedItem", parent.getSelectedItem().toString());
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