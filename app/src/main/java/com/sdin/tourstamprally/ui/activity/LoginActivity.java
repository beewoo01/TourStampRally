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
import com.sdin.tourstamprally.api.APIService;
import com.sdin.tourstamprally.databinding.ActivityLoginBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        // TODO: 6/10/21 자동로그인 OR 아이디저장 확인

    }

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
        if (!TextUtils.isEmpty(binding.editPhone.getText()) || !TextUtils.isEmpty(binding.editPhone.getText())){
            login();
            
        }else {
            Toast.makeText(LoginActivity.this, "로그인 정보를 정확히 입력해 주세요", Toast.LENGTH_SHORT).show();
        }

    }

    private void login(){
        RetrofitGenerator retrofitGenerator = new RetrofitGenerator();
        APIService apiService = retrofitGenerator.getApiService();

        apiService.userLogin(binding.editPhone.getText().toString(), binding.editPassword.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("result JOIN", result);
                if (result.equals("true")){
                    moveToMain();
                    Toast.makeText(LoginActivity.this, "로그인 성공!!" + result, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "로그인 실패!!" + result, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void moveToMain(){

        if (binding.autoLoginRbt.isChecked()){
            setShearedString("phone", binding.editPhone.getText().toString());
            setShearedString("password", binding.editPassword.getText().toString());
        }else if (binding.saveIdRbt.isChecked()){
            setShearedString("phone", binding.editPhone.getText().toString());
        }
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

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