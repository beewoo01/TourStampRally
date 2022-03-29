package com.sdin.tourstamprally.ui.fragment;

import android.graphics.Outline;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

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
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.model.course.SelectCourseModel;
import com.sdin.tourstamprally.ui.activity.MainActivity2;
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog;
import com.sdin.tourstamprally.ui.dialog.DialogFailTimeOver;
import com.sdin.tourstamprally.ui.dialog.ScanResultDialog;
import com.sdin.tourstamprally.ui.dialog.ScanResultPopup;
import com.sdin.tourstamprally.ui.dialog.course.SelectCourseDialog;
import com.sdin.tourstamprally.utill.GpsTracker;
import com.sdin.tourstamprally.utill.listener.NFCListener;
import com.sdin.tourstamprally.utill.listener.ScanListener;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NFCFragment extends BaseFragment implements NFCListener, ScanListener {

    private static final String DataArray = "param1";
    private static final String Listener = "param2";

    private TouristSpotPoint touristSpotPoint;

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
        } else {
            MainActivity2 mainActivity = (MainActivity2) getActivity();
            /*if (mainActivity != null) {
                mainActivity.setOnListener(this);
            }*/
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

        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(text2)) {
            new ScanResultDialog(requireContext(), 0, "NFC 태깅 실패", " 확인 하신 후 \n재시도 해주세요.").show();
        } else {
            if (!TextUtils.isEmpty(text2)) {
                checkIn(text2);
                //sendTagging(text2);
            }
            //new ScanResultDialog(requireContext(), true, "NFC").show();
        }


    }

    private void checkIn(final String taggingStr) {

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

        /*apiService.getDistance(taggingStr).enqueue(new Callback<TouristSpotPoint>() {
            @Override
            public void onResponse(@NotNull Call<TouristSpotPoint> call, @NotNull Response<TouristSpotPoint> response) {
                if (response.isSuccessful()) {
                    //Log.wtf("isAvailable", "isSuccessful");

                    touristSpotPoint = response.body();
                    //Log.wtf("isAvailable11111", touristSpotPoint.toString());
                    if (touristSpotPoint == null) {
                        showFailDialog();
                    } else {
                        gpsTracker = new GpsTracker(requireContext());
                        sendTagging(taggingStr);
                        //distance_calculation(text, touristSpotPoint.getTouristspotpoint_latitude(), touristSpotPoint.getTouristspotpoint_longitude());

                    }

                } else {
                    showFailDialog();
                }
            }

            @Override
            public void onFailure(@NotNull Call<TouristSpotPoint> call, @NotNull Throwable t) {
                showFailDialog();

                t.printStackTrace();
            }
        });*/
    }

    private void setCheckInResult(HashMap<String, Integer> map) {
        int result = map.get("result");
        int touristspotIdx = map.get("touristspot_idx");
        switch (result) {
            case 0: {
                // 인증성공 POPUP
                new ScanResultPopup(requireContext(), 0, "NFC", null ,() -> {

                    return null;
                }).show();
                break;
            }

            case 1: {
                //코스 선택 안함 POPUP
                getCourseInfo();
                break;
            }

            case 2: {
                // 시간 오버 POPUP
                new DialogFailTimeOver(requireContext()).show();
                break;
            }

            case 3: {
                // 태그 정보 잘못됨 POPUP
                new ScanResultPopup(requireContext(), 1, "NFC", null ,() -> null).show();
                break;
            }

            case 4: {
                //이미 인증됨  POPUP
                new ScanResultPopup(requireContext(), 2, "NFC", null ,() -> {
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

    private void distance_calculation(String tagIngo, double latitude, double longitude) {
        // 거리계산 30M 내
        //35.174201, 128.983804
        //35.256003, 129.013801
        //35.174208, 128.983784
        double dice = distance(latitude, longitude);
        if (dice <= 30) {
            Log.wtf("distance_calculation", "30M이내");
            checkIn(tagIngo);
        } else {

            Log.wtf("distance_calculation", "30M넘음");
            new ScanResultPopup(requireContext(), 1, "NFC", null ,() -> null).show();
            //showFailDialog();
        }
    }

    private double distance(double latitude, double longitude) {

        double userLatitude = gpsTracker.getLatitude();
        double userLongitude = gpsTracker.getLongitude();

        return Utils.distance(latitude, longitude, userLatitude, userLongitude);
    }


    /*private void showSuccessDialog(int touristhistory_touristspotpoint_idx) {
        new ScanResultDialog(
                requireContext(),
                NFCFragment.this,
                1,
                "NFC 태깅 성공",
                touristhistory_touristspotpoint_idx,
                "스탬프 랠리 획득!"
        ).show();
    }*/

    /*private void showFailDialog() {
        new ScanResultDialog(requireContext(), 0, "NFC 태깅 실패", "NFC 확인 하신 후 \n재시도 해주세요.").show();
    }*/

    /*private void showAlreadyDialog(int touristspotpoint_idx) {
        new ScanResultDialog(
                requireContext(),
                NFCFragment.this,
                2,
                "스탬프 확인",
                " 이미 획득 완료하신\n 스탬프 입니다.",
                touristspotpoint_idx
        ).show();
    }*/


    /*private void sendTagging(String text) {

        apiService.check_in(text, String.valueOf(Utils.User_Idx), Utils.UserPhone).enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(@NotNull Call<HashMap<String, Integer>> call, @NotNull Response<HashMap<String, Integer>> response) {
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
                        //실패
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

    }*/

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.wtf("NFCFragment", "onCreateView");
        FragmentNfcBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nfc, container, false);
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        if (nfcAdapter == null)
            Toast.makeText(getContext(), "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();


        binding.imageViewNfcBg.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + 200, 60);
            }
        });
        binding.imageViewNfcBg.setClipToOutline(true);
        getData();

        return binding.getRoot();
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
                getCourseInfo();
                break;
            }

            case 1 : {
                //코스 있음
                break;
            }

            case 2 : {
                //코스 시간 지남 POPUP
                new DialogFailTimeOver(requireContext()).show();
                break;
            }
        }

    }

    private void getCourseInfo() {
        new SelectCourseDialog(requireContext(), null).show();
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

    @Override
    public void moveSpotPoint(@NonNull RallyMapDTO model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putString("title", model.getTouristspot_name());
        bundle.putInt("state", 1);

        Navigation.findNavController(requireActivity(), R.id.nav_host)
                .navigate(R.id.check_spot_point_nfc, bundle);
    }
}