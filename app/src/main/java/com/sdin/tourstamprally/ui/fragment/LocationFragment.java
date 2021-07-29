package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentLocationBinding;
import com.sdin.tourstamprally.databinding.LocationReItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpot;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.GuidDialog;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.ItemOnClickAb;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends BaseFragment {

    private FragmentLocationBinding binding;
    private String location_name;
    private Tour_Spot tour_spot;
    private List<Tour_Spot> list;
    private LocationFragAdapter adapter;
    private Map<Integer, Integer> spot_poinMap;
    private Map<Integer, Integer> spot_HistoryMap;

    public static LocationFragment newInstance(Tour_Spot tour_spot) {

        Bundle args = new Bundle();

        LocationFragment fragment = new LocationFragment();
        args.putSerializable("model", tour_spot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tour_spot = (Tour_Spot) getArguments().getSerializable("model");
            location_name = tour_spot.getLocation_name();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false);
        binding.locationTxv.setText(location_name);
        getData();
        return binding.getRoot();
    }

    private void getData(){
        binding.locationPgb.setVisibility(View.VISIBLE);
        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("user_idx", Utils.User_Idx);
        dataMap.put("location_idx", tour_spot.getLocation_idx());
        apiService.getTourLocation_for_spot(dataMap).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                list = response.body();
                setRecylcerviewAdapter();

            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void setRecylcerviewAdapter(){
        Map<Integer, Tour_Spot> map = new HashMap<>();
        for (Tour_Spot tour_spot : list){
            map.put(tour_spot.getTouristspot_idx(), tour_spot);
        }

        Gson gson = new Gson();
        String gStr = gson.toJson(list);
        Type listType = new TypeToken<ArrayList<TouristSpotPoint>>(){}.getType();
        ArrayList<TouristSpotPoint> arrayList = gson.fromJson(gStr, listType);


        spot_poinMap = new HashMap<>();
        spot_HistoryMap = new HashMap<>();
        for (TouristSpotPoint touristSpotPoint : arrayList){
            spot_poinMap.put(touristSpotPoint.getTouristspotpoint_touristspot_idx(),
                    spot_poinMap.get(touristSpotPoint.getTouristspotpoint_touristspot_idx()) == null ?
                            1 : spot_poinMap.get(touristSpotPoint.getTouristspotpoint_touristspot_idx()) +1);


            if (touristSpotPoint.getTouristhistory_idx() != null){
                spot_HistoryMap.put(Integer.valueOf(touristSpotPoint.getTouristspotpoint_touristspot_idx()),
                        spot_HistoryMap.get(touristSpotPoint.getTouristspotpoint_touristspot_idx()) == null?
                                1 : spot_HistoryMap.get(touristSpotPoint.getTouristspotpoint_touristspot_idx()) +1);
            }

        }

        adapter = new LocationFragAdapter(
                map.values().stream().collect(Collectors.toCollection(ArrayList::new)),
                spot_poinMap,
                spot_HistoryMap
        );
        binding.recyclerviewLocationRe.setAdapter(adapter);
        binding.recyclerviewLocationRe.setLayoutManager(new LinearLayoutManager(requireContext()));
        setProgress();
    }

    private void setProgress(){

        int clear = 0;

        for(int key : spot_poinMap.keySet()) {
            int value = spot_poinMap.get(key);
            if (spot_HistoryMap.get(key) != null){
                int clearCount = spot_HistoryMap.get(key);
                if (value == clearCount){
                    clear++;
                }
            }
        }

        double percent = spot_poinMap.size() * 100 / 100;
        binding.seekBarLocation.setMax(100);
        binding.seekBarLocation.setProgress( (int)((double) clear /  (double) spot_poinMap.size() * 100.0), true);

        binding.seekPercentTxv.setText(((int) (clear / percent * 100)) +"%");
        binding.locationPgb.setVisibility(View.GONE);

    }


    class LocationFragAdapter extends RecyclerView.Adapter<LocationFragAdapter.ViewHolder>{

        private ArrayList<Tour_Spot> arrayList;
        private Map<Integer, Integer> spotPoint_map;
        private Map<Integer, Integer> history_map;
        private ItemOnClick listener;
        private Tour_Spot send_model;



        public LocationFragAdapter(ArrayList<Tour_Spot> arrayList, Map<Integer, Integer> spotPoint_map,
                                   Map<Integer, Integer> history_map) {
            this.arrayList = arrayList;
            this.spotPoint_map = spotPoint_map;
            this.history_map = history_map;
            listener = (MainActivity) requireActivity();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LocationReItemBinding.inflate(
                            LayoutInflater.from(requireContext()), parent,  false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.topLine.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE );
            holder.binding.bottomLine.setVisibility(position == arrayList.size() -1 ? View.GONE : View.VISIBLE);
            holder.binding.bottomLayout.setVisibility(position == arrayList.size() -1 ? View.GONE : View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(position % 2 == 0 ? R.drawable.icon_deep_blue : R.drawable.icon_sky_blue).into(holder.binding.locationImv);
            Glide.with(holder.itemView.getContext()).load(setProgress(position) ? R.drawable.mainlogo : R.drawable.logo_gray).into(holder.binding.logoImv);
            holder.binding.spotName.setText(arrayList.get(position).getTouristspot_name());
            holder.binding.explanTxv.setText(arrayList.get(position).getTouristspot_explan());

        }

        private boolean setProgress(int position){

            if (spotPoint_map.get(Integer.parseInt(arrayList.get(position).getTouristspotpoint_touristspot_idx())) != null
                    &&  history_map.get(Integer.parseInt(arrayList.get(position).getTouristspotpoint_touristspot_idx())) != null) {

                int allContents = spotPoint_map.get(Integer.parseInt(arrayList.get(position).getTouristspotpoint_touristspot_idx()));
                int clearCount = history_map.get(Integer.parseInt(arrayList.get(position).getTouristspotpoint_touristspot_idx()));
                return allContents - clearCount == 0;

            }else {

                return false;

            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private LocationReItemBinding binding;
            public ViewHolder(@NonNull LocationReItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                binding.topLayout.setOnClickListener( v -> {
                    send_model = arrayList.get(getAdapterPosition());
                    GuidDialog guidDialog = new GuidDialog(requireContext());
                    guidDialog.show();
                    guidDialog.setClickListener(itemOnClick);
                });
            }
        }


        private ItemOnClick itemOnClick = new ItemOnClickAb() {
            @Override
            public void ItemGuid(int position) {
                Log.d("dialog Onclick Listener", String.valueOf(position));
                if (position == 1 || position == 2){
                    listener.ItemGuid(position);
                }else {
                    listener.ItemGuid(position, send_model);
                }
            }
        };
    }


}