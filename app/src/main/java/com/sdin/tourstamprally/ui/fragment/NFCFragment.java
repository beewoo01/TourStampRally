package com.sdin.tourstamprally.ui.fragment;

import android.graphics.Outline;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentNfcBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NFCFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NFCFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentNfcBinding binding;

    public NFCFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NFCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NFCFragment newInstance(String param1, String param2) {
        NFCFragment fragment = new NFCFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nfc, container, false);
        binding.imageViewNfcBg.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + 200, 60);
            }
        });
        binding.imageViewNfcBg.setClipToOutline(true);
        return binding.getRoot();
    }
}