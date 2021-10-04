package com.sdin.tourstamprally.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.databinding.TourRecordItemReBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.GuidDialog;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.ItemOnClickAb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Tour_Spot> list;
    public static final String TAG = LocationAdapter.class.getSimpleName();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    private ItemOnClick listener;
    private Tour_Spot send_model;
    private Context context;
    //private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'T' HH:mm:ss.SSSX " , Locale.US);



    public LocationAdapter(ArrayList<Tour_Spot> list, Context context){
        this.list = list;
        this.context = context;
        listener = (MainActivity) context;

    }

    public void locationlistSet(ArrayList<Tour_Spot> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Tour_Spot> getList(){
        return list;
    }

    public List<Tour_Spot> locaitonSort(ArrayList<Tour_Spot> list, int category){
        this.list = list;
        notifyDataSetChanged();
        //sortList(category);
        return list;
    }

    private ItemOnClick itemOnClick = new ItemOnClickAb() {
        @Override
        public void ItemGuid(int position) {
            Log.d("dialog Onclick Listener", String.valueOf(position));
            if (position == 1 || position == 2){
                listener.ItemGuid(position);
            }else {
                //listener.ItemGuidForPoint(send_model);
            }
        }
    };


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

        if (TextUtils.isEmpty(list.get(position).getTouristhistory_idx())) list.get(position).setClear(false);
        else list.get(position).setClear(true);



        holder.binding.tourNameTxv.setText(list.get(position).getTouristspot_name());
        //Glide.with(holder.itemView.getContext()).load(list.get(position).imgUrl).circleCrop().into(holder.binding.tourImv);
        holder.binding.tourImv.setBackground(new ShapeDrawable(new OvalShape()));
        holder.binding.tourImv.setClipToOutline(true);
        Glide.with(holder.itemView.getContext()).load("http://coratest.kr/imagefile/bsr/" + list.get(position).getTouristspot_img()).circleCrop().into(holder.binding.tourImv);

        if (list.get(position).isClear()){
            //클리어시 적용될 코드
            holder.binding.tourImv.setColorFilter(Color.parseColor("#63000000"));
            holder.binding.clearTitleTxv.setVisibility(View.VISIBLE);


            try{

                long dateL = list.get(position).getTouristhistory_updatetime();
                String date = sdf.format(dateL);
                holder.binding.clearTitleTxv.setText(
                        sdf.format(list.get(position).getTouristhistory_updatetime()) + " 획득!");
            }catch (Exception e){
                e.printStackTrace();
            }

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
            binding.contentLayout.setOnClickListener( v->{
                send_model = list.get(getAbsoluteAdapterPosition());
                GuidDialog guidDialog = new GuidDialog(context);
                guidDialog.show();
                guidDialog.setClickListener(itemOnClick);
            });

        }
    }

}
