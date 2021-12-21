package com.sdin.tourstamprally.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.model.CouponModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PopUp_Image extends BaseDialog{

    private Context context;
    private CouponModel couponModel;
    private TextView coupontxv, expirationPeriodTxv, checktxv;

    public PopUp_Image(@NonNull Context context, CouponModel couponModel) {
        super(context, R.style.FullScreenDialogStyle);
        this.context = context;
        this.couponModel = couponModel;
    }

    @SuppressLint({"SetTextI18n","SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_popup);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;


        coupontxv = findViewById(R.id.coupon_txv);
        expirationPeriodTxv = findViewById(R.id.expiration_period_txv);
        checktxv = findViewById(R.id.check_txv);
        checktxv.setOnClickListener(v -> dismiss());

        coupontxv.setText("쿠폰번호 : " + couponModel.getCoupon_number());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdg2 = new SimpleDateFormat("yyyy.MM.dd");
        Calendar calendar = Calendar.getInstance();

        try {
            Date to = sdf.parse(couponModel.getCoupon_createtime());
            calendar.setTime(to);

            String oldDate = sdg2.format(to);
            calendar.add(Calendar.DATE, 30);

            int nMonth = calendar.get(Calendar.MONTH) +1;
            int nDate = calendar.get(Calendar.DATE);
            //String endDate = month + "." + date;
            String endDate = nMonth + "." + nDate;
            expirationPeriodTxv.setText("사용기간 : " + oldDate+ "~" + endDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        //couponModel.getCoupon_createtime()



        //ImageView imageView = findViewById(R.id.coupon_imv);
        //Glide.with(context).load(R.drawable.allclearimg).into(imageView);


        /*imageView.setOnClickListener( v -> {
            dismiss();
        });

        findViewById(R.id.container).setOnClickListener( v -> {
            Log.wtf("hollo", "hallo");
        });*/




    }
}
