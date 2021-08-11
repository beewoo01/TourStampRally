package com.sdin.tourstamprally.ui.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentTourDetailBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TourDetailFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "model";
    private FragmentTourDetailBinding binding;
    private Tour_Spot tour_spot;


    public TourDetailFragment() {
        // Required empty public constructor
    }

    public static TourDetailFragment newInstance(Tour_Spot tour_spot) {
        TourDetailFragment fragment = new TourDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, tour_spot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tour_spot = (Tour_Spot) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_detail, container, false);
        setData();
        return binding.getRoot();
    }

    private void setData(){


        binding.tourNameTxv.setText(tour_spot.getTouristspot_name());
        binding.tourContentTxv.setText(tour_spot.getTouristspot_explan());
        Glide.with(requireContext())
                .load("http://zzipbbong.cafe24.com/imagefile/bsr/" + tour_spot.getTouristspot_img())
                .error(ContextCompat.getDrawable(requireContext(), R.drawable.sample_bg))
                .into(binding.bgImv);

        binding.saveTxv.setOnClickListener( v -> {
            // TODO: 8/11/21 찜하기
            apiService.select_interest_status(Utils.User_Idx, tour_spot.getTouristspot_idx()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()){
                        //ShowToast("찜하기에 성공 하셨습니다.", requireContext());
                        Log.wtf("response.body()", String.valueOf(response.body()));
                        if (response.body() == 0) {
                            Log.wtf("onResponse", "찜하기 000000");
                            insert_intest();
                        }

                    } else {
                        Log.wtf("찜하기 실패", response.toString());
                        ShowToast("찜하기에 실패 하셨습니다.", requireContext());
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    ShowToast("찜하기에 실패 하셨습니다.", requireContext());
                    t.printStackTrace();
                }

            });


        });

        MapView mapView = new MapView(requireActivity());
        ViewGroup mapViewContainer = binding.mapView;

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(tour_spot.getTouristspot_latitude(), tour_spot.getTouristspot_longitude()), 2, true);


        mapViewContainer.addView(mapView);

        MapPOIItem customMarker = new MapPOIItem();
        customMarker.setItemName(tour_spot.getTouristspot_name());
        customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(tour_spot.getTouristspot_latitude(), tour_spot.getTouristspot_longitude()));
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.arrive_ic); // 마커 이미지.
        customMarker.setCustomImageAutoscale(true); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        binding.locationAddressTxv.setText(getAddress());

        mapView.addPOIItem(customMarker);
    }

    private void insert_intest(){
        apiService.insert_intest(Utils.User_Idx, tour_spot.getTouristspot_idx()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()){
                    ShowToast("찜하기에 성공 하셨습니다.", requireContext());
                }else {
                    Log.wtf("찜하기 실패", response.toString());
                    ShowToast("찜하기에 실패 하셨습니다.", requireContext());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String getAddress(){
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

        List<Address> addresses ;

        try {

            addresses = geocoder.getFromLocation(
                    tour_spot.getTouristspot_latitude(),
                    tour_spot.getTouristspot_longitude(),
                    7);

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(requireActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
                return tour_spot.getTouristspot_name();

            }

            Address address = addresses.get(0);
            Log.wtf("주소는?", address.getAddressLine(0).toString()+"\n");
            return address.getAddressLine(0);
        } catch (IOException ioException) {
            //네트워크 문제
            Log.wtf("addresses?", "지오코더 서비스 사용불가");
            return tour_spot.getTouristspot_name();
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.wtf("addresses", "잘못된 GPS 좌표");
            return tour_spot.getTouristspot_name();

        }
    }
}