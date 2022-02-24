package com.sdin.tourstamprally.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DialogBsrBinding;
import com.sdin.tourstamprally.utill.listener.ItemOnClick;

public class BsrDialog extends Dialog{

    private Context context;
    private DialogBsrBinding binding;
    private ItemOnClick itemOnClick;
    //private int position = 0;

    public void setClickListener(ItemOnClick itemOnClick){
        this.itemOnClick = itemOnClick;

    }

    public BsrDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_bsr, null, false);
        setContentView(binding.getRoot());
        binding.setDialog(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        /*binding.touristGuidTxv.setOnClickListener( v -> {
            //position = 0;
            binding.touristGuidTxv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_15));
            binding.touristGuidTxv.setTextColor(Color.WHITE);
            binding.shopGuidTxv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_03));
            binding.shopGuidTxv.setTextColor(Color.BLACK);
            itemOnClick.onClick(0);

        });
        binding.shopGuidTxv.setOnClickListener( v -> {
            //position = 1;
            binding.touristGuidTxv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_03));
            binding.touristGuidTxv.setTextColor(Color.BLACK);
            binding.shopGuidTxv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_15));
            binding.shopGuidTxv.setTextColor(Color.WHITE);
            itemOnClick.onClick(1);
        });*/

        binding.closeBtn.setOnClickListener(v -> dismiss());

        /*binding.cancleTxv.setOnClickListener(v -> dismiss());
        binding.confirmTxv.setOnClickListener(v -> itemOnClick.onClick(position));*/

    }

    public void itemClick(View view){
        int position = (int) view.getTag();
        itemOnClick.onClick(position);
        dismiss();
    }

}
