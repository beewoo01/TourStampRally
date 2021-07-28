package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.DirectionGuid_Adapter;
import com.sdin.tourstamprally.adapter.DirectionGuid_Tag_Adapter;
import com.sdin.tourstamprally.adapter.SelectLocationAdapter;
import com.sdin.tourstamprally.databinding.FragmentDirectionGuidBinding;
import com.sdin.tourstamprally.model.HashTagModel;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionGuidFragment extends BaseFragment {
    //길안내 관광지
    private FragmentDirectionGuidBinding binding;
    private List<Tour_Spot> tourList;
    private List<HashTagModel> hashTagModelList;
    //private RallyRecyclerviewAdapter adapter;
    private DirectionGuid_Adapter adapter;
    private DirectionGuid_Tag_Adapter tagAdpater;
    private Map<Integer, Integer> location_Progress_Map;
    private Map<Integer, Integer> location_history_Map;

    public DirectionGuidFragment() {

    }

    public static DirectionGuidFragment newInstance() {
        DirectionGuidFragment fragment = new DirectionGuidFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private void getData(){
        binding.directionGuidPgb.setVisibility(View.VISIBLE);
        apiService.getTourSortHashTag(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    tourList = new ArrayList<>();
                    tourList = response.body();

                    setProgress();
                    setTourSpotList();
                    setHashTag();

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setProgress(){

    }

    private void setTourSpotList(){

        location_Progress_Map = new HashMap<>();
        location_history_Map = new HashMap<>();

        for (Tour_Spot tour_spot : tourList) {

            location_Progress_Map.put(tour_spot.getLocation_idx(),
                    location_Progress_Map.get(tour_spot.getLocation_idx()) == null ?
                            1 : location_Progress_Map.get(tour_spot.getLocation_idx()) +1);


            if (tour_spot.getTouristhistory_idx() != null){
                location_history_Map.put(tour_spot.getLocation_idx(),
                        location_history_Map.get(tour_spot.getLocation_idx()) == null?
                                1 : location_history_Map.get(tour_spot.getLocation_idx()) +1);
            }

        }

        for(int key : location_Progress_Map.keySet()) {
            int value = location_Progress_Map.get(key);
            System.out.println("spot_poinMap , " + key + " : " + value);

        }

        for(int key : location_history_Map.keySet()) {
            int value = location_history_Map.get(key);
            System.out.println("spot_HistoryMap , " + key + " : " + value);

        }

        Map<Integer, Tour_Spot> hashMap = new HashMap<>();
        for (Tour_Spot model : tourList){
            hashMap.put(model.getLocation_idx(), model);
        }

        Collection<Tour_Spot> collection = hashMap.values();
        ArrayList<Tour_Spot> arrayList = new ArrayList(collection);
        adapter = new DirectionGuid_Adapter(arrayList, requireActivity(), location_Progress_Map, location_history_Map);
        binding.locationRe.setAdapter(adapter);
        binding.locationRe.setHasFixedSize(true);
    }



    private void setHashTag() {

        Set<String> hashSet = new HashSet<>();

        for (int i = 0; i < tourList.size(); i++){
            String hash = tourList.get(i).getTouristspot_tag();
            String[] array = Arrays.stream(hash.split("#")).map(String::trim).toArray(String[]::new);
            array = Arrays.stream(array)
                    .filter(s -> (s != null && s.length() > 0))
                    .toArray(String[]::new);

            hashSet.addAll(Arrays.asList(array));
        }

        ArrayList<String> list = new ArrayList<>(hashSet);


        tagAdpater = new DirectionGuid_Tag_Adapter(list);
        tagAdpater.setOnItemClickListener(param -> {
            Log.wtf("setOnItemClickListener", "param = " + param +"1");
            search(param);
        });
        binding.tagRe.setAdapter(tagAdpater);
        binding.tagRe.setHasFixedSize(true);
        binding.directionGuidPgb.setVisibility(View.GONE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direction_guid, container, false);
        SelectLocationAdapter spinnerAdapter = new SelectLocationAdapter(requireContext(), Arrays.asList(requireContext().getResources().getStringArray(R.array.area_direction)));
        binding.spinnerTourRecord.setAdapter(spinnerAdapter);
        binding.spinnerTourRecord.setOnItemSelectedListener(selectedListener);

        getData();
        binding.searchEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(binding.searchEdt.getText().toString());
            }

        });

        binding.searchBtn.setOnClickListener(v -> {
            search(binding.searchEdt.getText().toString());
        });

        return binding.getRoot();
    }

    private void search(String searchData){

        ArrayList<Tour_Spot> arrayList = new ArrayList<>();
        if (searchData.length() == 0){
            Log.wtf("searchData2", String.valueOf(searchData.length()));

            arrayList.addAll(tourList);
            adapter.setList(arrayList);
            //tagAdpater.setList(arrayList);

        }else {

            for (int i = 0; i < tourList.size(); i++){
                if (tourList.get(i).getLocation_name().toLowerCase().contains(searchData)){
                    arrayList.add(tourList.get(i));
                }

                if (tourList.get(i).getTouristspot_name().toLowerCase().contains(searchData)){
                    arrayList.add(tourList.get(i));
                }

                if (tourList.get(i).getTouristspot_tag().contains(searchData)){
                    arrayList.add(tourList.get(i));
                }
            }
        }

        Map<Integer, Tour_Spot> hashMap = new HashMap<>();
        for (Tour_Spot model : arrayList){
            hashMap.put(model.getLocation_idx(), model);
        }


        Collection<Tour_Spot> collection = hashMap.values();
        arrayList = new ArrayList(collection);


        adapter.setList(arrayList);
        adapter.notifyDataSetChanged();
    }


    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("getSelectedItem", parent.getSelectedItem().toString());

            if (tourList != null){
                String searchData = parent.getSelectedItem().toString();
                if (searchData.equals("전체"))
                    searchData = "";
                search(searchData);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}