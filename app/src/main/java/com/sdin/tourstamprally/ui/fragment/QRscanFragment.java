package com.sdin.tourstamprally.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.lang.reflect.Field;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRscanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRscanFragment extends Fragment implements DecoratedBarcodeView.TorchListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String toast;

    private FragmentQrScanBinding binding;

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private BackPressCloseHandler backPressCloseHandler;
    private ImageButton setting_btn,switchFlashlightButton;
    private Boolean switchFlashlightButtonCheck;

    public QRscanFragment() {

    }

    public static QRscanFragment newInstance(String param1, String param2) {
        QRscanFragment fragment = new QRscanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFlashlightButtonCheck = true;

        backPressCloseHandler = new BackPressCloseHandler(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void switchFlashlight(View view) {
        if (switchFlashlightButtonCheck) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_scan, container, false);
        binding.zxingBarcodeScanner.setTorchListener(this);
        IntentIntegrator.forSupportFragment(this).initiateScan();
        /*capture = new CaptureManager2(this, binding.zxingBarcodeScanner);

        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setBeepEnabled(false);
        capture.initializeFromIntent(integrator.createScanIntent(), savedInstanceState);
        capture.decode();




        capture.initializeFromIntent(getActivity().getIntent(), savedInstanceState);
        capture.decode();*/


        return binding.getRoot();
    }

    public void scanFromFragment() {

    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }


    @Override
    public void onTorchOn() {
        switchFlashlightButton.setImageResource(R.drawable.ic_flash_on_white_36dp);
        switchFlashlightButtonCheck = false;
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setImageResource(R.drawable.ic_flash_off_white_36dp);
        switchFlashlightButtonCheck = true;
    }
}

