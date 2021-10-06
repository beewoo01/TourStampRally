package com.sdin.tourstamprally.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.SerializedName;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.VisithistoryItemBinding;
import com.sdin.tourstamprally.model.ReviewWriter;
import com.sdin.tourstamprally.model.history_spotModel;
import com.sdin.tourstamprally.model.history_spotModel2;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.Del_Review_Dialog;
import com.sdin.tourstamprally.ui.dialog.NoReview_Dialog;
import com.sdin.tourstamprally.utill.ItemCliclListener;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.ReviewDelListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class VisitReAdapter extends RecyclerView.Adapter<VisitReAdapter.SwipeViewHolder> implements ReviewDelListener {

    private ArrayList<history_spotModel2> historySpotList;


    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private ItemCliclListener listener;
    private SimpleDateFormat oldSdf, newSdf, timeSdf;
    //private ToggleAnimation toggleAnimation;

    private ItemOnClick onWriteReviewListener;
    private Context context;
    private int delPosition = -1;

    public void itemCilcListener(ItemCliclListener listener) {
        this.listener = listener;
    }

    public void setList(ArrayList<history_spotModel2> historySpotList) {
        this.historySpotList = historySpotList;
    }

    @SuppressLint("SimpleDateFormat")
    public VisitReAdapter(ArrayList<history_spotModel2> historySpotList, Context context) {
        this.historySpotList = historySpotList;
        oldSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        oldSdf.setTimeZone(TimeZone.getTimeZone("KST"));
        newSdf = new SimpleDateFormat("yy.MM.dd");
        timeSdf = new SimpleDateFormat("HH:mm");
        this.context = context;
    }

    @NonNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisithistoryItemBinding binding = VisithistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        onWriteReviewListener = (MainActivity) parent.getContext();
        return new SwipeViewHolder(binding);
    }


    public void setChange(int position){

        history_spotModel2 model = historySpotList.get(position);
        model = new history_spotModel2(model.getAllCount(), model.getMyCount(),
                model.getLocation_idx(), model.getLocation_name() , model.getTouristspot_idx(),
                model.getTouristspot_name(), model.getTouristspot_explan(), model.getTouristspot_latitude(),
                model.getTouristspot_longitude(), model.getTouristspot_img(), model.getTouristhistory_updatetime(),
                0, 0, null);

        historySpotList.set(position, model);
        notifyItemChanged(position);
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

    @Override
    public void delOnClick() {
        listener.delReview(historySpotList.get(delPosition).getReview_idx(), delPosition);
        historySpotList.get(delPosition).setReview_idx(0);
        historySpotList.get(delPosition).setReview_contents("");

    }

    interface OnViewHolderItemClickListener {
        void onViewHolderItemClick();
    }


    public class SwipeViewHolder extends RecyclerView.ViewHolder {
        public VisithistoryItemBinding binding;
        public OnViewHolderItemClickListener onViewHolderItemClickListener;

        public SwipeViewHolder(@NonNull VisithistoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
        public void bind(history_spotModel2 model, int position, SparseBooleanArray selectedItems) {

            binding.seekBar.setEnabled(false);
            binding.titleTxv.setText(model.getTouristspot_name());
            binding.explanTxv.setText(model.getTouristspot_explan());

            int allCount = model.getAllCount();
            int myCount = model.getMyCount();
            int allCountd = (int) ((double) myCount / (double) allCount * 100);
            binding.seekBar.setMax(allCount);
            binding.seekBar.setProgress(myCount);
            if (allCountd != 100) {
                binding.dateTxv.setText(allCountd + "%");

            } else {

                try {
                    Date old_date = oldSdf.parse(model.getTouristhistory_updatetime());
                    if (old_date != null) {
                        String n_date = newSdf.format(old_date);
                        String n_time = timeSdf.format(old_date);
                        binding.dateTxv.setText(n_date + "\n" + n_time);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    binding.dateTxv.setText(model.getTouristhistory_updatetime());
                }
            }

            binding.ratingbar.setOnTouchListener((v, event) -> true);

            binding.heartImv.setOnClickListener(v -> {
                if (listener != null && getAbsoluteAdapterPosition() > -1) {
                    listener.deapsClick(getAbsoluteAdapterPosition(), historySpotList.get(getAbsoluteAdapterPosition()));
                }

            });
            binding.swipeView.setOnClickListener(v -> {
                //Log.wtf("swipeView click", String.valueOf(position));
                onViewHolderItemClickListener.onViewHolderItemClick();
            });

            Glide.with(binding.visitHistoryImv.getContext())
                    .load("http://coratest.kr/imagefile/bsr/" + model.getTouristspot_img())
                    .error(ContextCompat.getDrawable(binding.visitHistoryImv.getContext(), R.drawable.sample_profile_image))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                    .into(binding.visitHistoryImv);

            if (model.getReview_idx() > 0) {
                binding.reviewLayout.setVisibility(View.VISIBLE);
                binding.nameTxv.setText(Utils.User_Name);
                Glide.with(binding.profileIcon.getContext()).load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile).circleCrop()
                        .error(ContextCompat.getDrawable(binding.visitHistoryImv.getContext(), R.drawable.sample_profile_image))
                        .into(binding.profileIcon);

                binding.ratingbar.setRating(model.getReview_score());
                binding.reviewExplan.setText(model.getReview_contents());
            } else {
                binding.reviewLayout.setVisibility(View.GONE);

            }

            if (model.getReview_idx() > 0){
                binding.delReview.setOnClickListener( v -> {
                    delPosition = getAbsoluteAdapterPosition();
                    Del_Review_Dialog dialog = new Del_Review_Dialog(context);
                    dialog.setClickListener(() -> {
                        listener.delReview(historySpotList.get(delPosition).getReview_idx(), delPosition);
                        historySpotList.get(delPosition).setReview_idx(0);
                        historySpotList.get(delPosition).setReview_contents("");
                        onViewHolderItemClickListener.onViewHolderItemClick();

                    });
                    dialog.show();
                });

                binding.gotoReview.setOnClickListener( v -> {
                    onWriteReviewListener.onWriteRewviewClick(
                            new ReviewWriter(
                                    model.getTouristspot_idx(),
                                    model.getTouristspot_name(),
                                    false,
                                    model.getReview_idx(),
                                    model.getReview_score(),
                                    model.getReview_contents()
                            )
                    );
                });
            } else {

                binding.gotoReview.setOnClickListener(v -> {
                    onWriteReviewListener.onWriteRewviewClick(
                            new ReviewWriter(
                                    model.getTouristspot_idx(),
                                    model.getTouristspot_name(),
                                    true
                            )

                    );
                });

                binding.delReview.setOnClickListener( v -> {
                    // TODO: 10/6/21 팝업
                    NoReview_Dialog dialog = new NoReview_Dialog(context);
                    dialog.show();
                });
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
