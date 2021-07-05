package com.sdin.tourstamprally;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sdin.tourstamprally.databinding.ActivitySplashBinding;
import com.sdin.tourstamprally.ui.activity.LoginActivity;

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
        startActivity(new Intent(this, LoginActivity.class));
    }
}