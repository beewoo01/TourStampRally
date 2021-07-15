package com.sdin.tourstamprally.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DialogDefaultBinding;

public class DefaultDialog extends Dialog {
    private View.OnClickListener mCloseButtonListener;
    Context mContext;
    DialogDefaultBinding binding;


    private String title;


    public DefaultDialog(Context context, View.OnClickListener closeButtonListener, String title) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;


        mContext = context;
        this.title = title;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_default, null, false);
        setContentView(binding.getRoot());


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        binding.title.setText(this.title);
        binding.btnConfirm.setOnClickListener(mCloseButtonListener);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);


    }
}
