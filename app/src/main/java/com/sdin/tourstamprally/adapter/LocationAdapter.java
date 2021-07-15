package com.sdin.tourstamprally.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.TourRecordItemReBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sdin.tourstamprally.ui.fragment.TourRecordFragment.DATEFORM;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Tour_Spot> list;
    private Context context;
    public static final String TAG = LocationAdapter.class.getSimpleName();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");


    public LocationAdapter(List<Tour_Spot> list, Context context){
        this.list = list;
        this.context = context;
    }

    public List<Tour_Spot> sortList(int form){

        switch (form){
            case R.id.popular_btn :
                Log.d(TAG, "popular_btn");
                list.sort((o1, o2) -> {

                    if (o1.getTouristspot_checkin_count() == o2.getTouristspot_checkin_count()) return 0;
                    else if (o1.getTouristspot_checkin_count() < o2.getTouristspot_checkin_count()) return 1;
                    else return -1;
                });

                break;

            case R.id.recent_btn :
                Log.d(TAG, "recent_btn");
                list.sort((o1, o2) -> {

                    long sortDate1 = o1.getTouristhistory_updatetime();
                    long sortDate2 = o2.getTouristhistory_updatetime();
                    //35.1554, 129.0638

                    if (sortDate1 == sortDate2) return 0;
                    else if (sortDate1 < sortDate2) return 1;
                    else return -1;

                });
                break;

            case R.id.near_btn :
                Log.d(TAG, "near_btn");
                break;
        }



        /*   - spotpoint_idx
        *       . 1 = 8
        *       . 2 = 2
        *       . 3 = 1
        *
        *    - touristspotpoint_touristspot_idx
        *       . 1 = 21
        *       . 2 = 33
        *       . 3 = 36
        *
        *    - touristspot
        *     21 =  부산 시민공원 8
        *     33 =  비프광장 2
        *     36 =  국제시장 1
        * */



        notifyDataSetChanged();

        return list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TourRecordItemReBinding binding = TourRecordItemReBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("??", list.get(position).toString());

        if (list.get(position).getTouristhistory_idx() == null) list.get(position).setClear(false);
        else list.get(position).setClear(true);

        holder.binding.tourNameTxv.setText(list.get(position).getTouristspot_name());
        //Glide.with(holder.itemView.getContext()).load(list.get(position).imgUrl).circleCrop().into(holder.binding.tourImv);
        holder.binding.tourImv.setBackground(new ShapeDrawable(new OvalShape()));
        holder.binding.tourImv.setClipToOutline(true);
        Glide.with(holder.itemView.getContext()).load(R.drawable.enjoy_busan).circleCrop().into(holder.binding.tourImv);

        if (list.get(position).isClear()){
            //클리어시 적용될 코드
            holder.binding.tourImv.setColorFilter(Color.parseColor("#63000000"));
            holder.binding.clearTitleTxv.setVisibility(View.VISIBLE);
            holder.binding.clearTitleTxv.setText(
                    sdf.format(list.get(position).getTouristhistory_updatetime()) + " 획득!");
            holder.binding.clearTitleImv.setVisibility(View.VISIBLE);
        }else {
            holder.binding.tourImv.setColorFilter(null);
            holder.binding.clearTitleTxv.setVisibility(View.INVISIBLE);
            holder.binding.clearTitleTxv.setText("");
            holder.binding.clearTitleImv.setVisibility(View.GONE);
        }


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
