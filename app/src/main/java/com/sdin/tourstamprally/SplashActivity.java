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
import android.widget.Toast;

import com.sdin.tourstamprally.databinding.ActivitySplashBinding;
import com.sdin.tourstamprally.model.UserModel;
import com.sdin.tourstamprally.ui.activity.BaseActivity;
import com.sdin.tourstamprally.ui.activity.LoginActivity;
import com.sdin.tourstamprally.ui.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

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

        //startActivity(new Intent(this, LoginActivity.class));



        SharedPreferences preferences = setSharedPref();
        final String phone = preferences.getString("phone", "");
        final String psw = preferences.getString("password", "");
        Log.d("isAutoLogin phone", phone);
        Log.d("isAutoLogin psw", psw);

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(psw)) {
            apiService.userLoginExists(phone, psw).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    login(phone, psw);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
                }
            });

        }else {
            moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
        }


    }

    private void moveActivity(Class<?> Activity, String msg){
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SplashActivity.this, Activity));
    }

    private void login(String phone, String psw){


        apiService.userLogin(phone, psw).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel result = response.body();
                Log.d("result JOIN", result.toString());
                if (result.equals("null") || result == null){
                    Log.d("result", result.toString());
                    moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
                    //Toast.makeText(LoginActivity.this, "로그인 성공!!" + result, Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("result!!", result.toString());
                    if (result.getEnable().equals("0") ){

                        Utils.UserPhone = phone;
                        Utils.UserPassword = psw;
                        Utils.User_Idx = result.getUserIdx();
                        Utils.User_Name = result.getName();
                        Utils.User_Email = result.getEmail();
                        Utils.User_Location = result.getLocation();

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else {
                        moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
                    }

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
                moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
            }
        });




        /*apiService.userLogin(phone, psw).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {



                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(psw)) {
                    Utils.UserPhone = phone;
                    Utils.UserPassword = psw;

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
            }
        });*/

    }

    private SharedPreferences setSharedPref(){
        return getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE);
    }
}