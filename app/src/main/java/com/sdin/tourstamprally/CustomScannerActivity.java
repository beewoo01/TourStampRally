package com.sdin.tourstamprally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.lang.reflect.Field;

public class CustomScannerActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private BackPressCloseHandler backPressCloseHandler;
    private ImageButton setting_btn,switchFlashlightButton;
    private Boolean switchFlashlightButtonCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);


        switchFlashlightButtonCheck = true;

        backPressCloseHandler = new BackPressCloseHandler(this);

        /*setting_btn = (ImageButton)findViewById(R.id.setting_btn);
        switchFlashlightButton = (ImageButton)findViewById(R.id.switch_flashlight);

        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }*/


        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);
        disableLaser(barcodeScannerView);
        //barcodeScannerView.getViewFinder().setVisibility(View.INVISIBLE);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

    }

    private void disableLaser(DecoratedBarcodeView barcodeScannerView){
        ViewfinderView viewFinder = barcodeScannerView.getViewFinder();
        Field scannerAlphaField = null;

        try {
            scannerAlphaField = viewFinder.getClass().getDeclaredField("SCANNER_ALPHA");
            scannerAlphaField.setAccessible(true);
            scannerAlphaField.set(viewFinder, new int[1]);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*private Canvas canvas(){


        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect frame = framingRect;
        borderPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        //inside onDraw
        int distance = (frame.bottom - frame.top) / 4;
        int thickness = 15;

        //top left corner
        canvas.drawRect(frame.left - thickness, frame.top - thickness, distance + frame.left, frame.top, borderPaint);
        canvas.drawRect(frame.left - thickness, frame.top, frame.left, distance + frame.top, borderPaint);

        //top right corner
        canvas.drawRect(frame.right - distance, frame.top - thickness, frame.right + thickness, frame.top, borderPaint);
        canvas.drawRect(frame.right, frame.top, frame.right + thickness, distance + frame.top, borderPaint);

        //bottom left corner
        canvas.drawRect(frame.left - thickness, frame.bottom, distance + frame.left, frame.bottom + thickness, borderPaint);
        canvas.drawRect(frame.left - thickness, frame.bottom - distance, frame.left, frame.bottom, borderPaint);

        //bottom right corner
        canvas.drawRect(frame.right - distance, frame.bottom, frame.right + thickness, frame.bottom + thickness, borderPaint);
        canvas.drawRect(frame.right, frame.bottom - distance, frame.right + thickness, frame.bottom, borderPaint);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
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