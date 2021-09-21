package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentQrScanBinding;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.dialog.ScanResultDialog;
import com.sdin.tourstamprally.utill.GpsTracker;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRscanFragment extends BaseFragment {

    private FragmentQrScanBinding binding;

    private CodeScanner codeScanner;
    private TouristSpotPoint touristSpotPoint ;
    private GpsTracker gpsTracker;

    public QRscanFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //capture.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        //capture.onPause();
        codeScanner.releaseResources();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //capture.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_scan, container, false);
        codeScanner = new CodeScanner(getActivity(), binding.scannerView);
        codeScanner.setDecodeCallback(result -> getActivity().runOnUiThread(()
                -> {
            //showDialog(result == null? false : true);
            //Log.wtf("QR_RESULT", result.getText());
            isAvailable(result.getText());
            //Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
        }));

        binding.scannerView.setOnClickListener( v -> codeScanner.startPreview());

        return binding.getRoot();
    }


    private void isAvailable(final String text){

        //final String finalText = "T05100AA028";
        //테스트용

        apiService.getDistance(text).enqueue(new Callback<TouristSpotPoint>() {
            @Override
            public void onResponse(@NotNull Call<TouristSpotPoint> call, @NotNull Response<TouristSpotPoint> response) {
                if (response.isSuccessful()){
                    Log.wtf("거리 가져오기", "거리 가져오기");
                    //Log.wtf("isAvailable", "isSuccessful");

                    touristSpotPoint = response.body();
                   // Log.wtf("isAvailable11111", touristSpotPoint.toString());
                    if (touristSpotPoint == null) {
                        showDialog("QR 확인 하신 후 \n재시도 해주세요.");
                    } else {
                        gpsTracker = new GpsTracker(requireContext());
                        //sendTagging(text);
                        if (distance_calculation(touristSpotPoint.getTouristspotpoint_latitude(), touristSpotPoint.getTouristspotpoint_longitude())){
                            sendTagging(text);
                        }else {
                            showDialog("QR 확인 하신 후 \n재시도 해주세요.");
                        }

                    }

                } else {
                    showDialog("QR 확인 하신 후 \n재시도 해주세요.");
                    Log.wtf("else", "not isSuccessful");
                }
            }

            @Override
            public void onFailure(@NotNull Call<TouristSpotPoint> call, @NotNull Throwable t) {
                showDialog("QR 확인 하신 후 \n재시도 해주세요.");

                t.printStackTrace();
            }
        });
    }

    private boolean distance_calculation( double latitude, double longitude){
        // 거리계산 30M 내
        //35.174201, 128.983804
        //35.256003, 129.013801
        //35.174208, 128.983784
        double dice = distance(latitude, longitude);
        if (dice <= 30){
            Log.wtf("distance_calculation", "30M이내");

            return true;
        }else {

            Log.wtf("distance_calculation", "30M넘음");

            return false;
        }
        //Log.wtf("distance", String.valueOf(dice));
    }

    private double distance(double latitude, double longitude) {

        double userLatitude = gpsTracker.getLatitude();
        double userLongitude = gpsTracker.getLongitude();

        return Utils.distance(latitude, longitude, userLatitude, userLongitude);
    }

    private void sendTagging(String text){

        apiService.check_in(text, String.valueOf(Utils.User_Idx)).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                Log.wtf("체크인 ", "체크인 ");
                if (response.isSuccessful()){
                    int result = response.body();
                   // Log.wtf("result!!!!!!", String.valueOf(result));
                    Log.wtf("result!!!!!!! ", String.valueOf(result));
                    if (result == 0){
                        showDialog("이미 완료한 장소입니다.");
                    }else {
                        showDialog(result);
                    }
                    //showDialog(result == 0? false : true, result);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                showDialog("QR 확인 하신 후 \n재시도 해주세요.");
                t.printStackTrace();
            }
        });

    }

    private void showDialog(int touristhistory_touristspotpoint_idx){
        new ScanResultDialog(requireContext(), true, "QR", touristhistory_touristspotpoint_idx, "스탬프 랠리 획득!").show();
    }

    private void showDialog(String msg){
        new ScanResultDialog(requireContext(), false, "QR", msg).show();
    }


}

