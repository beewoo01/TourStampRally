package com.sdin.tourstamprally.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DialogDelCheckBinding;
import com.sdin.tourstamprally.databinding.PopupUserWithdrawalBinding;
import com.sdin.tourstamprally.utill.ItemOnClickAb;
import com.sdin.tourstamprally.utill.ReviewDelListener;

public class Del_Review_Dialog extends Dialog {



    private Context context;
    private DialogDelCheckBinding binding;
    private ReviewDelListener itemOnClick;

    public Del_Review_Dialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
    }

    public void setClickListener(ReviewDelListener itemOnClick){
        this.itemOnClick = itemOnClick;
        Log.d("DefaultDialog ", "setClickListener");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_del_check, null, false);
        binding.setDialog(this);
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;


    }

    public void out(){
        itemOnClick.delOnClick();
        dismiss();
    }
}
