package com.sdin.tourstamprally.ui.activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivitySignUpBinding;
import com.sdin.tourstamprally.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;
    private boolean isAuth = false;
    private String auth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setActivity(this);
        initView();

    }

    @Override
    protected void initView() {
        String[] arr = getResources().getStringArray(R.array.location);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(new ArrayList<>(Arrays.asList(arr)));
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //binding.spinnerLocation.setAdapter(spinnerAdapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>(Arrays.asList(arr))
        );
        binding.spinnerLocation.setAdapter(arrayAdapter);
        binding.editPhone.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            binding.btnRequestAuth.setEnabled(binding.editPhone.getText().toString().length() == 11);
        }
    };

    public void authResponse() {

        if (TextUtils.isEmpty(binding.editPhone.getText().toString())) {
            showToast("휴대폰번호를 입력해 주세요");
            return;
        }

        apiService.getToken().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    String Token = response.body();
                    if (Token != null || !Token.equals("error")) {
                        sendMsg(Token);
                    } else {
                        binding.btnRequestAuth.setEnabled(true);
                        showToast(getString(R.string.fail_sms));
                    }


                } else {
                    binding.btnRequestAuth.setEnabled(true);
                    showToast(getString(R.string.fail_sms));
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                t.printStackTrace();
                binding.btnRequestAuth.setEnabled(true);
                showToast("SMS발송 요청을 실패하였습니다.");
                Log.e("getToken", t.getMessage());
            }
        });
    }

    private void sendMsg(String Token) {
        showToast("SMS발송 요청을 하셨습니다.");
        apiService.getAutoNumber(binding.editPhone.getText().toString(), Token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {

                    auth = response.body();

                } else {
                    showToast(getString(R.string.fail_sms));
                    Log.d("Auth", "실패");
                }

            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                showToast(getString(R.string.fail_sms));
                t.printStackTrace();
                Log.e("getToken", t.getMessage());
            }
        });
    }

    public void authCheck() {
        if (!TextUtils.isEmpty(binding.editAuth.getText().toString())) {
            if (binding.editAuth.getText().toString().equals(auth)) {
                isAuth = true;
                showToast("휴대폰번호 인증에 성공하셨습니다.");
                binding.editPhone.setEnabled(false);
                binding.editAuth.setEnabled(false);
                binding.btnRequestAuth.setEnabled(false);
                binding.btnAuthCheck.setEnabled(false);
            } else {
                showToast("인증번호를 정확히 입력해 주세요");
            }
        } else {
            showToast("인증번호를 정확히 입력해 주세요");
        }
    }

    public void moveBack() {
        finish();
    }


    public void signUp() {

        Pattern pattern = Patterns.EMAIL_ADDRESS;

        if (!isAuth) {
            showToast("전화번호를 인증해 주세요");
            return;
        }

        if (TextUtils.isEmpty(binding.editPassword.getText().toString().trim())
                || binding.editPassword.getText().toString().trim().length() < 8) {

            showToast("비밀번호는 8자 이상입니다.");

        } else if (!binding.editPassword.getText().toString().trim().equals(
                binding.editPasswordConfirm.getText().toString().trim())) {

            showToast("비밀번호가 일치하지 않습니다.");

        } else if (TextUtils.isEmpty(binding.editName.getText().toString().trim())
                || binding.editName.getText().toString().trim().length() > 8
                || binding.editName.getText().toString().trim().length() < 2) {

            showToast("이름은 2자 이상 8자 이하로 등록해 주세요");

        } else if (TextUtils.isEmpty(binding.editEmail.getText().toString().trim())
                || !pattern.matcher(binding.editEmail.getText().toString().trim()).matches()) {

            showToast("이메일을 입력해 주세요.");

        } else {
            join();
        }
    }

    private void join() {
        UserModel userModel = new UserModel();
        //userModel.setPhone(binding.editPhone.getText().toString().trim());
        userModel.setPhone(binding.editPhone.getText().toString().trim());
        userModel.setPassword(binding.editPassword.getText().toString().trim());
        userModel.setName(binding.editName.getText().toString().trim());
        userModel.setEmail(binding.editEmail.getText().toString().trim());
        userModel.setLocation(binding.spinnerLocation.getSelectedItem().toString());

        apiService.userPhoneExists(userModel.getPhone()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    if (result != null) {
                        if (result.equals("true")) {
                            userSignUp(userModel);
                        } else {
                            Toast.makeText(SignUpActivity.this, "이미 등록된 번호 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showToast("회원가입에 실패하였습니다.");
                    }
                } else {
                    showToast("회원가입에 실패하였습니다.");
                }

            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                Log.wtf("SignUp", t.getMessage());
            }
        });


    }

    private void userSignUp(UserModel userModel) {
        apiService.userSignUp(userModel.getPhone(), userModel.getPassword(), userModel.getName()
                , userModel.getEmail(), userModel.getLocation(), 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {

                if (response.isSuccessful()) {
                    String result = response.body();
                    if (result != null) {
                        if (result.equals("1")) {
                            showToast("회원가입 성공");
                            /*TermsOfConditionsActivity activity = (TermsOfConditionsActivity) TermsOfConditionsActivity.activity;
                            activity.finish();*/
                            finish();
                        } else {
                            showToast("회원가입에 실패하였습니다.");
                        }
                    } else {
                        showToast("회원가입에 실패하였습니다.");
                    }
                } else {
                    showToast("회원가입에 실패하였습니다.");
                }


            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                t.printStackTrace();
                showToast("서버에 문제가 있습니다.");
            }
        });
    }


    private class SpinnerAdapter extends BaseAdapter {

        private final ArrayList<String> data;

        public SpinnerAdapter(ArrayList<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            if (data != null) return data.size();
            else return 0;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_title_item, parent, false);
            }

            if (data != null) {
                String text = data.get(position);
                ((TextView) convertView.findViewById(R.id.spinnerTitleText)).setText(text);
            }
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
            }

            String text = data.get(position);
            ((TextView) convertView.findViewById(R.id.spinnerText)).setText(text);
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int size = Math.round(20 * dm.density);
            TextView textView = convertView.findViewById(R.id.spinnerText);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = size;
            params.setMargins(10,5,10,5);
            textView.setLayoutParams(params);

            return convertView;
        }
    }
}