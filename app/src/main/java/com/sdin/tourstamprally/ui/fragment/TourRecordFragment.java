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
import com.sdin.tourstamprally.utill.GpsTracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourRecordFragment extends BaseFragment {


    private FragmentTourRecordBinding binding;
    private List<Tour_Spot> list = new ArrayList<>();
    //private List<Tour_Spot> adpaterList = new ArrayList<>();
    private Button[] buttons;
    public static final int POPULARFORM = 0;
    public static final int DATEFORM = 1;
    public static final int NEARFORM = 2;
    private Button selectedCateGory;
    private GpsTracker gpsTracker;

    private LocationAdapter locationAdapter;

    private ArrayList<Tour_Spot> Adapterlist = new ArrayList();

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
       // binding.setFragment(this);

        initData();
        initView();


        return binding.getRoot();
    }


    private void initView(){
        buttons = new Button[]{binding.popularBtn , binding.recentBtn ,binding.nearBtn};
        gpsTracker = new GpsTracker(requireContext());


        SelectLocationAdapter spinnerAdapter = new SelectLocationAdapter(requireContext(), Arrays.asList(requireContext().getResources().getStringArray(R.array.area)));
        binding.spinnerTourRecord.setAdapter(spinnerAdapter);
        binding.spinnerTourRecord.setOnItemSelectedListener(selectedListener);
        selectedCateGory = binding.popularBtn;



    }



    //어댑터 리스트 정렬
    public void setSort(View view){
        selectedCateGory = (Button) view;
        setSortBtnBackground(view.getId());
        Adapterlist = new ArrayList<>(sortList(view.getId()));
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


        /*apiService.getTourLocation_for_spot().enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {

            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {

            }
        });*/

        apiService.getTour(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    HashMap<Integer, Tour_Spot> hashMap = new HashMap<>();
                    for (int i = 0; i < list.size(); i++){
                        hashMap.put(list.get(i).getTouristspot_idx(), list.get(i));
                    }
                    Adapterlist = new ArrayList(hashMap.values());

                    locationAdapter = new LocationAdapter(Adapterlist, requireContext());


                    //locationSort(binding.spinnerTourRecord.getSelectedItem().toString());
                    Adapterlist = locationSort2(binding.spinnerTourRecord.getSelectedItem().toString());
                    binding.recyclerviewTourRecord.setAdapter(locationAdapter);

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private ArrayList locationSort2(String location){
        ArrayList<Tour_Spot> arrayList = new ArrayList();
        for (int i = 0; i < Adapterlist.size(); i++){
            if (Adapterlist.get(i).getLocation_name().equals(location)){
                arrayList.add(Adapterlist.get(i));
            }
        }

        Map<Integer, Tour_Spot> map = new HashMap<>();
        for (int i = 0; i < arrayList.size(); i++){
            map.put(arrayList.get(i).getTouristspot_idx(), arrayList.get(i));
        }

        return new ArrayList(map.values());
    }

    private void locationSort(String location){
        Adapterlist.clear();
        for (Tour_Spot model : list){
            if (model.getLocation_name().equals(location)){
                Adapterlist.add(model);
                Log.d("locationSort" , model.getLocation_name());
            }
        }

        Map<Integer, Tour_Spot> hashMap = new HashMap<>();
        for (int i = 0; i < Adapterlist.size(); i++){
            hashMap.put(Adapterlist.get(i).getTouristspot_idx(), list.get(i));
        }

        Adapterlist.clear();
        for ( Map.Entry<Integer, Tour_Spot> entry : hashMap.entrySet() ) {
            Adapterlist.add(entry.getValue());
            Log.wtf("locationSort" , String.valueOf(entry.getValue()));
        }

        /*HashMap<Integer, Tour_Spot> hashMap = new HashMap<>();
        for (int i = 0; i < Adapterlist.size(); i++){
            hashMap.put(Adapterlist.get(i).getTouristspot_idx(), list.get(i));
            Log.d("locationSort222" , Adapterlist.get(i).getLocation_name());
            Log.d("locationSort222" , String.valueOf(Adapterlist.get(i).getTouristspot_idx()));
            Log.d("locationSort222" , String.valueOf(Adapterlist.get(i).getTouristspot_name()));
        }
        Adapterlist = new ArrayList(hashMap.values());*/


        if (locationAdapter != null){
            locationAdapter.locaitonSort(Adapterlist, selectedCateGory.getId());
            setSortBtnBackground(selectedCateGory.getId());
        }


        //locationAdapter = new LocationAdapter(adpaterList, requireContext());
        //setSort(selectedCateGory);
    }

    public List<Tour_Spot> sortList(int form){

        switch (form){
            case R.id.popular_btn :
                Log.d(TAG, "popular_btn");
                list.sort((o1, o2) -> {

                    if (o1.getTouristspot_checkin_count() == o2.getTouristspot_checkin_count()) return 0;
                    else if (o1.getTouristspot_checkin_count() < o2.getTouristspot_checkin_count()) return 1;
                    else return -1;
                });

                break;

            case R.id.recent_btn :
                Log.d(TAG, "recent_btn");
                list.sort((o1, o2) -> {




                    try {
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date to1 = transFormat.parse(o1.getTouristhistory_updatetime());
                        Date to2 = transFormat.parse(o2.getTouristhistory_updatetime());
                        long time1 = to1.getTime();
                        long time2 = to2.getTime();

                        if (time1 == time2) return 0;
                        else if (time1 < time2) return 1;
                        else return -1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }

                    /*long sortDate1 = o1.getTouristhistory_updatetime();
                    long sortDate2 = o2.getTouristhistory_updatetime();*/



                });
                break;

            case R.id.near_btn :
                Log.d(TAG, "near_btn");
                list.sort((o1, o2) -> {

                    double o1_distance = distance(o1.getTouristspot_latitude(), o1.getTouristspot_longitude());
                    double o2_distance = distance(o2.getTouristspot_latitude(), o2.getTouristspot_longitude());

                    if (o1_distance == o2_distance) return 0;
                    else if (o1_distance > o2_distance) return 1;
                    else return -1;

                });



                break;
        }



        locationAdapter.notifyDataSetChanged();

        return list;
    }

    private double distance(double latitude, double longitude){
        double userLatitude = gpsTracker.getLatitude();
        double userLongitude = gpsTracker.getLongitude();

        return Utils.distance(latitude, longitude, userLatitude, userLongitude);
    }


    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("getSelectedItem", parent.getSelectedItem().toString());

            locationSort2(parent.getSelectedItem().toString());
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