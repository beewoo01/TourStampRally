package com.sdin.tourstamprally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.databinding.TourRecordItemReBinding;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private ArrayList list;
    private Context context;

    public StoreAdapter(ArrayList list, Context context) {
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
