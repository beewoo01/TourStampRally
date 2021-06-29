package com.sdin.tourstamprally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DrawaMenuBinding;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.utill.ItemOnClick;

public class DrawaRecyclerViewAdapter extends RecyclerView.Adapter<DrawaRecyclerViewAdapter.ViewHolder>{

    private ItemOnClick listener = null;


    public DrawaRecyclerViewAdapter(ItemOnClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DrawaRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DrawaRecyclerViewAdapter.ViewHolder(DrawaMenuBinding.inflate(
                LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DrawaRecyclerViewAdapter.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                setItem(R.drawable.setting_icon, "계정관리", false, holder);
                break;
            case 1:
                setItem(R.drawable.notice_icon, "공지사항", true, holder);
                holder.binding.newImv.setVisibility(View.VISIBLE);
                break;
            case 2:
                setItem(R.drawable.coupon_status_icon, "쿠폰현황", false, holder);
                break;
            case 3:
                setItem(R.drawable.alarm_settings_icon, "알림설정", false, holder);
                break;
            case 4:
                setItem(R.drawable.steamed_list_icon, "찜한목록", false, holder);
                break;
        }
    }

    private void setItem(int iconId, String title, boolean isNew, DrawaRecyclerViewAdapter.ViewHolder holder) {
        holder.binding.menuIcon.setImageResource(iconId);
        holder.binding.menuTxv.setText(title);
        if (isNew) holder.binding.newImv.setVisibility(View.VISIBLE);
        else holder.binding.newImv.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DrawaMenuBinding binding;

        public ViewHolder(@NonNull DrawaMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.drawaMenuLayout.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION)
                    if (listener != null) listener.onClick(getAdapterPosition());
            });

        }
    }
}
