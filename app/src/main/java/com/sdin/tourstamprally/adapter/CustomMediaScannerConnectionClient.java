package com.sdin.tourstamprally.adapter;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class CustomMediaScannerConnectionClient
        implements MediaScannerConnection.MediaScannerConnectionClient{

    private String mFilename;
    private String mMimeType;
    private MediaScannerConnection mediaScanner;

    public CustomMediaScannerConnectionClient(Context context, File file, String minetype) {
        mFilename = file.getAbsolutePath();
        mediaScanner = new MediaScannerConnection(context, this);
        mediaScanner.connect();
        this.mMimeType = minetype;
    }

    @Override
    public void onMediaScannerConnected() {
        mediaScanner.scanFile(mFilename, mMimeType);
        Log.wtf("MediaAdapter", "미디어 스캔 성공");
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        Log.wtf("MediaAdapter", "미디어 스캔 연결 종료처리 uri = " + uri);
        mediaScanner.disconnect();

    }
}
