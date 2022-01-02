package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentQrScanBinding;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.ScanResultDialog;
import com.sdin.tourstamprally.utill.DialogListener;
import com.sdin.tourstamprally.utill.GpsTracker;
import com.sdin.tourstamprally.utill.ItemCliclListener;
import com.sdin.tourstamprally.utill.ItemOnClick;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRscanFragment extends BaseFragment implements DialogListener {

    private FragmentQrScanBinding binding;

    private CodeScanner codeScanner;
    private TouristSpotPoint touristSpotPoint;
    private GpsTracker gpsTracker;
    private ScanResultDialog scanResultDialog = null;
    //private ItemOnClick listner = null;


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
        Log.wtf("onPause", "onPause");
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
        //listner = (ItemOnClick) requireActivity();
        Log.wtf("onCreateView", "onCreateView");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
        Log.wtf("onViewCreated", "onViewCreated");

        codeScanner = new CodeScanner(requireActivity(), binding.scannerView);
        codeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(()
                -> {
            //showDialog(result == null? false : true);
            //Log.wtf("QR_RESULT", result.getText());
            isAvailable(result.getText());
            //Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
        }));

        //binding.moveNfcBtn.setOnClickListener( ignore -> listner.ItemGuid(1));
        binding.moveNfcBtn.setOnClickListener(ignore ->
                Navigation
                        .findNavController(
                                requireActivity(), R.id.nav_host)
                        .navigate(R.id.action_fragment_qr_scan_to_fragment_nfc));


        //binding.scannerView.setOnClickListener( v -> codeScanner.startPreview());
    }


    private void isAvailable(final String text) {

        //final String finalText = "T05100AA028";
        //테스트용

        apiService.getDistance(text).enqueue(new Callback<TouristSpotPoint>() {
            @Override
            public void onResponse(@NotNull Call<TouristSpotPoint> call, @NotNull Response<TouristSpotPoint> response) {
                if (response.isSuccessful()) {
                    Log.wtf("거리 가져오기", "거리 가져오기");
                    //Log.wtf("isAvailable", "isSuccessful");

                    touristSpotPoint = response.body();
                    if (touristSpotPoint == null) {
                        showFailDialog();
                    } else {
                        gpsTracker = new GpsTracker(requireContext());
                        sendTagging(text);
                        /*if (distance_calculation(touristSpotPoint.getTouristspotpoint_latitude(), touristSpotPoint.getTouristspotpoint_longitude())){
                            sendTagging(text);
                        }else {
                            showDialog("QR 스캔 실패","QR 확인 하신 후 \n재시도 해주세요.");
                        }*/

                    }

                } else {
                    showFailDialog();
                    Log.wtf("else", "not isSuccessful");
                }
            }

            @Override
            public void onFailure(@NotNull Call<TouristSpotPoint> call, @NotNull Throwable t) {
                showFailDialog();

                t.printStackTrace();
            }
        });
    }

    private boolean distance_calculation(double latitude, double longitude) {
        // 거리계산 30M 내
        //35.174201, 128.983804
        //35.256003, 129.013801
        //35.174208, 128.983784
        double dice = distance(latitude, longitude);
        if (dice <= 30) {
            Log.wtf("distance_calculation", "30M이내");

            return true;
        } else {

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

    private void sendTagging(String text) {

        apiService.check_in(text, String.valueOf(Utils.User_Idx), Utils.UserPhone).enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(@NotNull Call<HashMap<String, Integer>> call, @NotNull Response<HashMap<String, Integer>> response) {
                Log.wtf("체크인 ", "체크인 ");
                if (response.isSuccessful()) {

                    HashMap<String, Integer> result = response.body();
                    Log.wtf("result!!!!!!", result.toString());
                    if (result != null && result.get("result") != null && result.get("data") != null) {

                        int isSuccess = result.get("result");
                        int sendData = result.get("data");

                        if (isSuccess == 1) {
                            //이미 방문
                            showAlreadyDialog(sendData);
                        } else if (isSuccess == 0) {
                            //성공
                            showSuccessDialog(sendData);
                        } else {
                            //실패
                            showFailDialog();
                        }


                    } else {
                        showFailDialog();
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<HashMap<String, Integer>> call, @NotNull Throwable t) {
                showFailDialog();
                t.printStackTrace();
            }
        });

    }

    private void showSuccessDialog(int touristhistory_touristspotpoint_idx) {
        scanResultDialog = new ScanResultDialog(requireContext(), 1, "QR 스캔 성공", touristhistory_touristspotpoint_idx, "스탬프 랠리 획득!");
        scanResultDialog.setDialogListener(this);
        scanResultDialog.show();

        /*ScanResultDialog scanResultDialog = new ScanResultDialog(requireContext(), true, "QR", touristhistory_touristspotpoint_idx, "스탬프 랠리 획득!");
        scanResultDialog.show();
        scanResultDialog.*/
    }

    private void showFailDialog() {
        scanResultDialog = new ScanResultDialog(requireContext(), 0, "QR 스캔 실패", "QR 확인 하신 후 \n재시도 해주세요.");
        scanResultDialog.setDialogListener(this);
        scanResultDialog.show();
    }

    private void showAlreadyDialog(int touristspotpoint_idx) {
        scanResultDialog = new ScanResultDialog(requireContext(), 2, "스탬프 확인", " 이미 획득 완료하신\n 스탬프 입니다.", touristspotpoint_idx);
        scanResultDialog.setDialogListener(this);
        scanResultDialog.show();
    }


    @Override
    public void onDissMiss() {
        codeScanner.startPreview();
    }
}

