package com.sdin.tourstamprally;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, getString(R.string.kakaoNativeAppKey));
    }
}
