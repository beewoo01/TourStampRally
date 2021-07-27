package com.sdin.tourstamprally.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.fragment.DirectionGuidFragment;
import com.sdin.tourstamprally.utill.ItemOnClick;

import java.util.ArrayList;
import java.util.List;

public class DirectionGuid_Adapter extends RecyclerView.Adapter<DirectionGuid_Adapter.ViewHolder> {

    private ArrayList<Tour_Spot> list;

    /*public interface ItemOnClick{
        void onClick(Tour_Spot tour_spot);
    } */

    private ItemOnClick itemOnClick = null;
    private Context context;

    public DirectionGuid_Adapter(ArrayList<Tour_Spot> list, Activity activity) {
        this.list = list;
        this.itemOnClick = (ItemOnClick) activity;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.locationSpotTxv.setText(list.get(position).getLocation_name());
        setPercent(position);
        //holder.binding.seekBarDirectionItem.setProgress();
    }

    private int setPercent(int position){
        list.get(position).getLocation_percentage();

        return 0;
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
                itemOnClick.onItemClick(list.get(getAdapterPosition()));
            });
        }
    }
}
