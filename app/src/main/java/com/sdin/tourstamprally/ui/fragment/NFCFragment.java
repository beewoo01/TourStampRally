package com.sdin.tourstamprally.ui.fragment;

import android.content.Intent;
import android.graphics.Outline;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentNfcBinding;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.ScanResultDialog;
import com.sdin.tourstamprally.utill.NFCListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NFCFragment extends BaseFragment implements NFCListener {

    private static final String DataArray = "param1";
    private static final String Listener = "param2";
    private NfcAdapter nfcAdapter;

    // TODO: Rename and change types of parameters
    private NdefMessage[] dataArray;
    private FragmentNfcBinding binding;

    public NFCFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dataArray = (NdefMessage[]) getArguments().getParcelableArray(DataArray);
            buildtagViews(dataArray);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }else {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnListener(this);
        }
    }


    private void buildtagViews(NdefMessage[] msgs) {

        if (msgs == null || msgs.length == 0) return;

        String text = "";
        String text2 = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;

        try {
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            String[] strs = text.split("\\|");
            text2 = strs[1];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.wtf("NFC 정보 text!", text);
        Log.wtf("NFC 정보 text22!", text2);

        Toast.makeText(getContext(), text2 == null? text : text2, Toast.LENGTH_SHORT).show();;

        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(text2)){
            new ScanResultDialog(requireContext(), false, "NFC").show();
        }else {
            if (!TextUtils.isEmpty(text2)){
                sendTagging(text2);
            }
            //new ScanResultDialog(requireContext(), true, "NFC").show();
        }


    }

    private void sendTagging(String text){
        //apiService.userLoginExists();
        apiService.check_in(text, String.valueOf(Utils.User_Idx)).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()){
                    int result = response.body();
                    if (result == 1){
                        showToast("체크인에 성공하였습니다.");
                    }else if (result == 2){
                        showToast("이미 체크인하였습니다.");
                    }else if (result == 0){
                        showToast("체크인에 실패하였습니다.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void showToast(String msg){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nfc, container, false);
        nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        if (nfcAdapter == null) Toast.makeText(getContext(), "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();



        binding.imageViewNfcBg.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + 200, 60);
            }
        });
        binding.imageViewNfcBg.setClipToOutline(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("NFCFragment","onActivityResult");

    }

    @Override
    public void onReadTag(NdefMessage[] action) {
        Log.d("have to come this method", "!!!!");
        buildtagViews(action);
    }
}