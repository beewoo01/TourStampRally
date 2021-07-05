package com.sdin.tourstamprally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sdin.tourstamprally.databinding.ActivityFindPasswordBinding;
import com.sdin.tourstamprally.databinding.ActivityTermsOfConditionsBinding;
import com.sdin.tourstamprally.ui.activity.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPasswordActivity extends BaseActivity {

    private ActivityFindPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        binding.setActivity(this);
        binding.backBtn.setOnClickListener( v -> {
            finish();
        });
    }

    public void findPsw(){

        if (TextUtils.isEmpty(binding.editName.getText())
                && TextUtils.isEmpty(binding.editPhoneNumber.getText())
                && TextUtils.isEmpty(binding.editEmail.getText())){

            Toast.makeText(this, "빈칸 없이 입력해 주세요", Toast.LENGTH_SHORT).show();

        }else {
            apiService.find_Password(binding.editName.getText().toString(), binding.editPhoneNumber.getText().toString(),
                    binding.editEmail.getText().toString()).enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("FindPasswordActivity", "onResponse");
                    if (response.isSuccessful()){
                        String result1 = String.valueOf(response.body());
                        String result = response.body();
                        Log.d("result", result);
                        Log.d("result1", result1);
                        Log.d("response", response.toString());

                        Log.d("password?", result);
                        if (!result.equals("false")){
                            showToast(result);
                        }else {
                            showToast("정확한 정보를 입력해주세요");
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }
}