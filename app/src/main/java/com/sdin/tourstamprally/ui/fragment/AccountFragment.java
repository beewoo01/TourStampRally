package com.sdin.tourstamprally.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentAccountBinding;
import com.sdin.tourstamprally.model.AllReviewDTO;
import com.sdin.tourstamprally.model.UserModel;
import com.sdin.tourstamprally.ui.activity.LoginActivity;
import com.sdin.tourstamprally.ui.dialog.UserWithdrawalDialog;
import com.sdin.tourstamprally.ui.dialog.UserWithdrawalSuccessDialog;
import com.sdin.tourstamprally.utill.FtpThread;
import com.sdin.tourstamprally.utill.ItemOnClickAb;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
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
        editor.apply();

        startActivity(new Intent(requireActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    private void setData() {
        /*String[] arr = getResources().getStringArray(R.array.location);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(new ArrayList(Arrays.asList(arr)));
        binding.spinnerLocation.setAdapter(spinnerAdapter);*/

        ArrayAdapter adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.location, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLocation.setAdapter(adapter);

        binding.spinnerLocation.setSelection(adapter.getPosition(Utils.User_Location));
        binding.spinnerLocation.getSelectedItem().toString();


        binding.editName.setText(Utils.User_Name);
        binding.editEmail.setText(Utils.User_Email);
        binding.editPhone.setText(Utils.UserPhone);



        Glide.with(requireContext()).load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile)
                .error(ContextCompat.getDrawable(requireContext(), R.drawable.sample_profile_image)).circleCrop()
                .into(binding.profileImb);
    }

    public void profileSetOnClick() {
        Log.wtf("profileSetOnClick", "11111");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryResult.launch(intent);
        /*startActivityForResult(intent, REQUEST_CODE);*/
    }

    private final ActivityResultLauncher<Intent> galleryResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK) {

                    Intent intent = result.getData();
                    if (intent != null) {
                        Glide.with(requireContext()).load(intent.getData()).circleCrop()
                                .error(ContextCompat.getDrawable(requireContext(), R.drawable.sample_profile_image))
                                .into(binding.profileImb);

                        imgeUri = intent.getData();
                        isChange = true;
                    } else {
                        Toast.makeText(requireContext(), "사진을 가져오는데 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }

                }else if (result.getResultCode() == RESULT_CANCELED) {

                    Toast.makeText(requireContext(), "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
            }
    );


    public void authResponse() {

        if (TextUtils.isEmpty(binding.editPhone.getText().toString())
                || binding.editPhone.getText().toString().length() < 11
                || binding.editPhone.getText().toString().length() > 11) {
            showToast("전화번호를 정확히 입력해 주세요");
            return;
        }

        /*if (Utils.UserPhone.equals(binding.editPhone.getText().toString())){
            showToast("인증이 완료된 번호입니다.");
            return;
        }*/

        apiService.getToken().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    String Token = response.body();
                    if (Token != null) {
                        sendMsg(Token);
                        binding.btnRequestAuth.setEnabled(false);
                        binding.editPhone.setEnabled(false);
                    } else {
                        binding.btnRequestAuth.setEnabled(true);
                    }


                } else {
                    binding.btnRequestAuth.setEnabled(true);
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                t.printStackTrace();
                binding.btnRequestAuth.setEnabled(true);
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
                    Log.d("response not Succes", response.toString());
                }

            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                showToast(getString(R.string.fail_sms));
                t.printStackTrace();
            }
        });
    }

    public void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }


    private boolean specialCheck(String str) {
        return str == null || !str.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*");
    }



    public void signUp() {

        if (specialCheck(binding.editPassword.getText().toString())){
            showToast("비밀번호는 특수문자없이 작성해 주세요");
            return;
        }

        if (specialCheck(binding.editName.getText().toString())){
            showToast("이름은 특수문자없이 작성해 주세요");
            return;
        }

        if (specialCheck(binding.editPasswordConfirm.getText().toString())){
            showToast("비밀번호는 특수문자없이 작성해 주세요");
            return;
        }




        Pattern pattern = Patterns.EMAIL_ADDRESS;
        //Log.wtf("signUp", "signUp");

        if (!isAuth) {
            showToast("전화번호를 인증해 주세요");
            return;
        }

        if (TextUtils.isEmpty(binding.editPassword.getText().toString().trim())
                || binding.editPassword.getText().toString().trim().length() < 8) {


            showToast("비밀번호는 8자 이상입니다.");
            return;
        }

        if (!binding.editPassword.getText().toString().trim().equals(
                        binding.editPasswordConfirm.getText().toString().trim())) {

            showToast("비밀번호가 일치하지 않습니다.");

        } else if (TextUtils.isEmpty(binding.editName.getText().toString().trim())
                || binding.editName.getText().toString().trim().length() < 2
                || binding.editName.getText().toString().trim().length() > 8) {

            showToast("이름은 2자 이상 8자 이하로 등록해주세요.");

        } else if (TextUtils.isEmpty(binding.editEmail.getText().toString().trim())
                || !pattern.matcher(binding.editEmail.getText().toString()).matches()) {

            showToast("이메일을 정확히 입력해 주세요.");

        } else {
            update();
        }
    }

    public void authCheck() {
        if (!TextUtils.isEmpty(binding.editAuth.getText().toString())) {
            if (binding.editAuth.getText().toString().equals(auth)) {
                isAuth = true;
                showToast("전화번호 인증에 성공하셨습니다.");
                binding.editPhone.setEnabled(false);
                binding.btnRequestAuth.setEnabled(false);
                binding.editAuth.setEnabled(false);
                binding.btnAuthCheck.setEnabled(false);

            } else {
                showToast("인증번호를 정확히 입력해 주세요");
            }
        } else {
            showToast("인증번호를 정확히 입력해 주세요");
        }
    }





    private void update() {
        //Log.wtf("update", "update");
        //uploadProfile();

        UserModel userModel = new UserModel();
        userModel.setPhone(binding.editPhone.getText().toString());
        userModel.setPassword(binding.editPassword.getText().toString());
        userModel.setName(binding.editName.getText().toString());
        userModel.setEmail(binding.editEmail.getText().toString());
        userModel.setLocation(binding.spinnerLocation.getSelectedItem().toString());

        if (isChange) {
            Date date = new Date(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            String getTime = sdf.format(date);
            String fileName = getTime + ".jpg";
            userModel.setUser_profile(fileName);
            uploadProfile(fileName);
        } else {
            userModel.setUser_profile(Utils.User_Profile);
        }


        apiService.user_update(Utils.User_Idx, userModel.getPhone(), userModel.getPassword(),
                userModel.getName(), userModel.getEmail(), userModel.getLocation(), userModel.getUser_profile()).enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {

                        if (response.isSuccessful()){
                            String result = response.body();

                            if (result != null) {
                                if (result.equals("1")) {

                                    Utils.UserPhone = userModel.getPhone();
                                    Utils.User_Email = userModel.getEmail();
                                    Utils.User_Location = userModel.getLocation();
                                    Utils.User_Name = userModel.getName();
                                    Utils.User_Profile = userModel.getUser_profile();
                                    Utils.UserPassword = userModel.getPassword();

                                    SharedPreferences preferences = setSharedPref();
                                    final String phone = preferences.getString("phone", "");
                                    final String psw = preferences.getString("password", "");
                                    if (!TextUtils.isEmpty(phone)){
                                        setShearedString("phone", binding.editPhone.getText().toString());
                                    }

                                    if (!TextUtils.isEmpty(psw)){
                                        setShearedString("password", binding.editPassword.getText().toString());
                                    }



                                    showToast("업데이트 성공");

                                } else {
                                    showToast("업데이트에 실패하였습니다.");
                                }
                            }else {
                                showToast("업데이트에 실패하였습니다.");
                            }
                        }else {
                            showToast("업데이트에 실패하였습니다.");
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        t.printStackTrace();
                        showToast("업데이트에 실패하였습니다.");
                    }
                });

    }


    private void setShearedString(String key, String value) {
        SharedPreferences preferences = setSharedPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    private SharedPreferences setSharedPref() {
        return requireContext().getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE);
    }

    private void uploadProfile(String fileName) {

        new FtpThread(imgeUri, requireContext(), fileName).start();

    }



    public void signout(){
        UserWithdrawalDialog dialog = new UserWithdrawalDialog(requireContext());
        dialog.setClickListener(new ItemOnClickAb() {
            @Override
            public void signOutListener() {
                showSuccessDialog();

                //realSignOut();
            }
        });
        dialog.show();
    }

    private void showSuccessDialog(){
        UserWithdrawalSuccessDialog successDialog = new UserWithdrawalSuccessDialog(requireContext());
        successDialog.setListener(new ItemOnClickAb() {
            @Override
            public void signOutListener() {
                realSignOut();
            }
        });
        successDialog.show();


    }

    private void realSignOut(){



        apiService.userwithdrawal(Utils.User_Idx).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer integer) {

                        SharedPreferences preferences = setSharedPref();
                        final String phone = preferences.getString("phone", "");
                        final String psw = preferences.getString("password", "");
                        if (!TextUtils.isEmpty(phone)){
                            removePrefer("phone");

                        }
                        if (!TextUtils.isEmpty(psw)){
                            removePrefer("password");
                        }

                        startActivity(new Intent(requireActivity(), LoginActivity.class));
                        requireActivity().finish();


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }


    private void removePrefer(String name){
        SharedPreferences preferences = setSharedPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(name);
        editor.apply();
    }


}