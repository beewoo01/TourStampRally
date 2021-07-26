package com.sdin.tourstamprally.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.sdin.tourstamprally.BackPressCloseHandler;
import com.sdin.tourstamprally.CustomScannerActivity;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentQrScanBinding;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.ScanResultDialog;

import java.lang.reflect.Field;

public class QRscanFragment extends Fragment {


    private String toast;

    private FragmentQrScanBinding binding;

    private CodeScanner codeScanner;

    public QRscanFragment() {

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //capture.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        //capture.onPause();
        codeScanner.releaseResources();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //capture.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_scan, container, false);
        codeScanner = new CodeScanner(getActivity(), binding.scannerView);
        codeScanner.setDecodeCallback(result -> getActivity().runOnUiThread(()
                -> {
            showDialog(result == null? false : true);
            Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
        }));

        binding.scannerView.setOnClickListener( v -> codeScanner.startPreview());

        return binding.getRoot();
    }


    private void displayToast() {

        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    private void showDialog(boolean isSucess){
        Log.wtf("displayToast", String.valueOf(isSucess));
        new ScanResultDialog(requireContext(), isSucess, "QR").show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.wtf("QRSCAN", "onActivityResult");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();

            }
            displayToast();
            // At this point we may or may not have a reference to the activity

        }
    }


}

