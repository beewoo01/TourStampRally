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

import androidx.fragment.app.Fragment;

public class GetStamp4Fragment extends Fragment {

    private TextView tvtitle;

    public static GetStamp4Fragment newInstance(int num) {
        GetStamp4Fragment fragment = new GetStamp4Fragment();
        Bundle args = new Bundle();
        args.putInt("stampnum", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int number = getArguments().getInt("stampnum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_stamp4, container, false);
        tvtitle = v.findViewById(R.id.textView);
        SpannableStringBuilder ssb = new SpannableStringBuilder(tvtitle.getText());
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9700")),0,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvtitle.setText(ssb);
        return v;
    }
}