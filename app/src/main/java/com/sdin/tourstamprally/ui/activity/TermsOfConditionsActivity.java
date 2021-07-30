package com.sdin.tourstamprally.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivityTermsOfConditionsBinding;

public class TermsOfConditionsActivity extends AppCompatActivity {

    private ActivityTermsOfConditionsBinding binding;
    boolean isAllCheck = false;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_of_conditions);
        binding.setActivity(this);
        activity = this;
    }

    public void chackBoxClick(){
        if (binding.serviceTermRdb.isChecked() && binding.personalTermsRdb.isChecked()){
            binding.allAgreeCheckbox.setChecked(true);
        }else {
            binding.allAgreeCheckbox.setChecked(false);
        }
    }

    public void allagree(View view){

        if (view.getId() == R.id.all_agree){
            if (binding.allAgreeCheckbox.isChecked()){
                binding.allAgreeCheckbox.setChecked(false);
            }else {
                binding.allAgreeCheckbox.setChecked(true);
            }

        }

        if (binding.allAgreeCheckbox.isChecked()){
            isAllCheck = false;
        }else {
            isAllCheck = true;
        }

        if (isAllCheck){

            binding.serviceTermRdb.setChecked(false);
            binding.personalTermsRdb.setChecked(false);
            binding.allAgreeCheckbox.setChecked(false);

        }else {

            binding.serviceTermRdb.setChecked(true);
            binding.personalTermsRdb.setChecked(true);
            binding.allAgreeCheckbox.setChecked(true);

        }

    }

    public void nextBtnClick(){
        if (binding.serviceTermRdb.isChecked() && binding.personalTermsRdb.isChecked()){
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        }else {
            Toast.makeText(this, "약관에 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveBack(){

        finish();
    }
}