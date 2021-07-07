package com.sdin.tourstamprally.adapter;

import android.content.Context;
import android.graphics.Rect;
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

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Tour_Spot> list;
    private Context context;

    public LocationAdapter(ArrayList<Tour_Spot> list, Context context){
        this.list = list;
        this.context = context;
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
        Glide.with(holder.itemView.getContext()).load(list.get(position).imgUrl).circleCrop().into(holder.binding.tourImv);

        if (list.get(position).isClear){
            //클리어시 적용될 코드
            holder.binding.tourImv.setForeground(ContextCompat.getDrawable(context, R.drawable.forground_overlay));
            holder.binding.clearTitleTxv.setVisibility(View.VISIBLE);
            holder.binding.clearTitleTxv.setText(list.get(position).getUpdateTime() + " 획득!");
            holder.binding.clearTitleImv.setVisibility(View.VISIBLE);
        }else {
            holder.binding.tourImv.setForeground(null);
            holder.binding.clearTitleTxv.setVisibility(View.GONE);
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
