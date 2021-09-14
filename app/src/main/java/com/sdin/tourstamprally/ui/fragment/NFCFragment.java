package com.sdin.tourstamprally.ui.fragment;

import android.content.Intent;
import android.graphics.Outline;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentNfcBinding;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.ScanResultDialog;
import com.sdin.tourstamprally.utill.GpsTracker;
import com.sdin.tourstamprally.utill.NFCListener;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NFCFragment extends BaseFragment implements NFCListener {

    private static final String DataArray = "param1";
    private static final String Listener = "param2";

    private TouristSpotPoint touristSpotPoint ;

    private GpsTracker gpsTracker;

    public NFCFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            NdefMessage[] dataArray = (NdefMessage[]) getArguments().getParcelableArray(DataArray);
            buildtagViews(dataArray);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }else {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.setOnListener(this);
            }
        }
    }


    private void buildtagViews(NdefMessage[] msgs) {

        if (msgs == null || msgs.length == 0) return;

        String text = "";
        String text2 = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;

        try {
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            String[] strs = text.split("\\|");
            text2 = strs[1];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Log.wtf("NFC 정보 text!", text);
        //Log.wtf("NFC 정보 text22!", text2);

        //Toast.makeText(getContext(), text2 == null? text : text2, Toast.LENGTH_SHORT).show();

        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(text2)){
            new ScanResultDialog(requireContext(), false, "NFC").show();
        }else {
            if (!TextUtils.isEmpty(text2)){
                isAvailable(text2);
                //sendTagging(text2);
            }
            //new ScanResultDialog(requireContext(), true, "NFC").show();
        }


    }

    private void isAvailable(final String text){

        apiService.getDistance(text).enqueue(new Callback<TouristSpotPoint>() {
            @Override
            public void onResponse(@NotNull Call<TouristSpotPoint> call, @NotNull Response<TouristSpotPoint> response) {
                if (response.isSuccessful()){
                    //Log.wtf("isAvailable", "isSuccessful");

                    touristSpotPoint = response.body();
                    //Log.wtf("isAvailable11111", touristSpotPoint.toString());
                    if (touristSpotPoint == null) {
                        showDialog();
                    } else {
                        gpsTracker = new GpsTracker(requireContext());
                        sendTagging(text);
                        //distance_calculation(text, touristSpotPoint.getTouristspotpoint_latitude(), touristSpotPoint.getTouristspotpoint_longitude());

                    }

                } else {
                    showDialog();
                }
            }

            @Override
            public void onFailure(@NotNull Call<TouristSpotPoint> call, @NotNull Throwable t) {
                showDialog();

                t.printStackTrace();
            }
        });
    }

    private void distance_calculation(String tagIngo, double latitude, double longitude){
        // 거리계산 30M 내
        //35.174201, 128.983804
        //35.256003, 129.013801
        //35.174208, 128.983784
        double dice = distance(latitude, longitude);
        if (dice <= 30){
            Log.wtf("distance_calculation", "30M이내");
            sendTagging(tagIngo);
        }else {

            Log.wtf("distance_calculation", "30M넘음");
            showDialog();
        }
        //Log.wtf("distance", String.valueOf(dice));
    }

    private double distance(double latitude, double longitude) {

        double userLatitude = gpsTracker.getLatitude();
        double userLongitude = gpsTracker.getLongitude();
        //Log.wtf("userLatitude", String.valueOf(userLatitude));
        //Log.wtf("userLongitude", String.valueOf(userLongitude));

        return Utils.distance(latitude, longitude, userLatitude, userLongitude);
    }


    private void showDialog(boolean isSuccess, int touristhistory_touristspotpoint_idx){
        new ScanResultDialog(requireContext(), isSuccess, "NFC", touristhistory_touristspotpoint_idx).show();
    }

    private void showDialog(){
        new ScanResultDialog(requireContext(), false, "NFC").show();
    }


    private void sendTagging(String text){

        apiService.check_in(text, String.valueOf(Utils.User_Idx)).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                if (response.isSuccessful()){
                    int result = response.body();
                    //Log.wtf("result!!!!!!", String.valueOf(result));
                    showDialog(result != 0, result);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                showDialog();
                t.printStackTrace();
            }
        });

    }

    private void showToast(String msg){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        com.sdin.tourstamprally.databinding.FragmentNfcBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nfc, container, false);
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        if (nfcAdapter == null) Toast.makeText(getContext(), "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();



        binding.imageViewNfcBg.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + 200, 60);
            }
        });
        binding.imageViewNfcBg.setClipToOutline(true);

        return binding.getRoot();
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("NFCFragment","onActivityResult");

    }*/

    @Override
    public void onReadTag(NdefMessage[] action) {
        buildtagViews(action);
    }
}