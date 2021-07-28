package com.sdin.tourstamprally.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.fragment.DirectionGuidFragment;
import com.sdin.tourstamprally.utill.GpsTracker;
import com.sdin.tourstamprally.utill.ItemOnClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.val;

public class DirectionGuid_Adapter extends RecyclerView.Adapter<DirectionGuid_Adapter.ViewHolder> {

    private ArrayList<Tour_Spot> list;
    private final Map<Integer, Integer> location_Progress_Map;
    private final Map<Integer, Integer> location_history_Map;
    private double latitude, longitude;
    private final Map<Integer, Integer> particiMap;
    // 총 참여자

    /*public interface ItemOnClick{
        void onClick(Tour_Spot tour_spot);
    } */

    private final ItemOnClick itemOnClick;
    private Context context;

    public DirectionGuid_Adapter(ArrayList<Tour_Spot> list, Activity activity,
                                 Map<Integer, Integer> location_Progress_Map,
                                 Map<Integer, Integer> location_history_Map,
                                 Map<Integer, Integer> particiMap) {
        this.list = list;
        this.itemOnClick = (ItemOnClick) activity;
        this.context = activity.getApplicationContext();
        this.location_history_Map = location_history_Map;
        this.location_Progress_Map = location_Progress_Map;
        this.particiMap = particiMap;
        getGps();
    }

    private void getGps(){
        GpsTracker gpsTracker = new GpsTracker(context);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
    }



    public void setList(ArrayList<Tour_Spot> list){
        //this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public int getListSize(){
        return list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                DirectionGuidLocationItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.locationSpotTxv.setText(list.get(position).getLocation_name());
        holder.binding.seekBarDirectionItem.setMax(100);
        double meter = Utils.distance(latitude, longitude, list.get(position).getTouristspot_latitude(), list.get(position).getTouristspot_longitude());
        String meterStr;
        if (meter < 1000){
            meterStr = (Math.floor(meter*10)/10.0) + "m";
        }else {
            meter = meter / 1000;
            meterStr = (Math.floor(meter*10)/10.0) + "km";
        }


        holder.binding.directionFromStempTxv.setText(meterStr);

        if (location_Progress_Map.get(list.get(position).getLocation_idx()) != null
                &&  location_history_Map.get(list.get(position).getLocation_idx()) != null) {


            int allContents = location_Progress_Map.get(list.get(position).getLocation_idx());
            int clearCount = location_history_Map.get(list.get(position).getLocation_idx());

            holder.binding.seekBarDirectionItem.setMax(allContents);
            holder.binding.seekBarDirectionItem.setProgress(clearCount);
            int allCountd = (int) ((double) clearCount /  (double) allContents * 100);
            holder.binding.seekPercentTxv.setText(allCountd + "%");
            Log.wtf("persentage", String.valueOf(allCountd));

        }else {
            holder.binding.seekBarDirectionItem.setProgress(0);
            holder.binding.seekPercentTxv.setText(0 + "%");
        }


        //참여자 데이터 적용
        if (particiMap.get(list.get(position).getLocation_idx()) != null){
            holder.binding.joinnerNumberTxv.setText(particiMap.get(list.get(position).getLocation_idx()));
        }


        //setProgress(holder, position);
    }

   /* private void setProgress(@NonNull ViewHolder holder, int position){

        if (location_Progress_Map.get(list.get(position).getLocation_idx()) != null
                &&  location_history_Map.get(list.get(position).getLocation_idx()) != null) {

            int allContents = location_Progress_Map.get(list.get(position).getLocation_idx());
            int clearCount = location_history_Map.get(list.get(position).getLocation_idx());

            holder.binding.seekBarDirectionItem.setMax(allContents);
            holder.binding.seekBarDirectionItem.setProgress(clearCount);
            int allCountd = (int) ((double) clearCount /  (double) allContents * 100);
            holder.binding.seekPercentTxv.setText(String.valueOf(allCountd) + "%");
            Log.wtf("persentage", String.valueOf(allCountd));

        }else {


        }
    }*/


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        DirectionGuidLocationItemBinding binding;
        public ViewHolder(@NonNull DirectionGuidLocationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.locationBg.setOnClickListener( v-> {
                itemOnClick.onItemClick(list.get(getAdapterPosition()));
            });
        }
    }
}
