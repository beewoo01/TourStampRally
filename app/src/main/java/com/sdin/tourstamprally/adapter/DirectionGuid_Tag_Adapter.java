package com.sdin.tourstamprally.adapter;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.model.HashTagModel;
import com.sdin.tourstamprally.model.TourTagModel;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;

public class DirectionGuid_Tag_Adapter extends RecyclerView.Adapter<DirectionGuid_Tag_Adapter.ViewHolder> {

    private ArrayList<TourTagModel> arrayList;
    private int selectedItem = -1;
    private int prevSelected = -1;

    public interface ItemOnClick {
        void onClick(TourTagModel param);
    };

    private ItemOnClick mListener = null;

    public void setOnItemClickListener(ItemOnClick mListener){
        this.mListener = mListener;
    }

    public DirectionGuid_Tag_Adapter(ArrayList<TourTagModel> arrayList) {
        this.arrayList = arrayList;
    }

    public void setList(ArrayList<TourTagModel> arrayList){
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
        holder.binding.tagItemTxv.setText("# " + arrayList.get(position).getHashTag());
        setBackground(position, holder);
    }

    private void setBackground(int position, ViewHolder holder){
        if (selectedItem == position){
            holder.binding.tagItemTxv.setBackground(ContextCompat.getDrawable(holder.binding.tagItemTxv.getContext(), R.drawable.bg_rounded_category_selected));
            holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.getContext(), R.color.white));

        }else {
            holder.binding.tagItemTxv.setBackground(ContextCompat.getDrawable(holder.binding.tagItemTxv.getContext(), R.drawable.bg_rounded_category));
            holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.getContext(), R.color.mainColor));
        }
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
            binding.tagItemTxv.setOnClickListener( v -> {
                prevSelected = selectedItem;
                selectedItem = getAbsoluteAdapterPosition();
                if (getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                    if (mListener != null){
                        mListener.onClick(arrayList.get(getAbsoluteAdapterPosition()));
                        notifyItemChanged(selectedItem);
                        notifyItemChanged(prevSelected);
                    }
                }
            });
        }
    }
}
