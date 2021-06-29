package com.sdin.tourstamprally.utill;

import android.nfc.NdefMessage;

public interface NFCListener {
    void onReadTag(NdefMessage[] action);
}
