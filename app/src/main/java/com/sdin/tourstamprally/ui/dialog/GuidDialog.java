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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DialogGuidBinding;
import com.sdin.tourstamprally.utill.ItemOnClick;

public class GuidDialog extends Dialog {



    private Context context;
    private DialogGuidBinding binding;
    //private ItemOnClick itemOnClick;
    private ItemOnClick itemOnClick;

    public GuidDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
    }

    public void setClickListener(ItemOnClick itemOnClick){
        this.itemOnClick = itemOnClick;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_guid, null, false);
        binding.setDialog(this);
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;


    }

    public void buttonClick(View view){
        int position = Integer.parseInt(String.valueOf(view.getTag()));
        itemOnClick.ItemGuid(position);
        dismiss();
    }

    public void result(View view){
        dismiss();

    }
}
