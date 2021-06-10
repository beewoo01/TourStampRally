package com.sdin.tourstamprally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sdin.tourstamprally.databinding.ActivityFindPasswordBinding;
import com.sdin.tourstamprally.databinding.ActivityTermsOfConditionsBinding;

public class FindPasswordActivity extends AppCompatActivity {

    private ActivityFindPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);

    }

    public void findPsw(){

        if (TextUtils.isEmpty(binding.editName.getText())
                && TextUtils.isEmpty(binding.editPhoneNumber.getText())
                && TextUtils.isEmpty(binding.editEmail.getText())){

            Toast.makeText(this, "빈칸 없이 입력해 주세요", Toast.LENGTH_SHORT).show();

        }else {
            // TODO: 6/10/21 비밀번호 찾기 ㄱㄱ
        }

    }
}