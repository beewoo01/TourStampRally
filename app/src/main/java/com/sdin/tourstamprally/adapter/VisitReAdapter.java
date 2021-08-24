package com.sdin.tourstamprally.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.adapter.swipe.ToggleAnimation;
import com.sdin.tourstamprally.databinding.VisithistoryItemBinding;
import com.sdin.tourstamprally.model.VisitHistory_Model;
import com.sdin.tourstamprally.utill.ItemCliclListener;

import java.util.ArrayList;

public class VisitReAdapter extends RecyclerView.Adapter<VisitReAdapter.SwipeViewHolder>{

    private ArrayList<VisitHistory_Model> arrayList;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private ItemCliclListener listener;
    //private ToggleAnimation toggleAnimation;

    public void itemCilcListener(ItemCliclListener listener){
        this.listener = listener;
    }

    public VisitReAdapter(ArrayList<VisitHistory_Model> arrayList){
        this.arrayList = arrayList;
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

        holder.bind(arrayList.get(position), position, selectedItems);
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
        return arrayList.size();
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
        public void bind(VisitHistory_Model model, int position , SparseBooleanArray selectedItems){

            binding.seekBar.setEnabled(false);
            binding.titleTxv.setText(model.getSpot_name());
            binding.explanTxv.setText(model.getSpot_explan());
            binding.dateTxv.setText(model.getDate());
            binding.ratingbar.setOnTouchListener((v, event) -> true);

            binding.heartImv.setOnClickListener( v -> {
                Log.wtf("heartImv click", String.valueOf(position));
                if (listener != null && getAdapterPosition() > -1){
                    binding.seekBar.setProgress(40);
                    listener.deapsClick(getAdapterPosition(), arrayList.get(getAdapterPosition()));
                }

            });
            binding.swipeView.setOnClickListener(v-> {
                Log.wtf("swipeView click", String.valueOf(position));
                onViewHolderItemClickListener.onViewHolderItemClick();
            });


            changeVisibility(selectedItems.get(position));
        }

        private void changeVisibility(final boolean isExpanded) {
            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(100);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // imageView의 높이 변경
                    binding.reviewParentLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                    binding.reviewParentLayout.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    binding.reviewParentLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }

        public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
            this.onViewHolderItemClickListener = onViewHolderItemClickListener;
        }

    }
}
