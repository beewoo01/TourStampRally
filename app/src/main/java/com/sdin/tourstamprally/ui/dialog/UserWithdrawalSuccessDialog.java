package com.sdin.tourstamprally.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.PopupUserWithdrawalSuccessBinding;
import com.sdin.tourstamprally.utill.ItemOnClickAb;

public class UserWithdrawalSuccessDialog extends Dialog {



    private Context context;
    private PopupUserWithdrawalSuccessBinding binding;

    private ItemOnClickAb listener;

    public UserWithdrawalSuccessDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
    }

    public void setListener(ItemOnClickAb listener){
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popup_user_withdrawal_success, null, false);
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        binding.cancleBtn.setOnClickListener(v -> {
            listener.signOutListener();
            dismiss();
        });


    }

}
