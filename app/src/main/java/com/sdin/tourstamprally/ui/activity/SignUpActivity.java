package com.sdin.tourstamprally.ui.activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivitySignUpBinding;
import com.sdin.tourstamprally.model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;

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
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(new ArrayList(Arrays.asList(arr)));
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinnerLocation.setAdapter(spinnerAdapter);
    }

    public void authResponse() {

        if (TextUtils.isEmpty(binding.editPhone.getText().toString())){
            showToast("휴대폰번호를 입력해 주세요");
            return;
        }

        apiService.getToken().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    String Token = response.body();
                    Log.d("Token3", Token);
                    sendMsg(Token);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
        // TODO: 6/10/21 전화번호 인증
    }

    private void sendMsg(String Token){
        Log.wtf("phone", binding.editPhone.getText().toString());
        Log.wtf("Token", Token);
        apiService.getAutoNumber(binding.editPhone.getText().toString(), Token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    auth = response.body();
                    Log.d("Auth", auth);
                }else {

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void authCheck() {
        if (!TextUtils.isEmpty(binding.editAuth.getText().toString())){
            if (binding.editAuth.getText().toString().equals(auth)){
                isAuth = true;
                showToast("휴대폰번호 인증에 성공하셨습니다.");
                Log.d("Auth?", auth);
                Log.d("Auth?", binding.editAuth.getText().toString());
            }else {
                showToast("인증번호를 정확히 입력해 주세요");
            }
        }else {
            showToast("인증번호를 정확히 입력해 주세요");
        }
    }

    public void moveBack() {
        finish();
    }


    public void signUp() {

        // TODO: 6/10/21 전화번호 인증ㄱㄱ
        if (!isAuth){
            showToast("전화번호를 인증해 주세요");
            return;
        }
        Log.d("SignUpActivity", "여기오면 안되는데?");

        if (TextUtils.isEmpty(binding.editPassword.getText())) {

            // TODO: 6/10/21 패스워드 체크
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(binding.editPasswordConfirm.getText())
                && binding.editPassword.getText().equals(binding.editPasswordConfirm.getText())) {

            // TODO: 6/10/21 패스워드 확인 체크
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(binding.editName.getText())) {

            // TODO: 6/10/21 이름 체크
            Toast.makeText(this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(binding.editEmail.getText())) {

            // TODO: 6/10/21 이메일 체크
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();

        } else {
            join();
        }
    }

    private void join() {
        UserModel userModel = new UserModel();
        userModel.setPhone(binding.editPhone.getText().toString());
        userModel.setPassword(binding.editPassword.getText().toString());
        userModel.setName(binding.editName.getText().toString());
        userModel.setEmail(binding.editEmail.getText().toString());
        userModel.setLocation(binding.spinnerLocation.getSelectedItem().toString());


        apiService.userSignUp(userModel.getPhone(), userModel.getPassword(), userModel.getName()
                , userModel.getEmail(), userModel.getLocation(), 1, 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result1 = String.valueOf(response.body());
                Log.d("result11111 login", result1);
                String result = response.body().toString();
                Log.d("result login!!!!", result);
                if (response.equals("1")) {
                    Toast.makeText(SignUpActivity.this, "회원가입 성공!!!!" + result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "회원가입 실패!!!!" + result, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    class SpinnerAdapter extends BaseAdapter {

        private ArrayList<String> data;

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
            textView.setLayoutParams(params);

            return convertView;
        }
    }
}