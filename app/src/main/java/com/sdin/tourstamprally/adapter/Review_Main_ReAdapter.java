package com.sdin.tourstamprally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ReItemReviewMainBinding;
import com.sdin.tourstamprally.model.AllReviewDTO;

import java.util.ArrayList;

public class Review_Main_ReAdapter extends RecyclerView.Adapter<Review_Main_ReAdapter.ViewHolder> {

    private ArrayList<AllReviewDTO> list;
    private ReviewMainItemOnClick listener;

    public interface ReviewMainItemOnClick {
        void onItemClick(AllReviewDTO model);
    }

    public void setListener(ReviewMainItemOnClick listener){
        this.listener = listener;
    }

    public Review_Main_ReAdapter(ArrayList<AllReviewDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ReItemReviewMainBinding.inflate(
                        LayoutInflater.from(
                                parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.locationTxv.setText(list.get(position).getLocation_name());
        holder.binding.spotpointNameTxv.setText(list.get(position).getTouristspot_name());
        holder.binding.userNameTxv.setText(list.get(position).getUser_name());
        holder.binding.reviewTxv.setText(list.get(position).getReview_contents());
        holder.binding.ratingbar.setRating(list.get(position).getReview_score());
        Glide.with(holder.binding.userProfileImv.getContext())
                .load("http://coratest.kr/imagefile/bsr/" + list.get(position).getUser_profile()).circleCrop()
                .error(R.drawable.sample_profile_image)
                .into(holder.binding.userProfileImv);
    }

    @Override
    public int getItemCount() {
        if (list.size() >= 1 && list.size() <= 3){
            return list.size();
        }else if (list.size() > 3){
            return 3;
        } else {
            return 0;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ReItemReviewMainBinding binding;

        public ViewHolder(@NonNull ReItemReviewMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.reviewContainer.setOnClickListener( v -> {
                if (listener != null){
                    listener.onItemClick(list.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }
}
