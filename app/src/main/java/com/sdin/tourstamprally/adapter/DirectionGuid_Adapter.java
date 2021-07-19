package com.sdin.tourstamprally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;

import java.util.List;

public class DirectionGuid_Adapter extends RecyclerView.Adapter<DirectionGuid_Adapter.ViewHolder> {

    private List list;

    public DirectionGuid_Adapter(List list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DirectionGuidLocationItemBinding binding = DirectionGuidLocationItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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
        }
    }
}
