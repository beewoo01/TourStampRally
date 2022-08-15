package com.sdin.tourstamprally;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GetStamp1Fragment extends Fragment {

    private TextView tvtitle;

    public static GetStamp1Fragment newInstance(int num) {
        GetStamp1Fragment fragment = new GetStamp1Fragment();
        Bundle args = new Bundle();
        args.putInt("stampnum", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            int number = getArguments().getInt("stampnum");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_get_stamp1, container, false);
        tvtitle = v.findViewById(R.id.textView);
        String str = "스탬프획득 버튼 클릭";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9700")),0,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvtitle.setText(ssb);
        return v;
    }
}