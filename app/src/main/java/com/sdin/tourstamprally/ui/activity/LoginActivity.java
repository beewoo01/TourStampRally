package com.sdin.tourstamprally.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sdin.tourstamprally.FindPasswordActivity;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.RetrofitGenerator;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.api.APIService;
import com.sdin.tourstamprally.databinding.ActivityLoginBinding;
import com.sdin.tourstamprally.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private String phone, psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        //isAutoLogin();


        if (!TextUtils.isEmpty(getIntent().getStringExtra("phone"))){
            //Log.wtf("phone", "phonephonephonephone");
            binding.editPhone.setText(getIntent().getStringExtra("phone"));
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra("psw"))){
            //Log.wtf("psw", "pswpswpswpswpsw");
            binding.editPassword.setText(getIntent().getStringExtra("psw"));
        }

    }

    /*private void isAutoLogin(){
        // TODO: 6/29/21 SplashActivity로 이동해야함
        SharedPreferences preferences = setSharedPref();
        phone = preferences.getString("phone", "");
        psw = preferences.getString("password", "");
        Log.d("isAutoLogin phone", phone);
        Log.d("isAutoLogin psw", psw);
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(psw)){
            *//*Utils.UserPhone = phone;
            Utils.UserPassword = psw;*//*
            login();
            //movoToMain();
        } else if (!TextUtils.isEmpty(phone) && TextUtils.isEmpty(psw)) {
            binding.editPhone.setText(phone);
        }

    }*/

    public void autoSaveClick(View view){
        if (binding.autoLoginRbt.isChecked() && binding.saveIdRbt.isChecked()){
            if (view.getId() == R.id.auto_login_rbt){
                binding.saveIdRbt.setChecked(false);
            }else {
                binding.autoLoginRbt.setChecked(false);
            }
        }
    }


    public void validation(){
        if (!TextUtils.isEmpty(binding.editPhone.getText()) || !TextUtils.isEmpty(binding.editPassword.getText())){
            phone = binding.editPhone.getText().toString();
            psw = binding.editPassword.getText().toString();
            login();
            
        }else {
            Toast.makeText(LoginActivity.this, "로그인 정보를 정확히 입력해 주세요", Toast.LENGTH_SHORT).show();
        }

    }

    private void login(){

        apiService.userLoginExists(phone, psw).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("result JOIN", result);
                if (result.equals("true")){
                    saveInfo();
                    //Toast.makeText(LoginActivity.this, "로그인 성공!!" + result, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void saveInfo(){


        apiService.userLogin(phone, psw).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel result = response.body();
                //Log.d("result JOIN", result.toString());
                try {
                    if (result == null || result.equals("null") ){
                        //Log.d("result", result.toString());
                        Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(LoginActivity.this, "로그인 성공!!" + result, Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d("result!!", result.toString());
                        if (result.getEnable().equals("0") ){
                            if (binding.autoLoginRbt.isChecked()){
                                setShearedString("phone", binding.editPhone.getText().toString());
                                setShearedString("password", binding.editPassword.getText().toString());

                            }else if (binding.saveIdRbt.isChecked()){
                                setShearedString("phone", binding.editPhone.getText().toString());
                            }
                            Utils.UserPhone = binding.editPhone.getText().toString();
                            Utils.UserPassword = binding.editPassword.getText().toString();
                            Utils.User_Idx = result.getUserIdx();
                            Utils.User_Name = result.getName();
                            Utils.User_Email = result.getEmail();
                            Utils.User_Location = result.getLocation();
                            Utils.User_Profile = result.getUser_profile();

                            movoToMain();
                        }else {
                            Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void movoToMain(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private SharedPreferences setSharedPref(){
        return getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE);
    }

    private void setShearedString(String key, String value){
        SharedPreferences preferences = setSharedPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public void Forgot_psw(){
        startActivity(new Intent(this, FindPasswordActivity.class));
    }

    public void SignUp(){
        startActivity(new Intent(this, TermsOfConditionsActivity.class));
    }

}