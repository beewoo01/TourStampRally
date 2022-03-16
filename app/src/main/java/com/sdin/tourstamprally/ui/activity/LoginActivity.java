package com.sdin.tourstamprally.ui.activity;

import androidx.annotation.NonNull;
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
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.ActivityLoginBinding;
import com.sdin.tourstamprally.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


        if (!TextUtils.isEmpty(getIntent().getStringExtra("phone"))) {
            binding.editPhone.setText(getIntent().getStringExtra("phone"));
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra("psw"))) {
            binding.editPassword.setText(getIntent().getStringExtra("psw"));
        }

    }


    public void autoSaveClick(View view) {
        if (binding.autoLoginRbt.isChecked() && binding.saveIdRbt.isChecked()) {
            if (view.getId() == R.id.auto_login_rbt) {
                binding.saveIdRbt.setChecked(false);
            } else {
                binding.autoLoginRbt.setChecked(false);
            }
        }
    }


    public void validation() {
        if (!TextUtils.isEmpty(binding.editPhone.getText()) || !TextUtils.isEmpty(binding.editPassword.getText())) {
            phone = binding.editPhone.getText().toString();
            psw = binding.editPassword.getText().toString();
            login();

        } else {
            Toast.makeText(LoginActivity.this, "로그인 정보를 정확히 입력해 주세요", Toast.LENGTH_SHORT).show();
        }

    }

    private void login() {

        apiService.userLoginExists(phone, psw).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                String result = response.body();
                //Log.d("result JOIN", result);
                if (result != null) {
                    if (result.equals("true")) {
                        saveInfo();
                    } else {
                        Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void saveInfo() {
        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.wtf("LoginActivity",
                                "Fetching FCM registration token failed" , task.getException());

                    }

                    String token = task.getResult();
                    Log.wtf("LoginActivity", "token =" + token);

                });*/

        apiService.userLogin(phone, psw).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NotNull Call<UserModel> call, @NotNull Response<UserModel> response) {
                UserModel result = response.body();
                //Log.d("result JOIN", result.toString());
                try {
                    if (result == null || result.equals("null")) {

                        Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        if (result.getEnable().equals("0")) {
                            if (binding.autoLoginRbt.isChecked()) {
                                setShearedString("phone", binding.editPhone.getText().toString());
                                setShearedString("password", binding.editPassword.getText().toString());

                            } else if (binding.saveIdRbt.isChecked()) {
                                setShearedString("phone", binding.editPhone.getText().toString());
                            }



                            Utils.UserPhone = binding.editPhone.getText().toString();
                            Utils.UserPassword = binding.editPassword.getText().toString();
                            Utils.User_Idx = result.getUserIdx();
                            Utils.User_Name = result.getName();
                            Utils.User_Email = result.getEmail();
                            Utils.User_Location = result.getLocation();
                            Utils.User_Profile = result.getUser_profile();

                            moveToMain();
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void moveToMain() {
        Set<String> set = getIds();
        boolean isNotFirstLogin = true;
        Intent intent;
        if (set != null) {
            List<String> list = new ArrayList<>(set);
            isNotFirstLogin = list.contains(binding.editPhone.getText().toString());
            intent = new Intent(LoginActivity.this, MainActivity2.class);
            intent.putExtra("isNotFirstLogin", isNotFirstLogin);
            if (!isNotFirstLogin) {
                list.add(binding.editPhone.getText().toString());
                Set<String> saveSet = new HashSet<>(list);
                setShearedStringSet(saveSet);
            }
        } else {
            Set<String> saveSet = new HashSet<>();
            saveSet.add(binding.editPhone.getText().toString());
            setShearedStringSet(saveSet);
            intent = new Intent(LoginActivity.this, MainActivity2.class);
            intent.putExtra("isNotFirstLogin", isNotFirstLogin);
        }




        startActivity(intent);
        finish();
    }

    private SharedPreferences setSharedPref() {
        return getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE);
    }

    private void setShearedString(String key, String value) {
        SharedPreferences preferences = setSharedPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void setShearedStringSet(Set<String> value) {
        SharedPreferences preferences = setSharedPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("phones", value);
        editor.apply();
    }


    private Set<String> getIds() {
        SharedPreferences preferences = setSharedPref();
        return preferences.getStringSet("phones", null);
    }


    public void Forgot_psw() {
        startActivity(new Intent(this, FindPasswordActivity.class));
    }

    public void SignUp() {
        startActivity(new Intent(this, TermsOfConditionsActivity.class));
    }

}