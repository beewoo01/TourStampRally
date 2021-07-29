package com.sdin.tourstamprally.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentAccountBinding;
import com.sdin.tourstamprally.model.UserModel;
import com.sdin.tourstamprally.ui.activity.LoginActivity;
import com.sdin.tourstamprally.utill.FtpThread;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends BaseFragment {

    private static final String ARG_PARAM = "model";

    private UserModel model;
    private String auth = null;
    private boolean isAuth = false;

    private Uri imgeUri;
    private boolean isChange = false;

    private static final int REQUEST_CODE = 1000;

    private FragmentAccountBinding binding;


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(UserModel model) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (UserModel) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container,false);
        binding.setFragment(this);
        setData();
        return binding.getRoot();
    }


    public void logout() {
        Log.d(TAG, "logout");
        SharedPreferences pref = requireContext().getSharedPreferences("rebuild_preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("phone");
        editor.remove("password");
        editor.commit();

        startActivity(new Intent(requireActivity(), LoginActivity.class));
        requireActivity().finish();


    }

    private void setData(){
        String[] arr = getResources().getStringArray(R.array.location);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(new ArrayList(Arrays.asList(arr)));
        binding.spinnerLocation.setAdapter(spinnerAdapter);
        binding.editPhone.addTextChangedListener(textWatcher);

        binding.editName.setText(Utils.User_Name);
        binding.editEmail.setText(Utils.User_Email);

        Glide.with(requireContext()).load("http://zzipbbong.cafe24.com/imagefile/bsr/" + Utils.User_Profile)
                .error(ContextCompat.getDrawable(requireContext(), R.drawable.sample_profile_image)).circleCrop()
                .into(binding.profileImb);
    }

    public void profileSetOnClick(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
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

        if (TextUtils.isEmpty(binding.editPhone.getText().toString())){
            showToast("휴대폰번호를 입력해 주세요");
            return;
        }

        apiService.getToken().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    String Token = response.body();
                    sendMsg(Token);
                    binding.btnRequestAuth.setEnabled(false);

                }else {
                    binding.btnRequestAuth.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                binding.btnRequestAuth.setEnabled(true);
            }
        });
    }

    private void sendMsg(String Token){
        showToast("SMS발송 요청을 하셨습니다.");
        apiService.getAutoNumber(binding.editPhone.getText().toString(), Token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    auth = response.body();
                }else {
                    showToast("서버에서 문제가 발생했습니다.");
                    Log.d("Auth", "실패");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("서버에서 문제가 발생했습니다.");
                t.printStackTrace();
            }
        });
    }

    public void showToast(String msg){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void signUp() {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Log.wtf("signUp", "signUp");

        /*if (TextUtils.isEmpty(binding.editPassword.getText()) || binding.editPassword.getText().toString().length() > 8) {

            showToast("비밀번호는 8자 이상입니다.");

        }*/

        if (false){
            Log.wtf("1111111111111111", "11111111111111111");
        } else if (TextUtils.isEmpty(binding.editPasswordConfirm.getText()) && TextUtils.isEmpty(binding.editPasswordConfirm.getText())
                && binding.editPassword.getText().equals(binding.editPasswordConfirm.getText())) {
            Log.wtf("22222222222222222", "22222222222");
            showToast("비밀번호가 일치하지 않습니다.");

        } else if (TextUtils.isEmpty(binding.editName.getText())) {
            Log.wtf("signUp", "signUp");
            showToast("이름을 입력해 주세요.");

        } else if (TextUtils.isEmpty(binding.editEmail.getText()) && !pattern.matcher(binding.editEmail.getText().toString()).matches()) {
            Log.wtf("signUp", "signUp");
            showToast("이메일을 입력해 주세요.");

        } else {
            update();
        }
    }

    public void authCheck() {
        if (!TextUtils.isEmpty(binding.editAuth.getText().toString())){
            if (binding.editAuth.getText().toString().equals(auth)){
                isAuth = true;
                showToast("휴대폰번호 인증에 성공하셨습니다.");
            }else {
                showToast("인증번호를 정확히 입력해 주세요");
            }
        }else {
            showToast("인증번호를 정확히 입력해 주세요");
        }
    }


    private void update() {
        Log.wtf("update", "update");
        //uploadProfile();

        UserModel userModel = new UserModel();
        userModel.setPhone(binding.editPhone.getText().toString());
        userModel.setPassword(binding.editPassword.getText().toString());
        userModel.setName(binding.editName.getText().toString());
        userModel.setEmail(binding.editEmail.getText().toString());
        userModel.setLocation(binding.spinnerLocation.getSelectedItem().toString());

        if (isChange){
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            String getTime = sdf.format(date);
            String fileName = getTime + ".jpg";
            userModel.setUser_profile(fileName);
            uploadProfile(fileName);
        }else {
            userModel.setUser_profile(Utils.User_Profile);
        }



        apiService.user_update( Utils.User_Idx,userModel.getPhone(), userModel.getPassword(), userModel.getName(), userModel.getEmail(), userModel.getLocation(), userModel.getUser_profile()).enqueue(
                new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String result1 = String.valueOf(response.body());
                String result = response.body();
                Log.d("result", result);
                Log.d("result1", result1);
                Log.d("response", response.toString());

                if (result.equals("1")) {
                    showToast("업데이트 성공");
                } else {
                    showToast("업데이트에 실패하였습니다.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                showToast("서버에 문제가 있습니다.");
            }
        });

    }

    private void uploadProfile(String fileName){

        new FtpThread(imgeUri, requireContext(), fileName).start();

    }

    /*ActivityResultLauncher<Intent> requestGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Uri uri = result.getData().getData();
        Log.d("uri??", uri.toString());

        File file = new File(uri.getPath());
        if (file == null) {
            Log.d("file is null?", "null..");
        } else {
            String realPath = getRealPathFromURI(uri);

            model.setImgUri(uri);
            uploadImage(uri, 0, realPath);

        }


    });*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK)
            {
                Glide.with(requireContext()).load(data.getData()).circleCrop().error(ContextCompat.getDrawable(requireContext(), R.drawable.sample_profile_image))
                        .into(binding.profileImb);
                imgeUri = data.getData();
                isChange = true;
                //new FtpThread(imgeUri.toString());
                //uploadProfile();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(requireContext(), "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
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
            textView.setLayoutParams(params);

            return convertView;
        }
    }
}