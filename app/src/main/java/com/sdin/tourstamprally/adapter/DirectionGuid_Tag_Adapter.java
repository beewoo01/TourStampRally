package com.sdin.tourstamprally.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.model.TourTagModel;

import java.util.ArrayList;

public class DirectionGuid_Tag_Adapter extends RecyclerView.Adapter<DirectionGuid_Tag_Adapter.ViewHolder> {

    //필요없는 애 같음
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

    @SuppressLint("NotifyDataSetChanged")
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.wtf("", "");
        holder.binding.tagItemTxv.setText("# " + arrayList.get(position).getHashTag());
        setBackground(position, holder);
    }

    private void setBackground(int position, ViewHolder holder){
        int icon;
        if (selectedItem == position){
            holder.binding.tagItemContainer.setBackground(ContextCompat.getDrawable(holder.binding.tagItemContainer.getContext(), R.drawable.bg_rounded_category_selected));
            holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.getContext(), R.color.white));
            icon = setIcon(true, position);
            Log.wtf("onBindViewHolder", "setBackground if ");

        }else {
            holder.binding.tagItemContainer.setBackground(ContextCompat.getDrawable(holder.binding.tagItemContainer.getContext(), R.drawable.bg_rounded_category));
            holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.getContext(), R.color.mainColor));
            icon = setIcon(false, position);
            Log.wtf("onBindViewHolder", "setBackground elseA ");
        }

        /*Glide.with(holder.binding.tagItemImg.getContext())
                .load(icon)
                //.error(R.drawable.button_selector_drawable)
                .into(holder.binding.tagItemImg);*/
    }

    private int setIcon(boolean isSelected, int position) {
        switch (position) {
            case 0 : {
                if (isSelected) {
                    return R.drawable.ic_roadtour_on;

                } else {
                    return R.drawable.ic_roadtour_off;
                }

            }

            case 1 : {
                if (isSelected) {
                    return R.drawable.ic_hardtour_on;
                } else {
                    return R.drawable.ic_hardtour_off;
                }
            }

            case 2 : {
                if (isSelected) {
                    return R.drawable.ic_trackingtour_on;
                } else {
                    return R.drawable.ic_trackingtour_off;
                }
            }

            case 3 : {
                if (isSelected) {
                    return R.drawable.ic_festivaltour_on;
                } else {
                    return R.drawable.ic_festivaltour_off;
                }

            }

            case 4 : {
                if (isSelected) {
                    return R.drawable.ic_webtoontour_on;
                } else {
                    return R.drawable.ic_webtoontour_off;
                }
            }

            case 5 : {
                if (isSelected) {
                    return R.drawable.ic_historytour_on;
                } else {
                    return R.drawable.ic_hardtour_off;
                }
            }

            default: {
                return R.drawable.ic_hardtour_off;
            }

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
