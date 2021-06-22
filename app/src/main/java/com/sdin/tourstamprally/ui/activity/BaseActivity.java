package com.sdin.tourstamprally.ui.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.RetrofitGenerator;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.api.APIService;
import com.sdin.tourstamprally.data.PreferenceManager;
import com.sdin.tourstamprally.data.UserInfo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;



public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "fc_debug";
    public RetrofitGenerator retrofitGenerator;
    public APIService apiService;
    protected Context mContext;
    public final long FINISH_INTERVAL_TIME = 2000;
    public long backPressedTime = 0;
    public View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        retrofitGenerator = new RetrofitGenerator();
        apiService = retrofitGenerator.getApiService();

        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();

    }   //  onResume

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    protected void initView(){

    }


    public void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /*회원 로그아웃 처리*/
    public void userLogout(){

        /*내부에 저장된 유저 정보 초기화*/
        PreferenceManager.setInt(this, "selectLoginType", -1);
        PreferenceManager.setString(mContext, "vteacherId","");
        PreferenceManager.setString(mContext, "vteacherName","");
        PreferenceManager.setString(mContext, "userpw", "");
        PreferenceManager.setInt(mContext, "idx",-1);
        PreferenceManager.setBoolean(this, "autoLoginCheck", false);
        PreferenceManager.setInt(mContext, "selectedAgencyPosition", 0);
        PreferenceManager.setInt(mContext, "selectedProgramPosition", 0);

        /*충돌 대비 초기화*/
        UserInfo.getInstance().password = PreferenceManager.getString(mContext,"userpw");
        UserInfo.getInstance().accountidx = PreferenceManager.getInt(mContext,"accountidx");


//로그아웃후 화면
//
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

    /*액티비티 메시지 스낵바*/
    public void showSnackbar(String msg){

        Snackbar snackbar = Snackbar.make(mainLayout, msg, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getResources().getColor(R.color.deepGrayColor));

        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0,0,0,0);

        snackbarView.setLayoutParams(params);

        TextView mTextView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        mTextView.setGravity(Gravity.TOP);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
        mTextView.setTextColor(getColor(R.color.white));
        mTextView.setTextSize(Utils.dpToPx(this, getResources().getDimension(R.dimen._1sdp)));
        mTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.getView().bringToFront();

        snackbar.show();

    }

    /*프래그먼트, 다이얼로그 메시지 스낵바*/
    public void showSnackbar(View parent, String msg){

        Snackbar snackbar = Snackbar.make(parent, msg, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getResources().getColor(R.color.deepGrayColor));

        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0,0,0,0);

        snackbarView.setLayoutParams(params);

        TextView mTextView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        mTextView.setGravity(Gravity.TOP);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
        mTextView.setTextColor(getColor(R.color.white));
        mTextView.setTextSize(Utils.dpToPx(this, getResources().getDimension(R.dimen._1sdp)));
        mTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.getView().bringToFront();

        snackbar.show();

    }

}
