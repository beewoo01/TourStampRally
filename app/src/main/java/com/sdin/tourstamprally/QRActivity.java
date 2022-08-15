package com.sdin.tourstamprally;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            Toast.makeText(this, re, Toast.LENGTH_LONG).show();
        }else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}