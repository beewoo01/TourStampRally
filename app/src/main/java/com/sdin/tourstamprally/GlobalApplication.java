package com.sdin.tourstamprally;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

import java.io.IOException;
import java.net.SocketException;

import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, getString(R.string.kakaoNativeAppKey));

        RxJavaPlugins.setErrorHandler(e ->{
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException) || (e instanceof SocketException)) {
                return;
            }

            if (e instanceof InterruptedException) {
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {

                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }

            if (e instanceof IllegalStateException) {

                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);

                return;
            }
            //Log.e("RxJava_HOOK", "Undeliverable exception received, not sure what to do" + e.getMessage());


        });
    }
}
