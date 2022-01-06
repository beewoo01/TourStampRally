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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DialogBsrBinding;
import com.sdin.tourstamprally.databinding.DialogStempBinding;
import com.sdin.tourstamprally.utill.ItemOnClick;

public class StampDialog extends Dialog{

    private Context context;
    private DialogStempBinding binding;
    private ItemOnClick itemOnClick;
    //private int position = 0;

    public void setClickListener(ItemOnClick itemOnClick){
        this.itemOnClick = itemOnClick;
    }

    public StampDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_stemp, null, false);
        setContentView(binding.getRoot());
        binding.setDialog(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        binding.closeBtn.setOnClickListener(v -> dismiss());

    }

    public void itemClick(View view){
        int position = Integer.parseInt(String.valueOf(view.getTag()));
        itemOnClick.ItemGuid(position);
        dismiss();
    }

}
