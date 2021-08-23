package com.sdin.tourstamprally.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sdin.tourstamprally.databinding.VisithistoryItemBinding;
import com.sdin.tourstamprally.model.VisitHistory_Model;
import com.sdin.tourstamprally.ui.fragment.VisitHistoryFragment;

import java.util.ArrayList;

public class VisitReAdapter extends RecyclerView.Adapter<VisitReAdapter.ViewHolder>{

    private ArrayList<VisitHistory_Model> arrayList;

    public VisitReAdapter(ArrayList<VisitHistory_Model> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VisitReAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VisitReAdapter.ViewHolder(VisithistoryItemBinding.inflate(
                LayoutInflater.from(
                        parent.getContext()), parent, false)
        );
    }


    @Override
    public void onBindViewHolder(@NonNull VisitReAdapter.ViewHolder holder, int position) {
        holder.binding.titleTxv.setText(arrayList.get(position).getSpot_name());
        holder.binding.explanTxv.setText(arrayList.get(position).getSpot_explan());
        holder.binding.dateTxv.setText(arrayList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private VisithistoryItemBinding binding;
        public ViewHolder(@NonNull VisithistoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
