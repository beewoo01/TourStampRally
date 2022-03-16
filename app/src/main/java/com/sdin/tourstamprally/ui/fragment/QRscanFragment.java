package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentQrScanBinding;
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog;
import com.sdin.tourstamprally.ui.dialog.DialogFailTimeOver;
import com.sdin.tourstamprally.ui.dialog.ScanResultDialog;
import com.sdin.tourstamprally.ui.dialog.ScanResultPopup;
import com.sdin.tourstamprally.ui.dialog.course.SelectCourseDialog;
import com.sdin.tourstamprally.utill.listener.DialogListener;
import com.sdin.tourstamprally.utill.GpsTracker;
import com.sdin.tourstamprally.utill.listener.ScanListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRscanFragment extends BaseFragment implements DialogListener, ScanListener {

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);

        codeScanner = new CodeScanner(requireActivity(), binding.scannerView);
        codeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(()
                ->  {
            Log.wtf("taginfo", result.getText());
            checkIn(result.getText());
        }));

        binding.moveNfcBtn.setOnClickListener(ignore -> {

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host);
            navController.navigate(R.id.action_fragment_qr_scan_to_fragment_nfc);
            //navController.popBackStack(R.id.page_stamp, true);

        });

        getData();
    }

    private void getData() {
        Log.wtf("NFCFragment", "getData");
        apiService.haveCourseForStemp(Utils.User_Idx)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                        Log.wtf("NFCFragment", "haveCourseForStemp, onSuccess");
                        initGuid(integer);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void initGuid(int position) {
        Log.wtf("NFCFragment", "initGuid");
        switch (position) {
            case 0 : {
                //코스 선택 안함
                Log.wtf("NFCFragment", "코스 선택 안함");
                getCourseInfo();
                break;
            }

            case 1 : {
                Log.wtf("NFCFragment", "코스 있음");
                //코스 있음
                break;
            }

            case 2 : {
                //코스 시간 지남 POPUP
                Log.wtf("NFCFragment", "코스 시간 지남");
                new DialogFailTimeOver(requireContext()).show();
                break;
            }
        }

    }

    private void getCourseInfo() {
        new SelectCourseDialog(requireContext()).show();
    }


    private void checkIn(final String taggingStr) {

        //final String finalText = "T05100AA028";
        //테스트용

        apiService.checkInStemp(Utils.User_Idx, taggingStr, Utils.UserPhone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HashMap<String, Integer>>() {

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull HashMap<String, Integer> map) {
                        setCheckInResult(map);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void setCheckInResult(HashMap<String, Integer> map) {
        int result = map.get("result");
        int touristspotIdx = map.get("touristspot_idx");
        switch (result) {
            case 0: {
                // 인증성공 POPUP
                new ScanResultPopup(requireContext(), 0, "NFC", () -> {
                    //check_spot_point_nfc
                    return null;
                }).show();
                break;
            }

            case 1: {
                //코스 선택 안함 POPUP
                getCourseInfo();
                /*apiService.selectNotClearCourse(Utils.User_Idx)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<SelectCourseModel>>() {

                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<SelectCourseModel> selectCourseModels) {

                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }
                        });*/
                break;
            }

            case 2: {
                // 시간 오버 POPUP
                new DialogFailTimeOver(requireContext()).show();
                break;
            }

            case 3: {
                // 태그 정보 잘못됨 POPUP
                new ScanResultPopup(requireContext(), 1, "NFC", () -> null).show();
                break;
            }

            case 4: {
                //이미 인증됨  POPUP
                new ScanResultPopup(requireContext(), 2, "NFC", () -> {
                    Log.wtf("ScanResultPopup", "touristspotIdx " + touristspotIdx);
                    return null;
                }).show();
                break;
            }

            case 5: {
                // 현재 코스가 아님 POPUP
                new DefaultBSRDialog(
                        requireContext(),
                        "현재 진행중인 코스를\n중단 하시겠습니까?",
                        "중간 시 해당 코스는 모두\n실패 처리가 됩니다.",
                        true,
                        false,
                        "취소",
                        "중단",
                        isCancel -> {
                            if (!isCancel) {
                                // TODO: 2022/03/13 내 코스 모든 기록 삭제
                            }
                            return null;
                        }
                ).show();
                break;
            }

            default: {
                showToast("인증에 실패하였습니다. 관리자에게 문의해주세요 " + result);
                break;
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
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

    /*private void sendTagging(String text) {

        apiService.check_in(text, String.valueOf(Utils.User_Idx), Utils.UserPhone).enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(@NotNull Call<HashMap<String, Integer>> call, @NotNull Response<HashMap<String, Integer>> response) {

                if (response.isSuccessful()) {

                    HashMap<String, Integer> result = response.body();
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
        scanResultDialog =
                new ScanResultDialog(
                        requireContext(),
                        QRscanFragment.this,
                        1,
                        "QR 스캔 성공",
                        touristhistory_touristspotpoint_idx,
                        "스탬프 랠리 획득!");

        scanResultDialog.setDialogListener(this);
        scanResultDialog.show();

    }

    private void showFailDialog() {
        scanResultDialog =
                new ScanResultDialog(
                        requireContext(),
                        0,
                        "QR 스캔 실패",
                        "QR 확인 하신 후 \n재시도 해주세요."
                );

        scanResultDialog.setDialogListener(this);
        scanResultDialog.show();
    }

    private void showAlreadyDialog(int touristspotpoint_idx) {
        scanResultDialog = new ScanResultDialog(
                requireContext(),
                QRscanFragment.this,
                2,
                "스탬프 확인",
                " 이미 획득 완료하신\n 스탬프 입니다.",
                touristspotpoint_idx);
        scanResultDialog.setDialogListener(this);
        scanResultDialog.show();
    }*/


    @Override
    public void onDissMiss() {
        codeScanner.startPreview();
    }

    @Override
    public void moveSpotPoint(@NonNull RallyMapDTO model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putString("title", model.getTouristspot_name());
        bundle.putInt("state", 1);

        Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        Navigation.findNavController(requireActivity(), R.id.nav_host)
                .navigate(R.id.check_spot_point_qr, bundle);
    }
}

