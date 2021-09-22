
package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
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
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.TourTagModel;
import com.sdin.tourstamprally.model.Tour_Spot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import kotlin.collections.AbstractMutableList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionGuidFragment extends BaseFragment {
    //길안내 관광지

    private static final String ARG_PARAM = "model";
    private FragmentDirectionGuidBinding binding;
    //private List<Tour_Spot> tourList;

    private DirectionGuid_Adapter adapter;
    private DirectionGuid_Tag_Adapter tagAdpater;
    /*private Map<Integer, Integer> location_Progress_Map;
    private Map<Integer, Integer> location_history_Map;
    private Map<Integer, Integer> particiMap;*/
    // 총 참여자

    private ArrayList<Location_four> paramArrayList;
    //private ArrayList<Pair<Location_four, TourTagModel>> locationArrayList;
    private ArrayList<TourTagModel> hashTagList = new ArrayList<>();

    public DirectionGuidFragment() {

    }

    public static DirectionGuidFragment newInstance(ArrayList<Location_four> param1) {
        DirectionGuidFragment fragment = new DirectionGuidFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramArrayList = getArguments().getParcelableArrayList(ARG_PARAM);
            /*Log.wtf("paramArrayList", paramArrayList.toString());
            Log.wtf("paramArrayListsize", String.valueOf(paramArrayList.size()));*/
        }
    }


    private void getData() {
        binding.directionGuidPgb.setVisibility(View.VISIBLE);

        apiService.getHashTag().enqueue(new Callback<List<TourTagModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<TourTagModel>> call, @NotNull Response<List<TourTagModel>> response) {
                if (response.isSuccessful()) {
                    List<TourTagModel> hashTagModels = response.body();

                    if (hashTagModels != null) {
                        //locationArrayList = new ArrayList<>();

                        for (TourTagModel model : hashTagModels) {
                            //Set<String> set = new HashSet<>();
                            String[] array = Arrays.stream(model.getHashTag().split("#")).map(String::trim).toArray(String[]::new);

                            Arrays.stream(array)
                                    .filter(s -> (s != null && s.length() > 0))
                                    .distinct()
                                    .forEach(data -> hashTagList.add(new TourTagModel(data, model.getLocation_idx())));


                            //Log.wtf("hashTagList set ToString", set.toString());
                            /*for (Location_four location_four : paramArrayList){
                                if (location_four.getLocation_idx() == model.getLocation_idx()){
                                    locationArrayList.add(new Pair<>(location_four, model));
                                }
                            }*/

                        }
                    }

                    setHashTag();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TourTagModel>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });


        //참여자 데이터 받아오는 부분
        /*apiService.getTourParticipants().enqueue(new Callback<List<Map<String, Integer>>>() {
            @Override
            public void onResponse(@NotNull Call<List<Map<String, Integer>>> call, @NotNull Response<List<Map<String, Integer>>> response) {
                if (response.isSuccessful()){
                    //Log.wtf("getTourParticipants", "1111111111");
                    List<Map<String, Integer>> list = response.body();
                    Log.wtf("list", list.toString());
                    particiMap = new HashMap<>();
                    for (Map hashMap : list){
                        Log.wtf("list", hashMap.toString());
                        if (hashMap.get("location_idx") != null && hashMap.get("cnt") != null){
                            particiMap.put((int) hashMap.get("location_idx"), (int) hashMap.get("cnt"));
                        }

                    }
                    getAllData();
                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Integer>>> call, Throwable t) {
                t.printStackTrace();
            }
        });*/
    }

    /*private void getAllData(){
        apiService.getTourSortHashTag(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(@NotNull Call<List<Tour_Spot>> call, @NotNull Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    tourList = new ArrayList<>();
                    tourList = response.body();

                    setTourSpotList();
                    setHashTag();

                }else {

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Tour_Spot>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    private void setTourSpotList() {

        adapter = new DirectionGuid_Adapter(
                paramArrayList,
                requireActivity()/*,
                location_Progress_Map,
                location_history_Map,
                particiMap*/);

        binding.locationRe.setAdapter(adapter);
        binding.locationRe.setHasFixedSize(true);
    }


    /*private void setTourSpotList(){

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

        Map<Integer, Tour_Spot> hashMap = new HashMap<>();
        for (Tour_Spot model : tourList){
            hashMap.put(model.getLocation_idx(), model);
        }

        Collection<Tour_Spot> collection = hashMap.values();
        ArrayList<Tour_Spot> arrayList = new ArrayList(collection);
        adapter = new DirectionGuid_Adapter(
                arrayList,
                requireActivity(),
                location_Progress_Map,
                location_history_Map,
                particiMap);

        binding.locationRe.setAdapter(adapter);
        binding.locationRe.setHasFixedSize(true);
    }*/


    private void setHashTag() {

        /*Set<String> hashSet = new HashSet<>();

        for (int i = 0; i < tourList.size(); i++){
            String hash = tourList.get(i).getTouristspot_tag();

            String[] array = Arrays.stream(hash.split("#")).map(String::trim).toArray(String[]::new);
            array = Arrays.stream(array)
                    .filter(s -> (s != null && s.length() > 0))
                    .toArray(String[]::new);

            hashSet.addAll(Arrays.asList(array));
        }

        ArrayList<String> list = new ArrayList<>(hashSet);*/


        tagAdpater = new DirectionGuid_Tag_Adapter(hashTagList);
        tagAdpater.setOnItemClickListener(param -> {
            //Log.wtf("setOnItemClickListener", "param = " + param +"1");
            search(param.getHashTag());
        });
        binding.tagRe.setAdapter(tagAdpater);
        binding.tagRe.setHasFixedSize(true);
        binding.directionGuidPgb.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direction_guid, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        SelectLocationAdapter spinnerAdapter = new SelectLocationAdapter(requireContext(), Arrays.asList(requireContext().getResources().getStringArray(R.array.area_direction)));
        binding.spinnerTourRecord.setAdapter(spinnerAdapter);
        binding.spinnerTourRecord.setOnItemSelectedListener(selectedListener);
        setTourSpotList();
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

        binding.searchBtn.setOnClickListener(v -> search(binding.searchEdt.getText().toString()));
    }

    private void search(String searchData) {

        ArrayList<Location_four> arrayList = new ArrayList<>();

        Set<Integer> locationIdxes = new HashSet<>();

        if (searchData.length() == 0) {
            //Log.wtf("searchData2", String.valueOf(searchData.length()));

            arrayList.addAll(paramArrayList);
            adapter.setList(arrayList);
            //tagAdpater.setList(arrayList);

        } else {

            /*for (TourTagModel model : hashTagList){
                for (Location_four location_model : paramArrayList){
                    if (model.getLocation_idx() == location_model.getLocation_idx()){
                        arrayList.add(location_model);
                    }
                }
            }*/

            //Log.wtf("searchData", searchData);

            for (TourTagModel tagModel : hashTagList) {
                if (tagModel.getHashTag().toLowerCase().contains(searchData)) {
                    locationIdxes.add(tagModel.getLocation_idx());
                    //Log.wtf("tagModel", tagModel.getHashTag());
                }
            }

            for (Location_four location_model : paramArrayList) {
                if (location_model.getLocation_name().toLowerCase().contains(searchData)) {
                    locationIdxes.add(location_model.getLocation_idx());
                    //Log.wtf("location_model", String.valueOf(location_model.getLocation_idx()));
                }
            }

            /*Observable<Location_four> source1 = Observable.fromIterable(paramArrayList);
            Observable<TourTagModel> source2 = Observable.fromIterable(hashTagList);

            Observable.zip(source1, source2, (Location_four n1, TourTagModel n2) ->
                    n1.getLocation_idx() == n2.getLocation_idx()).subscribe(data1 ->
                            Log.wtf("Observable!!", data1.toString())
            );*/



            for (Integer idx : locationIdxes) {
                for (Location_four location_model : paramArrayList) {
                    if (idx == location_model.getLocation_idx()) {
                        arrayList.add(location_model);
                    }
                }
            }

            /*Observable test = Observable.just(paramArrayList, hashTagList);

            Observable.merge(test).subscribe( it->

                    Log.wtf("merge", String.valueOf(it.toString()))
            );*/



            /*for (int i = 0; i < paramArrayList.size(); i++){
                if (paramArrayList.get(i).getLocation_name().toLowerCase().contains(searchData)){
                    arrayList.add(paramArrayList.get(i));
                }

                if (paramArrayList.get(i).getTouristspot_name().toLowerCase().contains(searchData)){
                    arrayList.add(paramArrayList.get(i));
                }

                if (paramArrayList.get(i).getTouristspot_tag().contains(searchData)){
                    arrayList.add(paramArrayList.get(i));
                }
            }*/
        }

        /*Map<Integer, Tour_Spot> hashMap = new HashMap<>();
        for (Tour_Spot model : arrayList){
            hashMap.put(model.getLocation_idx(), model);
        }


        Collection<Tour_Spot> collection = hashMap.values();
        arrayList = new ArrayList(collection);*/


        adapter.setList(arrayList);
        adapter.notifyDataSetChanged();
    }


    private final AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Log.d("getSelectedItem", parent.getSelectedItem().toString());

            if (paramArrayList != null) {
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