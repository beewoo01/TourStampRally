package com.sdin.tourstamprally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
import com.sdin.tourstamprally.databinding.TourRecordItemReBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.sdin.tourstamprally.ui.fragment.TourRecordFragment.DATEFORM;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Tour_Spot> list;
    private Context context;
    public static final String TAG = LocationAdapter.class.getSimpleName();


    public LocationAdapter(ArrayList<Tour_Spot> list, Context context){
        this.list = list;
        this.context = context;
    }

    public ArrayList<Tour_Spot> sortList(int form){

        switch (form){
            case R.id.popular_btn :
                Log.d(TAG, "popular_btn");
                break;
            case R.id.recent_btn :
                Log.d(TAG, "recent_btn");
                break;
            case R.id.near_btn :
                Log.d(TAG, "near_btn");
                break;
        }

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");


        list.sort((o1, o2) -> {


            try {
                Date tempDate = sdf.parse(o1.getUpdateTime());
                Date tempDate2 = sdf.parse(o2.getUpdateTime());

                long currentLong = tempDate.getTime();
                long currentLong2 = tempDate2.getTime();

                if (currentLong == currentLong2){
                    Log.d(TAG, "0000");
                    return 0;
                }


                else if (currentLong < currentLong2) {
                    Log.d(TAG, "1111");
                    return 1;
                }

                else {
                    Log.d(TAG, "-1-1-1-1");
                    return -1;
                }

            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }


        });

        notifyDataSetChanged();

        return list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TourRecordItemReBinding binding = TourRecordItemReBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tourNameTxv.setText(list.get(position).spotName);
        //Glide.with(holder.itemView.getContext()).load(list.get(position).imgUrl).circleCrop().into(holder.binding.tourImv);
        holder.binding.tourImv.setBackground(new ShapeDrawable(new OvalShape()));
        holder.binding.tourImv.setClipToOutline(true);
        Glide.with(holder.itemView.getContext()).load(R.drawable.enjoy_busan).circleCrop().into(holder.binding.tourImv);

        if (list.get(position).isClear){
            //클리어시 적용될 코드
            holder.binding.tourImv.setColorFilter(Color.parseColor("#63000000"));
            holder.binding.clearTitleTxv.setVisibility(View.VISIBLE);
            holder.binding.clearTitleTxv.setText(list.get(position).getUpdateTime() + " 획득!");
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
