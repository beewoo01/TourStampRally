package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.navigation.fragment.NavHostFragment;

import com.sdin.tourstamprally.RetrofitGenerator;
import com.sdin.tourstamprally.api.APIService;

public class BaseFragment2 extends NavHostFragment {

    public RetrofitGenerator retrofitGenerator;
    public APIService apiService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofitGenerator = new RetrofitGenerator();
        apiService = retrofitGenerator.getApiService();
    }
}
