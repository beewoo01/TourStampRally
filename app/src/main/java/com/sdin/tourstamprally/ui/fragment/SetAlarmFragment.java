package com.sdin.tourstamprally.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentSetAlarmBinding;

import org.jetbrains.annotations.NotNull;


public class SetAlarmFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FragmentSetAlarmBinding binding;

    public SetAlarmFragment() {
        // Required empty public constructor
    }

    public static SetAlarmFragment newInstance(String param1, String param2) {
        SetAlarmFragment fragment = new SetAlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_alarm, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getStatus();

        binding.switchEmail.setOnCheckedChangeListener((buttonView, isChecked) ->
                setShearedString("alEmail", isChecked));

        binding.switchPush.setOnCheckedChangeListener((buttonView, isChecked) ->
                setShearedString("alPush", isChecked));

        binding.switchSms.setOnCheckedChangeListener((buttonView, isChecked) ->
                setShearedString("alSms", isChecked));
    }

    private void getStatus(){
        SharedPreferences preferences = setSharedPref();
        final boolean alEmail = preferences.getBoolean("alEmail", false);
        final boolean alPush = preferences.getBoolean("alPush", false);
        final boolean alSms = preferences.getBoolean("alSms", false);
        binding.switchEmail.setChecked(alEmail);
        binding.switchPush.setChecked(alPush);
        binding.switchSms.setChecked(alSms);

    }

    private SharedPreferences setSharedPref(){
        return requireActivity().getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE);
    }

    private void setShearedString(String key, boolean isCheck){
        SharedPreferences preferences = setSharedPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, isCheck);
        editor.apply();

    }
}