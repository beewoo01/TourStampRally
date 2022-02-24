package com.sdin.tourstamprally.utill.listener;

import android.nfc.NdefMessage;

public interface NFCListener {
    void onReadTag(NdefMessage[] action);
}
