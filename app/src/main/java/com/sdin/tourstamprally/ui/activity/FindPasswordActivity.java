package com.sdin.tourstamprally.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivityFindPasswordBinding;

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
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private boolean specialCheck(String str) {
        return str == null || !str.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*");
    }

    public void findPsw() {
        

       /* if (specialCheck(binding.editPassword.getText().toString())){
            showToast("특수문자없이 작성하여 주세요.");
            return;
        }*/

        if (TextUtils.isEmpty(binding.editName.getText())
                || TextUtils.isEmpty(binding.editPhoneNumber.getText().toString().trim())
                || TextUtils.isEmpty(binding.editEmail.getText().toString().trim())
                /*|| TextUtils.isEmpty(binding.editPassword.getText().toString().trim())
                || TextUtils.isEmpty(binding.editPasswordConfirm.getText().toString().trim())*/) {

            showToast("빈칸 없이 입력해 주세요");

        } /*else if (!binding.editPassword.getText().toString().equals(binding.editPasswordConfirm.getText().toString())) {

            showToast("비밀번호가 일치하지 않습니다.");

        } else if (binding.editPassword.getText().toString().trim().length() < 8) {

            showToast("비밀번호는 8자 이상으로 등록해 주세요");


        }*/ else {
            apiService.update_user_psw(binding.editName.getText().toString(), binding.editPhoneNumber.getText().toString(),
                    binding.editEmail.getText().toString()/*, binding.editPassword.getText().toString()*/).enqueue(new Callback<Integer>() {

                @Override
                public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                    if (response.isSuccessful()) {
                        Integer result = response.body();
                        if (result != null) {
                            /**
                             * 0 ~ -1 -> Error
                             * -2 -> email not equals
                             * -3 -> name not equals
                             * */

                            switch (result) {
                                case -2 : {
                                    showToast("이메일 주소가 일치하지 않습니다.");
                                    break;
                                }

                                case -3 : {
                                    showToast("이름이 일치하지 않습니다.");
                                    break;
                                }

                                case 0 :
                                case -1 : {
                                    showToast("비밀번호 변경에 실패하였습니다.");
                                    break;
                                }

                                default: {
                                    showToast("비밀번호 변경을 완료하였습니다.");
                                    finish();
                                    break;
                                }
                            }

                        } else {
                            showToast("비밀번호 변경에 실패하였습니다.");
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    showToast("비밀번호 변경에 실패하였습니다.");
                }
            });
        }

    }
}