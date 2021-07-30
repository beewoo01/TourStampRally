package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.DeabsItemBinding;
import com.sdin.tourstamprally.databinding.FragmentDeabsBinding;
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding;
import com.sdin.tourstamprally.model.Location;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.Tour_Spot2;
import com.sdin.tourstamprally.utill.DecoRation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeabsFragment extends BaseFragment {

    private FragmentDeabsBinding binding;
    private List<Tour_Spot2> list;
    private List<Location> location_list;
    private Map<Integer, String> location_map;
    private DeabsApdater deabsApdater;
    private LocaAdapter locaAdapter;

    public DeabsFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deabs, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView(){
        //binding.locationRe.setLayoutManager(new LinearLayoutManager(requireContext()));
        list = new ArrayList<>();
        getData();
    }

    private void sort(Location location){
        ArrayList arrayList = new ArrayList();
        if (location.getLocation_idx() == 0){
            arrayList.addAll(list);
        }else {
            for (int i = 0; i < list.size(); i++){
                if (location.getLocation_idx() == list.get(i).getTouristspot_location_location_idx()){
                    arrayList.add(list.get(i));
                }
            }
        }


        deabsApdater.changeList(arrayList);
    }

    private void getData(){

        apiService.getLocations().enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful()){
                    location_list = response.body();
                    location_list.add(0, new Location(0, "전체", "null", "null", "null"));
                    locaAdapter = new LocaAdapter(new ArrayList(location_list));
                    binding.locationRe.setAdapter(locaAdapter);

                    locaAdapter.itemOnclickLo = location -> {
                        sort(location);
                    };

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                t.printStackTrace();
            }
        });




        apiService.getSelect_interest(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot2>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot2>> call, Response<List<Tour_Spot2>> response) {
                if (response.isSuccessful()){
                    list = response.body();

                    location_map = new HashMap<>();
                    /*for (Tour_Spot2 tour_spot2 : list){
                        //location_map.put(tour_spot2.getTouristspot_location_location_idx(), tour_spot2.getTouristspot_location_());
                    }

                    Log.wtf("location_map", location_map.toString());
                    location_list= new ArrayList<>();
                    location_list.add(new Pair<>(99, "전체"));
                    for ( Map.Entry<Integer, String> entry : location_map.entrySet() ) {
                        Log.wtf("location_map111111111", entry.getValue());
                        location_list.add(new Pair<>(entry.getKey(), entry.getValue()));
                    }

                    locaAdapter = new LocaAdapter(new ArrayList(location_list));
                    binding.locationRe.setAdapter(locaAdapter);*/


                    deabsApdater =  new DeabsApdater(new ArrayList(list));
                    binding.deabsRe.setAdapter(deabsApdater);
                    binding.deabsRe.addItemDecoration(new DecoRation(2, 50, true));

                }else {
                    Log.wtf("getSelect_interest", "else");
                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot2>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface ItemOnclickLo{
        void onItemClick(Location location);
    }

    class LocaAdapter extends RecyclerView.Adapter<LocaAdapter.ViewHolder>{

        private ArrayList<Location> arrayList;

        private ItemOnclickLo itemOnclickLo;
        private int selectedItem = 0;

        public LocaAdapter(ArrayList arrayList) {
            this.arrayList = arrayList;
        }

        public void setOnClickListener(ItemOnclickLo itemOnclickLo){
            this.itemOnclickLo = itemOnclickLo;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.deabs_location_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(arrayList.get(position).getLocation_name());

            holder.textView.setOnClickListener( v -> {
                itemOnclickLo.onItemClick(arrayList.get(position));
                selectedItem = position;
                notifyDataSetChanged();
            });

            if (selectedItem == position){
                holder.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.mainColor));
            }else {
                holder.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.location_for_deabs);
            }
        }
    }



    class DeabsApdater extends RecyclerView.Adapter<DeabsApdater.ViewHolder>{

        private ArrayList<Tour_Spot2> arrayList;

        public DeabsApdater(ArrayList arrayList) {
            this.arrayList = arrayList;
        }

        public void changeList(ArrayList arrayList){
            this.arrayList = arrayList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(
                    DeabsItemBinding.inflate(
                            LayoutInflater.from(requireContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(requireContext()).load("http://zzipbbong.cafe24.com/imagefile/bsr/" + arrayList.get(position).getTouristspot_img()).into(holder.binding.tourImageImv);
            holder.binding.deabImv.setVisibility(View.VISIBLE);
            holder.binding.title.setText(arrayList.get(position).getTouristspot_name());
            holder.binding.tag.setText(arrayList.get(position).getTouristspot_tag());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends  RecyclerView.ViewHolder{
            DeabsItemBinding binding;
            public ViewHolder(@NonNull DeabsItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

            }
        }
    }



}