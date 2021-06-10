package com.sdin.tourstamprally.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.sdin.tourstamprally.FindPasswordActivity;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // TODO: 6/10/21 자동로그인 OR 아이디저장 확인

    }


    public void Login(){
        if (!TextUtils.isEmpty(binding.editPhone.getText()) || !TextUtils.isEmpty(binding.editPhone.getText())){
            // TODO: 6/10/21 로그인
        }else {
            // TODO: 6/10/21 return
        }

        if (binding.autoLoginRbt.isChecked()){
            // TODO: 6/10/21 자동로그인 ㄱㄱ
        }else {
            // TODO: 6/10/21 id저장 ㄱㄱ
        }

        startActivity(new Intent(this, MainActivity.class));

    }

    public void Forgot_psw(){
        startActivity(new Intent(this, FindPasswordActivity.class));
    }

    public void SignUp(){
        startActivity(new Intent(this, TermsOfConditionsActivity.class));
    }

}