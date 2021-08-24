package com.sdin.tourstamprally.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.VisithistoryItemBinding;
import com.sdin.tourstamprally.model.VisitCountModel;
import com.sdin.tourstamprally.model.history_spotModel;
import com.sdin.tourstamprally.utill.ItemCliclListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class VisitReAdapter extends RecyclerView.Adapter<VisitReAdapter.SwipeViewHolder>{

    private ArrayList<history_spotModel> historySpotList;


    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private ItemCliclListener listener;
    private SimpleDateFormat oldSdf, newSdf;
    //private ToggleAnimation toggleAnimation;

    public void itemCilcListener(ItemCliclListener listener){
        this.listener = listener;
    }

    public void setList(ArrayList<history_spotModel> historySpotList){
        this.historySpotList = historySpotList;
    }

    @SuppressLint("SimpleDateFormat")
    public VisitReAdapter(ArrayList<history_spotModel> historySpotList){
        this.historySpotList = historySpotList;
        oldSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        oldSdf.setTimeZone(TimeZone.getTimeZone("KST"));
        newSdf = new SimpleDateFormat("yy.MM.dd");
      //  toggleAnimation = new ToggleAnimation();
    }

    @NonNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisithistoryItemBinding binding = VisithistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);

        return new SwipeViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull SwipeViewHolder holder, int position) {
        holder.bind(historySpotList.get(position), position, selectedItems);
        holder.setOnViewHolderItemClickListener(() -> {
            if (selectedItems.get(position)) {
                // 펼쳐진 Item을 클릭 시
                selectedItems.delete(position);
            } else {
                // 직전의 클릭됐던 Item의 클릭상태를 지움
                selectedItems.delete(prePosition);
                // 클릭한 Item의 position을 저장
                selectedItems.put(position, true);
            }
            // 해당 포지션의 변화를 알림
            if (prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(position);
            // 클릭된 position 저장
            prePosition = position;
        });


    }

    @Override
    public int getItemCount() {
        return historySpotList.size();
    }

    interface OnViewHolderItemClickListener{
        void onViewHolderItemClick();
    }


    public class SwipeViewHolder extends RecyclerView.ViewHolder{
        public VisithistoryItemBinding binding;
        private OnViewHolderItemClickListener onViewHolderItemClickListener;
        public SwipeViewHolder(@NonNull VisithistoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;



        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(history_spotModel model, int position , SparseBooleanArray selectedItems){

            binding.seekBar.setEnabled(false);
            binding.titleTxv.setText(model.getTouristspot_name());
            binding.explanTxv.setText(model.getTouristspot_explan());
            try {

                Date old_date = oldSdf.parse(model.getTouristhistory_updatetime());
                String n_date = newSdf.format(old_date);
                binding.dateTxv.setText(n_date);
            } catch (ParseException e) {
                e.printStackTrace();
                binding.dateTxv.setText(model.getTouristhistory_updatetime());
            }

            binding.ratingbar.setOnTouchListener((v, event) -> true);

            binding.heartImv.setOnClickListener( v -> {
                Log.wtf("heartImv click", String.valueOf(position));
                if (listener != null && getAdapterPosition() > -1){
                    //binding.seekBar.setProgress(40);
                    listener.deapsClick(getAdapterPosition(), historySpotList.get(getAdapterPosition()));
                }

            });
            binding.swipeView.setOnClickListener(v-> {
                Log.wtf("swipeView click", String.valueOf(position));
                if (model.getReview_idx() > 0){
                    onViewHolderItemClickListener.onViewHolderItemClick();
                }
            });

            Glide.with(binding.visitHistoryImv.getContext())
                    .load("http://zzipbbong.cafe24.com/imagefile/bsr/" + model.getTouristspot_img())
                    .error(ContextCompat.getDrawable(binding.visitHistoryImv.getContext(), R.drawable.sample_profile_image))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                    .into(binding.visitHistoryImv);

            binding.seekBar.setMax(100);
            binding.seekBar.setProgress(model.getPercent());
            Log.wtf("percent", String.valueOf(model.getPercent()));
            if (model.getPercent() < 100){
                binding.dateTxv.setText(model.getPercent() + "%");
            }

            if (model.getReview_idx() > 0){
                binding.nameTxv.setText(model.getUser_name());
                Glide.with(binding.profileIcon.getContext()).load("http://zzipbbong.cafe24.com/imagefile/bsr/" + model.getUser_profile()).circleCrop()
                        .error(ContextCompat.getDrawable(binding.visitHistoryImv.getContext(), R.drawable.sample_profile_image))
                        .into(binding.profileIcon);

                binding.ratingbar.setRating(model.getReview_score());
                binding.reviewExplan.setText(model.getReview_contents());
            }



            changeVisibility(selectedItems.get(position));
        }

        private void changeVisibility(final boolean isExpanded) {
            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(100);
            va.addUpdateListener(animation -> {
                // imageView의 높이 변경
                binding.reviewParentLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                binding.reviewParentLayout.requestLayout();
                // imageView가 실제로 사라지게하는 부분
                binding.reviewParentLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            });
            // Animation start
            va.start();
        }

        public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
            this.onViewHolderItemClickListener = onViewHolderItemClickListener;
        }

    }
}
