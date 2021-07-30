package com.sdin.tourstamprally.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.TourRecordItemReBinding;
import com.sdin.tourstamprally.model.Location;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.utill.GpsTracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sdin.tourstamprally.ui.fragment.TourRecordFragment.DATEFORM;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Tour_Spot> list;
    public static final String TAG = LocationAdapter.class.getSimpleName();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");



    public LocationAdapter(ArrayList<Tour_Spot> list, Context context){
        this.list = list;

        sdf = new SimpleDateFormat("yyyy.MM.dd");

    }

    public void locationlistSet(ArrayList<Tour_Spot> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Tour_Spot> getList(){
        return list;
    }

    public List<Tour_Spot> locaitonSort(ArrayList<Tour_Spot> list, int category){
        this.list = list;
        /*for (Tour_Spot model : this.list){
            Log.d("locaitonSort!!!!!", model.getLocation_name());
            Log.d("locaitonSort!!!!!", model.getTouristspot_name());
        }*/
        notifyDataSetChanged();
        //sortList(category);
        return list;
    }








    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TourRecordItemReBinding binding = TourRecordItemReBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("??", list.get(position).toString());

        if (TextUtils.isEmpty(list.get(position).getTouristhistory_idx())) list.get(position).setClear(false);
        else list.get(position).setClear(true);

        holder.binding.tourNameTxv.setText(list.get(position).getTouristspot_name());
        //Glide.with(holder.itemView.getContext()).load(list.get(position).imgUrl).circleCrop().into(holder.binding.tourImv);
        holder.binding.tourImv.setBackground(new ShapeDrawable(new OvalShape()));
        holder.binding.tourImv.setClipToOutline(true);
        Glide.with(holder.itemView.getContext()).load("http://zzipbbong.cafe24.com/imagefile/bsr/" + list.get(position).getTouristspot_img()).circleCrop().into(holder.binding.tourImv);

        if (list.get(position).isClear()){
            //클리어시 적용될 코드
            holder.binding.tourImv.setColorFilter(Color.parseColor("#63000000"));
            holder.binding.clearTitleTxv.setVisibility(View.VISIBLE);


            try{
                Log.wtf("날짜?", list.get(position).getTouristhistory_updatetime());
                holder.binding.clearTitleTxv.setText(
                        sdf.format(list.get(position).getTouristhistory_updatetime()) + " 획득!");
            }catch (Exception e){
                e.printStackTrace();
            }

            holder.binding.clearTitleImv.setVisibility(View.VISIBLE);
        }else {
            holder.binding.tourImv.setColorFilter(null);
            holder.binding.clearTitleTxv.setVisibility(View.INVISIBLE);
            holder.binding.clearTitleTxv.setText("");
            holder.binding.clearTitleImv.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TourRecordItemReBinding binding;
        public ViewHolder(@NonNull TourRecordItemReBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

}
