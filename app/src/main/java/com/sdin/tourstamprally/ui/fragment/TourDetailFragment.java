package com.sdin.tourstamprally.ui.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.sdk.common.util.KakaoCustomTabsClient;
import com.kakao.sdk.navi.NaviClient;
import com.kakao.sdk.navi.model.CoordType;
import com.kakao.sdk.navi.model.Location;
import com.kakao.sdk.navi.model.NaviOption;
import com.kakao.sdk.template.model.FeedTemplate;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentTourDetailBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.dialog.DetailDialog;
import com.sdin.tourstamprally.ui.dialog.ReadyDialog;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TourDetailFragment extends BaseFragment/* implements MapView.MapViewEventListener*/ {

    private static final String ARG_PARAM1 = "model";
    private FragmentTourDetailBinding binding;
    private TouristSpotPoint touristSpotPoint;
    private MapView mapView;


    public TourDetailFragment() {
        // Required empty public constructor
    }

    public static TourDetailFragment newInstance(TouristSpotPoint touristSpotPoint) {
        TourDetailFragment fragment = new TourDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, touristSpotPoint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            touristSpotPoint = (TouristSpotPoint) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_detail, container, false);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
        setData();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onClick(View view) {
        if (view.getId() == binding.phoneTxv.getId()) {

            Pattern pattern1 = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
            Pattern pattern2 = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
            Pattern pattern3 = Pattern.compile("\\d{11}");
            Pattern pattern4 = Pattern.compile("\\d{10}");

            if (touristSpotPoint.getTouristspotpoint_contactinfo() != null &&
                    !TextUtils.isEmpty(binding.phoneTxv.getText().toString())) {
                String phone = touristSpotPoint.getTouristspotpoint_contactinfo();
                Log.wtf("phone", phone);
                boolean isVailable;
                if (pattern1.matcher(phone).matches()) {
                    isVailable = true;
                } else if (pattern2.matcher(phone).matches()) {
                    isVailable = true;
                } else if (pattern3.matcher(phone).matches()) {
                    isVailable = true;
                } else if (pattern4.matcher(phone).matches()) {
                    isVailable = true;
                } else {
                    isVailable = false;
                }

                if (isVailable) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        requireActivity().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "요청중 에러 발생.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(requireContext(), "해당 관광지는 등록된 전화번호가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(requireContext(), "해당 관광지는 등록된 전화번호가 없습니다.", Toast.LENGTH_SHORT).show();
            }


        } else if (view.getId() == binding.detailTxv.getId()) {
            String link = touristSpotPoint.getTouristspotpoint_link();
            Log.wtf("LINK", link);
            showDetailPopup();
            //showReadyDialog();


            /* 해당 기능은 이미 구현 완료되었으나 잠시 주석처리 해둔 상태 구현해야한다면 주석 풀면됨*/
            /*if (!TextUtils.isEmpty(link) && !link.equalsIgnoreCase("null")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    requireActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "요청중 에러 발생.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(requireContext(), "서비스 준비 중 입니다.", Toast.LENGTH_SHORT).show();
            }*/
        }

    }

    public void watchMovie() {
        try {
            //requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCNQDd7xWs6faDK4WW-zMcPg")));
            requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(touristSpotPoint.getTouristspotpoint_videolink())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void naviClick() {

        Log.wtf(TAG, "naviClick naviClick");
        if (NaviClient.getInstance().isKakaoNaviInstalled(requireContext())) {
            //Log.wtf(TAG, "카카오내비 앱으로 길안내 가능");
            requireActivity().startActivity(NaviClient.getInstance().navigateIntent(
                    new Location(touristSpotPoint.getTouristspotpoint_name(), String.valueOf(touristSpotPoint.getTouristspotpoint_longitude()), String.valueOf(touristSpotPoint.getTouristspotpoint_latitude())),
                    new NaviOption(CoordType.WGS84)
                    )
            );
        } else {
            //Log.wtf(TAG, "카카오내비 미설치 : 웹 길안내 사용 권장");
            Uri uri = NaviClient.getInstance().navigateWebUrl(
                    new Location(touristSpotPoint.getTouristspotpoint_name(), String.valueOf(touristSpotPoint.getTouristspotpoint_longitude()), String.valueOf(touristSpotPoint.getTouristspotpoint_latitude())),
                    /*new Location("하이루", "127.108640", "37.402111"),*/
                    new NaviOption(CoordType.WGS84)
            );

            KakaoCustomTabsClient.INSTANCE.openWithDefault(requireContext(), uri);
        }
    }

    public void kakaoLink() {
        //KakaoLink
        showReadyDialog();
        /*Toast.makeText(requireContext(), "서비스 준비 중 입니다.", Toast.LENGTH_SHORT).show();*/
    }

    private void showDetailPopup() {
        new DetailDialog(requireContext(), touristSpotPoint).show();
    }

    private void showReadyDialog() {
        ReadyDialog dialog = new ReadyDialog(requireContext());
        dialog.show();
    }

    /*fun kakaoLink() {
        val params = FeedTemplate
                .newBuilder(
                        ContentObject.newBuilder(
                                "디저트 사진",
                                "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                        .setMobileWebUrl("https://developers.kakao.com").build()
                        )
                                .setDescrption("아메리카노, 빵, 케익")
                                .build()
                )
                .setSocial(
                        SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                                .setSharedCount(30).setViewCount(40).build()
                )
                .addButton(
                        ButtonObject(
                                "웹에서 보기",
                                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl(
                                        "https://developers.kakao.com"
                                ).build()
                        )
                )
                .addButton(
                        ButtonObject(
                                "앱에서 보기", LinkObject.newBuilder()
                                        .setWebUrl("'https://developers.kakao.com")
                                        .setMobileWebUrl("https://developers.kakao.com")
                                        .setAndroidExecutionParams("key1=value1")
                                        .setIosExecutionParams("key1=value1")
                                        .build()
                        )
                )
                .build()

        val serverCallbackArgs: MutableMap<String, String> =
        HashMap()
        serverCallbackArgs["user_id"] = "\${current_user_id}"
        serverCallbackArgs["product_id"] = "\${shared_product_id}"

        KakaoLinkService.getInstance().sendDefault(
                this,
                params,
                serverCallbackArgs,
                object : ResponseCallback<KakaoLinkResponse?>() {
            override fun onFailure(errorResult: ErrorResult) {
                //Logger.e(errorResult.toString())
            }

            override fun onSuccess(result: KakaoLinkResponse?) { // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        })
    }*/

    private void setData() {


        binding.tourNameTxv.setText(touristSpotPoint.getTouristspotpoint_name());
        binding.tourContentTxv.setText(touristSpotPoint.getTouristspotpoint_explan());
        //getTouristspotpoint_detail_explan 값 팝업창에 전달 예정
        /*binding.tourContentTxv.setText(touristSpotPoint.getTouristspotpoint_detail_explan());*/

        //Log.wtf("imgimgimg", touristSpotPoint.getTouristspotpoint_img());
        Glide.with(requireContext())
                .load("http://coratest.kr/imagefile/bsr/" + touristSpotPoint.getTouristspotpoint_img())
                .error(ContextCompat.getDrawable(requireContext(), R.drawable.sample_bg))
                .into(binding.bgImv);

        mapView = new MapView(requireActivity());
        //mapView.setMapViewEventListener(this);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(touristSpotPoint.getTouristspotpoint_latitude(), touristSpotPoint.getTouristspotpoint_longitude()), 2, true);


        binding.mapviewLayout.addView(mapView);

        MapPOIItem customMarker = new MapPOIItem();
        customMarker.setItemName(touristSpotPoint.getTouristspotpoint_name());
        customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(touristSpotPoint.getTouristspotpoint_latitude(), touristSpotPoint.getTouristspotpoint_longitude()));
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.arrive_ic); // 마커 이미지.
        customMarker.setCustomImageAutoscale(true); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        binding.locationAddressTxv.setText(getAddress());

        mapView.addPOIItem(customMarker);
    }

    private void removeMapView() {
        binding.mapviewLayout.removeView(mapView);
    }

    @Override
    public void onStop() {
        super.onStop();
        removeMapView();
    }

    private String getAddress() {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

        try {

            List<Address> addresses = geocoder.getFromLocation(
                    touristSpotPoint.getTouristspotpoint_latitude(),
                    touristSpotPoint.getTouristspotpoint_longitude(),
                    7);

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(requireActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
                return touristSpotPoint.getTouristspotpoint_name();

            }

            Address address = addresses.get(0);
            return address.getAddressLine(0);
        } catch (IOException | IllegalArgumentException ioException) {
            //네트워크 문제
            //Log.wtf("addresses?", "지오코더 서비스 사용불가");
            return touristSpotPoint.getTouristspotpoint_name();
        } //Log.wtf("addresses", "잘못된 GPS 좌표");

    }
}