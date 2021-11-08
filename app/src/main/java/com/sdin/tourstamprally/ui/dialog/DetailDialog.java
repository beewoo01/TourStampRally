package com.sdin.tourstamprally.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.DialogDetailBinding;
import com.sdin.tourstamprally.model.TouristSpotPoint;

public class DetailDialog extends BaseDialog{

    private Context context;
    private TouristSpotPoint touristSpotPoint;
    private DialogDetailBinding binding;

    public DetailDialog(@NonNull Context context, TouristSpotPoint touristSpotPoint) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
        this.touristSpotPoint = touristSpotPoint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_detail, null, false);
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        initView();

    }

    private void initView(){

        binding.titleTxv.setText(touristSpotPoint.getTouristspotpoint_name());
        binding.contentTxv.setMovementMethod(new ScrollingMovementMethod());
        if (touristSpotPoint.getTouristspotpoint_detail_explan() != null && !TextUtils.isEmpty(touristSpotPoint.getTouristspotpoint_detail_explan())){
            binding.contentTxv.setText(touristSpotPoint.getTouristspotpoint_detail_explan());

        } else {
            binding.contentTxv.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load("http://coratest.kr/imagefile/bsr/"+ touristSpotPoint.getTouristspotpoint_img())
                .error(R.drawable.sample_bg)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(binding.tourspotImv);

        binding.closeBtn.setOnClickListener( v -> {
            dismiss();
        });
    }
}
