package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentSetAlarmBinding;
import com.sdin.tourstamprally.model.AlramState;
import com.sdin.tourstamprally.sqlite.SQLiteConnector;

import org.jetbrains.annotations.NotNull;


public class SetAlarmFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private boolean emailstat = false;
    private boolean pushstat = false;
    private boolean smsstat = false;

    private FragmentSetAlarmBinding binding;

    private SQLiteConnector sqlConn;


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

        /*DbOpenHelper helper = new DbOpenHelper(requireActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.onCreate(db);*/

        sqlConn = new SQLiteConnector(requireActivity());
        AlramState alramState = sqlConn.selectExits(Utils.UserPhone);


        getStatus(alramState);

        binding.switchEmail.setOnCheckedChangeListener((buttonView, isChecked) ->{
            emailstat = isChecked;
            update();
        });

        binding.switchPush.setOnCheckedChangeListener((buttonView, isChecked) ->{
            pushstat = isChecked;
            update();
        });

        binding.switchSms.setOnCheckedChangeListener((buttonView, isChecked) ->{
            smsstat = isChecked;
            update();
        });
    }

    private void update(){
        sqlConn.update(new AlramState(
                Utils.UserPhone,
                convertBooleanToInt(emailstat),
                convertBooleanToInt(pushstat),
                convertBooleanToInt(smsstat)
        ));
    }

    private int convertBooleanToInt(boolean state){
        return state? 1 : 0;
    }

    private boolean convertInttoBoolean(int state){
        return state != 0;
    }

    private void getStatus(AlramState alramState){

        binding.switchEmail.setChecked(convertInttoBoolean(alramState.getEmail_Alram()));
        binding.switchPush.setChecked(convertInttoBoolean(alramState.getPush_Alram()));
        binding.switchSms.setChecked(convertInttoBoolean(alramState.getSms_Alram()));

    }

}