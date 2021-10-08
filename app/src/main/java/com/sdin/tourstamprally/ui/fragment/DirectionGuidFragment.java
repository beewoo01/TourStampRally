
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
    // 총 참여자

    private ArrayList<Location_four> paramArrayList;
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
                try {
                    if (response.isSuccessful()) {
                        List<TourTagModel> hashTagModels = response.body();

                        if (hashTagModels != null) {

                            for (TourTagModel model : hashTagModels) {
                                if (model == null){
                                    Log.wtf("TourTagModel", "NULL!!" );
                                }
                                if (model != null && model.getHashTag() != null){
                                    String[] array = Arrays.stream(model.getHashTag().split("#")).map(String::trim).toArray(String[]::new);
                                    Arrays.stream(array)
                                            .filter(s -> (s != null && s.length() > 0))
                                            .distinct()
                                            .forEach(data -> hashTagList.add(new TourTagModel(data, model.getLocation_idx())));
                                }


                            }
                        }

                        setHashTag();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<TourTagModel>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });


    }

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


    private void setHashTag() {

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


            arrayList.addAll(paramArrayList);
            adapter.setList(arrayList);

        } else {

            for (TourTagModel tagModel : hashTagList) {
                if (tagModel.getHashTag().toLowerCase().contains(searchData)) {
                    locationIdxes.add(tagModel.getLocation_idx());
                }
            }

            for (Location_four location_model : paramArrayList) {
                if (location_model.getLocation_name().toLowerCase().equals(searchData)) {
                    locationIdxes.add(location_model.getLocation_idx());
                }
            }


            for (Integer idx : locationIdxes) {
                for (Location_four location_model : paramArrayList) {
                    if (idx == location_model.getLocation_idx()) {
                        arrayList.add(location_model);
                    }
                }
            }

        }



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