package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.DirectionGuid_Adapter;
import com.sdin.tourstamprally.adapter.DirectionGuid_Tag_Adapter;
import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.FragmentTourSpotPointBinding;
import com.sdin.tourstamprally.databinding.LocationReItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourSpotPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourSpotPointFragment extends BaseFragment {


    private FragmentTourSpotPointBinding binding;
    private List<Tour_Spot> list;

    // 상세 랠리 맵
    // 구글맵 이나 카카오맵 구현 하자

    public TourSpotPointFragment() {
        // Required empty public constructor
    }

    public static TourSpotPointFragment newInstance() {
        TourSpotPointFragment fragment = new TourSpotPointFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_spot_point, container,false);
        setMap();
        getData();

        return binding.getRoot();
    }

    private void getData(){
        apiService.getTourOrderBy(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    list = new ArrayList<>();
                    list = response.body();
                    ArrayList<Tour_Spot> arrayList = new ArrayList<>();
                    arrayList.addAll(list);
                    binding.recyclerviewLocationRe.setAdapter(new TourSpotPointAdapter(arrayList));

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setMap(){
        MapView mapView = new MapView(requireActivity());
        ViewGroup mapViewContainer = (ViewGroup) binding.mapView;

        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.1017, 129.0282), true);
        //mapView.setZoomLevel(7, true);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(35.1017, 129.0282), 2, true);
        mapView.zoomIn(true);
        mapView.zoomOut(true);

        mapViewContainer.addView(mapView);
        MapPoint point = MapPoint.mapPointWithGeoCoord(35.1012, 129.0257);
        ArrayList<MapPoint> arrayList = new ArrayList<>();
        arrayList.add(MapPoint.mapPointWithGeoCoord(35.1012, 129.0323));
        arrayList.add(MapPoint.mapPointWithGeoCoord(35.1017, 129.0282));
        arrayList.add(MapPoint.mapPointWithGeoCoord(35.1092, 128.9427));
        arrayList.add(MapPoint.mapPointWithGeoCoord(35.1150, 129.0387));
        mapView.setMapCenterPoint(point, true);



        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(point);
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.marker_icon);
        marker.setCustomImageAutoscale(true);
        //marker.setCustomImageAnchor(0.5f, 1.0f);
        marker.setCustomImageAnchor(0.5f, 1.0f);

        mapView.addPOIItem(marker);

        MapPOIItem[] markers = new MapPOIItem[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++){
            markers[i] = new MapPOIItem();
            markers[i].setMapPoint(arrayList.get(i));
            markers[i].setItemName("연습용 마커");
            markers[i].setMarkerType(MapPOIItem.MarkerType.CustomImage);
            markers[i].setCustomImageResourceId(R.drawable.marker_icon);
            markers[i].setCustomImageAutoscale(true);
        }
        mapView.addPOIItems(markers);
        // TODO: 7/23/21 데이터 적용해야함

    }

    class TourSpotPointAdapter extends RecyclerView.Adapter<TourSpotPointAdapter.ViewHolder>{

        private ArrayList<Tour_Spot> arrayList;

        public TourSpotPointAdapter(ArrayList<Tour_Spot> arrayList) {
            this.arrayList = arrayList;
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
            holder.binding.topLine.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.binding.bottomLine.setVisibility(position == arrayList.size() - 1 ?  View.VISIBLE : View.INVISIBLE);
            holder.binding.bottomLayout.setVisibility(position == arrayList.size() - 1 ?  View.VISIBLE : View.INVISIBLE);
            Glide.with(holder.itemView.getContext()).load(position % 2 == 0 ? R.drawable.icon_sky_blue : R.drawable.icon_deep_blue).into(holder.binding.locationImv);

            Glide.with(holder.itemView.getContext()).load(
                    Integer.parseInt(arrayList.get(position).getTouristhistory_touristspotpoint_idx()) == arrayList.get(position).getTouristspot_idx() ?
                            R.drawable.mainlogo : R.drawable.logo_gray
                    ).into(holder.binding.locationImv);

            holder.binding.spotName.setText(arrayList.get(position).getTouristspotpoint_name());
            holder.binding.spotName.setText(arrayList.get(position).getTouristspotpoint_explan());
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
            }
        }
    }
}