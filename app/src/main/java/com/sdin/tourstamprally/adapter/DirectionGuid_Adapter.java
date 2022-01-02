package com.sdin.tourstamprally.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.fragment.DirectionGuidFragment;
import com.sdin.tourstamprally.utill.GpsTracker;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.v2.RecyclerViewListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DirectionGuid_Adapter extends RecyclerView.Adapter<DirectionGuid_Adapter.ViewHolder> {



    private ArrayList<Location_four> list;
    /*private final Map<Integer, Integer> location_Progress_Map;
    private final Map<Integer, Integer> location_history_Map;*/
    private double latitude, longitude;
    //private final Map<Integer, Integer> particiMap;
    // 총 참여자

    /*public interface ItemOnClick{
        void onClick(Tour_Spot tour_spot);
    } */

    //private final ItemOnClick itemOnClick;
    private final RecyclerViewListener recyclerViewListener;
    private final Context context;

    public DirectionGuid_Adapter(ArrayList<Location_four> list, RecyclerViewListener recyclerViewListener, Context context
            /*Activity activity*/) {
        this.list = list;
        this.recyclerViewListener = recyclerViewListener;
        this.context = context;
        /*this.itemOnClick = (ItemOnClick) activity;
        this.context = activity.getApplicationContext();*/
        getGps();
    }

    private void getGps(){
        GpsTracker gpsTracker = new GpsTracker(context);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
    }



    public void setList(ArrayList<Location_four> list){
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

        if (!TextUtils.isEmpty(list.get(position).getLocation_img())){
            Glide.with(context).load("http://coratest.kr/imagefile/bsr/" + list.get(position).getLocation_img()).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    holder.binding.locationBg.setBackground(resource);

                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }

        if (list.get(position).getLocation_idx() > 0){
            int allContents = list.get(position).getAllPointCount();
            int clearCount = list.get(position).getMyHistoryCount();
            int allCountd = (int) ((double) clearCount /  (double) allContents * 100);
            holder.binding.seekBarDirectionItem.setMax(allContents);
            holder.binding.seekBarDirectionItem.setProgress(clearCount);
            holder.binding.seekPercentTxv.setText(allCountd + "%");
        }



        //참여자 데이터 적용
        holder.binding.joinnerNumberTxv.setText(String.valueOf(list.get(position).getPopular()));



    }


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
                //itemOnClick.onItemClick(list.get(getAbsoluteAdapterPosition()));
                recyclerViewListener.onLocationItemClick(list.get(getAbsoluteAdapterPosition()));
            });
        }
    }
}
