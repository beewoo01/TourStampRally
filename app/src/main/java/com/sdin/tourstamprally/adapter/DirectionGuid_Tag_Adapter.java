package com.sdin.tourstamprally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;

public class DirectionGuid_Tag_Adapter extends RecyclerView.Adapter<DirectionGuid_Tag_Adapter.ViewHolder> {

    private ArrayList<Tour_Spot> arrayList;


    public DirectionGuid_Tag_Adapter(ArrayList<Tour_Spot> arrayList) {
        this.arrayList = arrayList;
    }

    public void setList(ArrayList<Tour_Spot> arrayList){
        //this.list.clear();
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                DirectionGuidTagItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tagItemTxv.setText(arrayList.get(position).getTouristspot_tag());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        DirectionGuidTagItemBinding binding;

        public ViewHolder(@NonNull DirectionGuidTagItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
