package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentTourSpotPointBinding;
import com.sdin.tourstamprally.databinding.LocationReItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.utill.ItemOnClick;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourSpotPointFragment extends BaseFragment {


    private FragmentTourSpotPointBinding binding;
    private List<TouristSpotPoint> list;
    private Tour_Spot tour_spot;

    // 상세 랠리 맵
    // 구글맵 이나 카카오맵 구현 하자

    public TourSpotPointFragment() {
        // Required empty public constructor
    }

    public static TourSpotPointFragment newInstance(Tour_Spot tour_spot) {
        TourSpotPointFragment fragment = new TourSpotPointFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", tour_spot);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tour_spot = (Tour_Spot) getArguments().getSerializable("model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_spot_point, container,false);
        binding.tourSpotPointPgb.setVisibility(View.VISIBLE);

        getData();

        return binding.getRoot();
    }

    private void getData(){
        Map<String, Integer> map = new HashMap<>();
        map.put("user_idx", Utils.User_Idx);
        map.put("touristspot_idx", Integer.valueOf(tour_spot.getTouristspotpoint_touristspot_idx()));
        apiService.getTourLocation_spotpoint(map).enqueue(new Callback<List<TouristSpotPoint>>() {
            @Override
            public void onResponse(Call<List<TouristSpotPoint>> call, Response<List<TouristSpotPoint>> response) {
                if (response.isSuccessful()){
                    list = new ArrayList<>();
                    list = response.body();
                    ArrayList<TouristSpotPoint> arrayList = new ArrayList<>(list);
                    binding.recyclerviewLocationRe.setAdapter(new TourSpotPointAdapter(arrayList));
                    binding.recyclerviewLocationRe.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.recyclerviewLocationRe.setHasFixedSize(true);
                    setMap(arrayList);

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<TouristSpotPoint>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setMap(ArrayList<TouristSpotPoint> arrayList){
        MapView mapView = new MapView(requireActivity());
        ViewGroup mapViewContainer = binding.mapView;

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(tour_spot.getTouristspot_latitude(), tour_spot.getTouristspot_longitude()), 2, true);
        mapView.zoomIn(true);
        mapView.zoomOut(true);

        mapViewContainer.addView(mapView);


        ArrayList<MapPoint> pointArrayList = new ArrayList<>();
        MapPOIItem[] markers = new MapPOIItem[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i ++){
            pointArrayList.add(MapPoint.mapPointWithGeoCoord(arrayList.get(i).getTouristspotpoint_latitude(), arrayList.get(i).getTouristspotpoint_longitude()));
            markers[i] = new MapPOIItem();
            markers[i].setMapPoint(pointArrayList.get(i));
            markers[i].setItemName(arrayList.get(i).getTouristspotpoint_name());
            markers[i].setMarkerType(MapPOIItem.MarkerType.CustomImage);
            markers[i].setCustomImageResourceId(R.drawable.marker_icon);
            markers[i].setCustomImageAutoscale(true);
        }

        mapView.addPOIItems(markers);

        binding.tourSpotPointPgb.setVisibility(View.GONE);

    }

    private void removeMapView(){
        binding.topLayout.removeAllViews();
    }

    class TourSpotPointAdapter extends RecyclerView.Adapter<TourSpotPointAdapter.ViewHolder>{

        private ArrayList<TouristSpotPoint> arrayList;
        private ItemOnClick listener;

        public TourSpotPointAdapter(ArrayList<TouristSpotPoint> arrayList) {
            this.arrayList = arrayList;
            listener = (MainActivity) requireContext();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LocationReItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.topLine.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE );
            holder.binding.bottomLine.setVisibility(position == arrayList.size() -1 ? View.GONE : View.VISIBLE);
            holder.binding.bottomLayout.setVisibility(position == arrayList.size() -1 ? View.GONE : View.VISIBLE);

            Glide.with(holder.itemView.getContext()).load(position % 2 == 0 ? R.drawable.icon_deep_blue : R.drawable.icon_sky_blue).into(holder.binding.locationImv);


            Glide.with(holder.itemView.getContext()).load(list.get(position).getTouristhistory_idx() != null ? R.drawable.mainlogo : R.drawable.logo_gray).into(holder.binding.logoImv);
            holder.binding.spotName.setText(arrayList.get(position).getTouristspotpoint_name());
            holder.binding.explanTxv.setText(arrayList.get(position).getTouristspotpoint_explan());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            private LocationReItemBinding binding;
            public ViewHolder(@NonNull LocationReItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                
                binding.topLayout.setOnClickListener( v -> {
                    removeMapView();
                    listener.ItemGuidForDetail(arrayList.get(getAdapterPosition()));
                });

            }
        }
    }
}