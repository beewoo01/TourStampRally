package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.LocationAdapter;
import com.sdin.tourstamprally.adapter.SelectLocationAdapter;
import com.sdin.tourstamprally.databinding.FragmentTourRecordBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.utill.GpsTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.stream.Collectors.toList;

public class BlankFragment extends BaseFragment {

    private FragmentTourRecordBinding binding;
    private Button[] buttons;
    private GpsTracker gpsTracker;
    private LocationAdapter locationAdapter;
    private Button selectedCateGory;
    private List<Tour_Spot> list;
    private ArrayList<Tour_Spot> arrayList;

    public BlankFragment() {
        // Required empty public constructor
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_record, container, false);
        //binding.setFragment(this);
        getData();
        initData();
        return binding.getRoot();
    }

    private void initData() {


        list = new ArrayList<>();

        apiService.getTour(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    HashMap<Integer, Tour_Spot> hashMap = new HashMap<>();

                    list = list.stream()
                            .sorted(Comparator.comparing(Tour_Spot::getTouristhistory_idx, Comparator.nullsFirst(Comparator.naturalOrder())))
                            .collect(toList());

                    for (int i = 0; i < list.size(); i++) {
                        hashMap.put(list.get(i).getTouristspot_idx(), list.get(i));
                    }


                    arrayList = new ArrayList(hashMap.values());

                    locationAdapter = new LocationAdapter(arrayList, requireContext());

                    binding.recyclerviewTourRecord.setAdapter(locationAdapter);
                    binding.spinnerTourRecord.setOnItemSelectedListener(selectedListener);
                    locationSort(binding.spinnerTourRecord.getSelectedItem().toString());
                    selectedCateGory = binding.popularBtn;

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getData() {
        buttons = new Button[]{binding.popularBtn, binding.recentBtn, binding.nearBtn};
        gpsTracker = new GpsTracker(requireContext());

        SelectLocationAdapter spinnerAdapter = new SelectLocationAdapter(requireContext(), Arrays.asList(requireContext().getResources().getStringArray(R.array.area)));
        binding.spinnerTourRecord.setAdapter(spinnerAdapter);

        selectedCateGory = binding.popularBtn;
    }

    private void locationSort(String location) {

        //spinner 클릭시 오는 부분

        //Log.wtf("arrayList.size()", String.valueOf(arrayList.size()));
        ArrayList<Tour_Spot> samplArray = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //Log.wtf("map for i 라라라 " + i, String.valueOf(arrayList.get(i).getTouristspot_idx()));
            if (list.get(i).getLocation_name().equals(location)) {
                samplArray.add(list.get(i));
            }
        }

        Map<Integer, Tour_Spot> map = new HashMap<>();
        for (int i = 0; i < samplArray.size(); i++) {
            map.put(samplArray.get(i).getTouristspot_idx(), samplArray.get(i));
            //Log.wtf("map for i " + i, String.valueOf(arrayList.get(i).getTouristspot_idx()));
        }

        locationAdapter.locationlistSet(new ArrayList<>(map.values()));
        setSort(selectedCateGory);
    }

    private void myList() {
        //최신 획득순

    }


    //어댑터 리스트 정렬
    public void setSort(View view) {
        selectedCateGory = (Button) view;
        setSortBtnBackground(view.getId());
        sortList(view.getId());
        //Adapterlist = new ArrayList<>(sortList(view.getId()));
        //list = locationAdapter.sortList(view.getId());
    }

    private void setSortBtnBackground(int id) {

        for (Button button : buttons) {
            if (button.getId() == id) {
                button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_category_selected));
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            } else {
                button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_category));
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.mainColor));
            }
        }
    }


    public void sortList(int form) {
        arrayList = locationAdapter.getList();

        switch (form) {
            case R.id.popular_btn:
                Log.d(TAG, "popular_btn");
                arrayList.sort((o1, o2) -> {

                    if (o1.getTouristspot_checkin_count() == o2.getTouristspot_checkin_count())
                        return 0;
                    else if (o1.getTouristspot_checkin_count() < o2.getTouristspot_checkin_count())
                        return 1;
                    else return -1;
                });

                break;

            case R.id.recent_btn:
                Log.d(TAG, "recent_btn");
                //myList();

                arrayList = (ArrayList<Tour_Spot>) arrayList.stream()
                        .sorted(Comparator.comparing(Tour_Spot::getTouristhistory_idx, Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(toList());
                break;

            case R.id.near_btn:
                Log.d(TAG, "near_btn");
                arrayList.sort((o1, o2) -> {

                    double o1_distance = distance(o1.getTouristspot_latitude(), o1.getTouristspot_longitude());
                    double o2_distance = distance(o2.getTouristspot_latitude(), o2.getTouristspot_longitude());

                    if (o1_distance == o2_distance) return 0;
                    else if (o1_distance > o2_distance) return 1;
                    else return -1;

                });


                break;
        }


        locationAdapter.locationlistSet(arrayList);

    }


    private double distance(double latitude, double longitude) {
        double userLatitude = gpsTracker.getLatitude();
        double userLongitude = gpsTracker.getLongitude();

        return Utils.distance(latitude, longitude, userLatitude, userLongitude);
    }


    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("getSelectedItem", parent.getSelectedItem().toString());

            locationSort(parent.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}