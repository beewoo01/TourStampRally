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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentNfcBinding;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.utill.NFCListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class NFCFragment extends Fragment implements NFCListener {

    private static final String DataArray = "param1";
    private static final String Listener = "param2";
    private NfcAdapter nfcAdapter;

    // TODO: Rename and change types of parameters
    private NdefMessage[] dataArray;
    private FragmentNfcBinding binding;

    public NFCFragment() {

    }

    public static NFCFragment newInstance(NdefMessage[] dataArray, NFCListener listener) {
        NFCFragment fragment = new NFCFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(DataArray, dataArray);
        fragment.setArguments(args);
        return fragment;
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


    private void buildtagViews(NdefMessage[] msgs){
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


        Toast.makeText(getContext(), text2 == null? text : text2, Toast.LENGTH_SHORT).show();

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

    }

    @Override
    public void onReadTag(NdefMessage[] action) {
        Log.d("have to come this method", "!!!!");
        buildtagViews(action);
    }
}