package com.sdin.tourstamprally;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdin.tourstamprally.api.APIService;
import com.sdin.tourstamprally.data.Constant;
import com.sdin.tourstamprally.data.LenientGsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

/**
 * ClassName            RetrofitGenerator
 * Created by JSky on   2020-06-03
 *
 * Description          서버 통신 설정
 */

public class RetrofitGenerator {

    private static final RetrofitGenerator instance = new RetrofitGenerator();
    public static RetrofitGenerator getInstance() {
        return instance;
    }
    public RetrofitGenerator(){}

    private static OkHttpClient createOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        /*로그*/
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        /*타임아웃 시간*/
        builder.connectTimeout(5, TimeUnit.MINUTES);
        builder.readTimeout(5, TimeUnit.MINUTES);
        builder.writeTimeout(5, TimeUnit.MINUTES);
        return builder.build();
    }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();



    // FirstCare
    Retrofit retrofitFC = new Retrofit.Builder()
            .baseUrl(Constant.SERVER_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(LenientGsonConverterFactory.create(gson))
            /*.addConverterFactory(new NullOnEmptyConverterFactory())*/
            .client(createOkHttpClient())
            .build();

    APIService apiService = retrofitFC.create(APIService.class);

    public APIService getApiService() {
        return apiService;
    }



}
