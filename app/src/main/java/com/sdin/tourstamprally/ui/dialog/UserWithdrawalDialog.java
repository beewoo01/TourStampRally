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
import com.sdin.tourstamprally.databinding.PopupUserWithdrawalBinding;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.ItemOnClickAb;

public class UserWithdrawalDialog extends Dialog {



    private Context context;
    private PopupUserWithdrawalBinding binding;
    private ItemOnClickAb itemOnClick;

    public UserWithdrawalDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
    }

    public void setClickListener(ItemOnClickAb itemOnClick){
        this.itemOnClick = itemOnClick;
        Log.d("DefaultDialog ", "setClickListener");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popup_user_withdrawal, null, false);
        binding.setDialog(this);
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;


    }

    public void cancle(View view){
        /*position = Integer.parseInt(String.valueOf(view.getTag()));
        itemOnClick.ItemGuid(position);*/
        dismiss();
    }

    public void out(View view){
        if (binding.checkbox.isChecked()){
            itemOnClick.signOutListener();
            dismiss();
        }else {
            Toast.makeText(context, "체크항목을 선택해주세요", Toast.LENGTH_SHORT).show();
        }

        //dismiss();

    }
}
