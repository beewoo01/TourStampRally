package com.sdin.tourstamprally;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sdin.tourstamprally.databinding.ActivitySplashBinding;
import com.sdin.tourstamprally.ui.activity.LoginActivity;
import com.sdin.tourstamprally.ui.activity.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private Animation fadeOutAnimation, fadeInAnimation;
    private boolean isErr = false;
    private static int CHECKNUM = 0;
    Handler mHandler;
    int MY_PERMISSIONS_REQUEST_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        /*스플래시 로고 페이드 인/아웃 애니메이션*/
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_fade_out);
        fadeOutAnimation.setAnimationListener(fadeOutAnimationListener);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in);
        fadeInAnimation.setAnimationListener(fadeInAnimationListener);
        binding.logo.startAnimation(fadeInAnimation);
    }


    /*스플래시 로고 페이드 아웃 리스너*/
    public Animation.AnimationListener fadeOutAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            binding.logo.setVisibility(View.GONE);
            startLoading();


        }

        @Override
        public void onAnimationRepeat(Animation animation) {


        }
    };

    /*스플래시 로고 페이드 인 리스너*/
    public Animation.AnimationListener fadeInAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            binding.logo.startAnimation(fadeOutAnimation);


        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    };


    /*체크 실행*/
    private void startLoading() {

        SharedPreferences preferences = setSharedPref();
        String phone = preferences.getString("phone", "");
        String psw = preferences.getString("password", "");
        Log.d("isAutoLogin phone", phone);
        Log.d("isAutoLogin psw", psw);

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(psw)) {
            Utils.UserPhone = phone;
            Utils.UserPassword = psw;

            startActivity(new Intent(this, MainActivity.class));
        } else {

            startActivity(new Intent(this, LoginActivity.class));
        }


    }

    private SharedPreferences setSharedPref(){
        return getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE);
    }
}