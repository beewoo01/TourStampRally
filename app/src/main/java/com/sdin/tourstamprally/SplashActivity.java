package com.sdin.tourstamprally;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.sdin.tourstamprally.data.Constant;
import com.sdin.tourstamprally.databinding.ActivitySplashBinding;
import com.sdin.tourstamprally.model.UserModel;
import com.sdin.tourstamprally.ui.activity.BaseActivity;
import com.sdin.tourstamprally.ui.activity.LoginActivity;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.DefaultDialog;

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
    private DefaultDialog defaultPopUpDialog;

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

        mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {

                if (!isErr) {

                    SharedPreferences preferences = setSharedPref();
                    final String phone = preferences.getString("phone", "");
                    final String psw = preferences.getString("password", "");
                    Log.d("isAutoLogin phone", phone);
                    Log.d("isAutoLogin psw", psw);
                    if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(psw))
                    {
                        login(phone, psw);
                    }else {
                        moveActivity(LoginActivity.class, null);
                    }



                    /*앱 관련 체크 하나라도 false면 처음부터 다시 체크*/
                } else {

                    startLoading();

                }

            }

        };
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

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isErr){
                if (permission(CHECKNUM)){
                    CHECKNUM++;

                    Message msg = mHandler.obtainMessage();
                    Bundle bd = new Bundle();
                    bd.putString("fire", "fire");
                    msg.setData(bd);
                    isErr = false;
                    mHandler.sendMessage(msg);    //메세지를 핸들러로 넘김

                } else {

                    isErr = true;

                    /*메시지 팝업*/
                    ViewGroup permissionView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_permission_meta, null, false);

                    switch (CHECKNUM) {

                        case 0:

                            TextView permissionGpsErrText = permissionView.findViewById(R.id.tv_test);
                            permissionGpsErrText.setText("어플 사용시 필요한 권한을 허용해 주세요.");
                            defaultPopUpDialog = new DefaultDialog(mContext,  permissionErrPopUpCloseButtonListener, "어플 사용시 필요한 권한을 허용해 주세요.");
                            defaultPopUpDialog.show();

                            break;

                        case 1:

                            TextView gpsErrText = permissionView.findViewById(R.id.tv_test);
                            gpsErrText.setText("GPS 기능이 꺼져 있습니다. GPS 기능을 켜주세요.");
                            defaultPopUpDialog = new DefaultDialog(mContext,  gpsErrPopUpCloseButtonListener, "GPS 기능이 꺼져 있습니다. GPS 기능을 켜주세요.");
                            defaultPopUpDialog.show();

                            break;

                        case 2:

                            TextView networkErrText = permissionView.findViewById(R.id.tv_test);
                            networkErrText.setText("네트워크 오류가 발생 하였습니다.");
                            defaultPopUpDialog = new DefaultDialog(mContext,  networkErrPopUpCloseButtonListener, "네트워크 오류가 발생 하였습니다.");
                            defaultPopUpDialog.show();

                            break;

                        case 3:

                            break;

                    }
                }
            }
        },500);




    }

    private void moveActivity(Class<?> Activity, String msg) {

        Intent intent = new Intent(this, Activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in);
        if (!TextUtils.isEmpty(msg)){
            Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void login(String phone, String psw){

        //permission();

        apiService.userLogin(phone, psw).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel result = response.body();
                try {
                    if (result == null || result.equals("null")){
                          //Log.d("result", "null오네");
                          moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
                        //Toast.makeText(LoginActivity.this, "로그인 성공!!" + result, Toast.LENGTH_SHORT).show();
                    }else {
                        //Log.d("result!!", result.toString());
                        if (result.getEnable().equals("0") ){

                            Utils.UserPhone = phone;
                            Utils.UserPassword = psw;
                            Utils.User_Idx = result.getUserIdx();
                            Utils.User_Name = result.getName();
                            Utils.User_Email = result.getEmail();
                            Utils.User_Location = result.getLocation();
                            Utils.User_Profile = result.getUser_profile();

                            //Log.wtf("UserIDX!!", String.valueOf(Utils.User_Idx));

                            moveActivity(MainActivity.class, "로그인에 성공하셨습니다.");
                        }else {
                            moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
                moveActivity(LoginActivity.class, "로그인에 실패하셨습니다.");
            }
        });

    }

    private boolean permission(int checkNum){
        boolean check;

        switch (checkNum){
            case 0:
                Constant.ACCESS_CAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                Constant.ACCESS_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                Constant.ACCESS_STOREGE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (Constant.ACCESS_CAMERA == PackageManager.PERMISSION_GRANTED){
                    check = true;
                    if (Constant.ACCESS_LOCATION == PackageManager.PERMISSION_GRANTED){
                        check = true;
                        if (Constant.ACCESS_STOREGE == PackageManager.PERMISSION_GRANTED){
                            check = true;
                        }else {
                            check = false;
                        }
                    }else {
                        check = false;
                    }
                }else {
                    check = false;
                }






                break;

            case 1 :
                if (Utils.getGPSState(this)){
                    check = true;
                } else {
                    check = false;
                }
                break;

            case 2 :
                if (Utils.getNetworkStatus(this) < 3){
                    check = true;
                } else {
                    check = false;
                }

                break;

            default:
                check = false;
        }
        return check;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //Log.wtf("onRequestPermissionsResult ", "1111111111");
            SharedPreferences preferences = setSharedPref();
            String phone = preferences.getString("phone", "");
            String psw = preferences.getString("password", "");
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(psw)){
                Utils.UserPhone = phone;
                Utils.UserPassword = psw;
                login(phone, psw);
                //movoToMain();
            }else {
                moveActivity(LoginActivity.class, null);
            }

        }else {
            AlertDialog.Builder di = new AlertDialog.Builder(this);
            di.setTitle("앱 권한");
            di.setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보> 권한> 에서 모든 권한을 허용해 주십시오");
            di.setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                    startActivity(intent);
                    dialog.cancel();
                }
            });
            di.show();
        }

        return;

    }

    private SharedPreferences setSharedPref(){
        return getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE);
    }


    // 퍼미션 에러 팝업 닫기
    private View.OnClickListener permissionErrPopUpCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            defaultPopUpDialog.dismiss();
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CONTACTS);

        }
    };

    // gps 에러 팝업 닫기
    private View.OnClickListener gpsErrPopUpCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            defaultPopUpDialog.dismiss();
            CHECKNUM = 0;
            isErr = false;
            startLoading();

        }
    };

    // 네트워크 에러 팝업 닫기
    private View.OnClickListener networkErrPopUpCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            defaultPopUpDialog.dismiss();
            CHECKNUM = 0;
            isErr = false;
            startLoading();

        }
    };
}