package com.sdin.tourstamprally.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;

public class PopUp_Image extends BaseDialog{

    private Context context;

    public PopUp_Image(@NonNull Context context) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_popup);

        ImageView imageView = findViewById(R.id.coupon_imv);
        Glide.with(context).load(R.drawable.allclearimg).into(imageView);

        imageView.setOnClickListener( v -> {
            dismiss();
        });

        findViewById(R.id.container).setOnClickListener( v -> {
            Log.wtf("hollo", "hallo");
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;


    }
}
