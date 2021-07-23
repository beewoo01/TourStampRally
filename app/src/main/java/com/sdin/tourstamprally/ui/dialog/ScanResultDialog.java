package com.sdin.tourstamprally.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.sdin.tourstamprally.R;

public class ScanResultDialog extends Dialog {

    public ScanResultDialog(@NonNull Context context, String result) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result_dialog);


    }
}
