package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.DirectionGuid_Adapter;
import com.sdin.tourstamprally.adapter.DirectionGuid_Tag_Adapter;
import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.databinding.FragmentDirectionGuidBinding;
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding;
import com.sdin.tourstamprally.model.HashTagModel;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        apiService.getTour(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    tourList = new ArrayList<>();
                    tourList = response.body();
                    ArrayList<Tour_Spot> arrayList = new ArrayList<>();
                    arrayList.addAll(tourList);
                    adapter = new DirectionGuid_Adapter(arrayList);

                    hashTagModelList = new ArrayList<>();
                    for (int i = 0; i < tourList.size(); i++){
                        String hash = tourList.get(i).getTouristspot_tag();
                        //if (tourList.get(i).getTouristspot_idx() )
                        String[] array = hash.split("#");
                        for (int j = 0; j < array.length; j++){
                            if (!TextUtils.isEmpty(array[j].trim())){
                                hashTagModelList.add(
                                        new HashTagModel(
                                                tourList.get(i).getTouristspot_location_location_idx(), tourList.get(i).getTouristspot_idx(), "#"+array[j]
                                        )
                                );
                            }
                            //Log.wtf("ArrayString: ", array[j]);

                        }

                    }
                    ArrayList<HashTagModel> tagModels = new ArrayList<>();
                    tagModels.addAll(hashTagModelList);
                    tagAdpater = new DirectionGuid_Tag_Adapter(tagModels);
                    binding.locationRe.setAdapter(adapter);
                    binding.locationRe.setHasFixedSize(true);
                    binding.tagRe.setAdapter(tagAdpater);
                    binding.tagRe.setHasFixedSize(true);

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direction_guid, container, false);

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
            }
        }
        adapter.setList(arrayList);
        //tagAdpater.setList(arrayList);
        adapter.notifyDataSetChanged();
        //tagAdpater.notifyDataSetChanged();
    }


    /*private class TagAdpater extends RecyclerView.Adapter<TagAdpater.Viewholder>{

        private List<Tour_Spot> list;

        public TagAdpater(List<Tour_Spot> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Viewholder(
                    DirectionGuidTagItemBinding.inflate(
                            LayoutInflater.from(requireContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            holder.binding.tagItemTxv.setText(list.get(position).getTouristspot_tag());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder{
            DirectionGuidTagItemBinding binding;

            public Viewholder(@NonNull DirectionGuidTagItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }*/



}