package com.sdin.tourstamprally.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivitySignUpBinding;
import com.sdin.tourstamprally.databinding.ActivityTermsOfConditionsBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();

    }

    private void initView(){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinnerLocation.setAdapter(adapter);
    }

    public void authResponse(){
        // TODO: 6/10/21 전화번호 인증
    }

    public void authCheck(){
        // TODO: 6/10/21 인증확인
    }

    public void moveBack(){
        finish();
    }


    public void signUp(){

        // TODO: 6/10/21 전화번호 인증ㄱㄱ

        if (!TextUtils.isEmpty(binding.editPassword.getText())){

            // TODO: 6/10/21 패스워드 체크

        }else if (!TextUtils.isEmpty(binding.editPasswordConfirm.getText())){

            // TODO: 6/10/21 패스워드 확인 체크

        }else if (!TextUtils.isEmpty(binding.editName.getText())){

            // TODO: 6/10/21 이름 체크

        }else if (!TextUtils.isEmpty(binding.editEmail.getText())){

            // TODO: 6/10/21 이메일 체크

        }else {
            join();
        }
    }

    private void join(){

        // TODO: 6/10/21 회원가입ㄱㄱ
    }
}