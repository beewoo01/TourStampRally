package com.sdin.tourstamprally.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivityTermsOfConditionsBinding;

public class TermsOfConditionsActivity extends AppCompatActivity {

    private ActivityTermsOfConditionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_of_conditions);
    }

    public void allagree(){
        binding.serviceTermRdb.setChecked(true);
        binding.personalTermsRdb.setChecked(true);
    }

    public void nextBtnClick(){
        if (binding.serviceTermRdb.isChecked() && binding.personalTermsRdb.isChecked()){
            startActivity(new Intent(this, SignUpActivity.class));
        }else {
            Toast.makeText(this, "약관에 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveBack(){
        finish();
    }
}