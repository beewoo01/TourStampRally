package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentTourSpotPointBinding;
import com.sdin.tourstamprally.databinding.LocationReItemBinding;
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.utill.ItemOnClick;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

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
    private RallyMapDTO rallyMapDTO;
    private MapView mapView = null;

    // 상세 랠리 맵

    public TourSpotPointFragment() {
        // Required empty public constructor
    }

    public static TourSpotPointFragment newInstance(RallyMapDTO rallyMapDTO) {
        TourSpotPointFragment fragment = new TourSpotPointFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", rallyMapDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rallyMapDTO = (RallyMapDTO) getArguments().getSerializable("model");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_spot_point, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
        binding.tourSpotPointPgb.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeMapView();
    }

    private void getData() {
        Map<String, Integer> map = new HashMap<>();
        map.put("user_idx", Utils.User_Idx);
        map.put("touristspot_idx", rallyMapDTO.getTouristspot_idx());
        apiService.getTourLocation_spotpoint(map).enqueue(new Callback<List<TouristSpotPoint>>() {
            @Override
            public void onResponse(@NotNull Call<List<TouristSpotPoint>> call, @NotNull Response<List<TouristSpotPoint>> response) {
                if (response.isSuccessful()) {
                    list = new ArrayList<>();
                    list = response.body();
                    ArrayList<TouristSpotPoint> arrayList = new ArrayList<>(list);
                    binding.recyclerviewLocationRe.setAdapter(new TourSpotPointAdapter(arrayList));
                    binding.recyclerviewLocationRe.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.recyclerviewLocationRe.setHasFixedSize(true);
                    setMap(arrayList);

                } else {

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TouristSpotPoint>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setMap(ArrayList<TouristSpotPoint> arrayList) {
        /*if (mapView != null){
            Log.wtf("mapview", "not_null");
            binding.mapLayout.removeView(mapView);
        }else{
            Log.wtf("mapview", "null");
            mapView = new MapView(requireActivity());
        }*/
        mapView = new MapView(requireActivity());

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(rallyMapDTO.getTouristspot_latitude(), rallyMapDTO.getTouristspot_longitude()), 2, true);
        mapView.zoomIn(true);
        mapView.zoomOut(true);

        binding.mapLayout.addView(mapView, 0);


        ArrayList<MapPoint> pointArrayList = new ArrayList<>();
        MapPOIItem[] markers = new MapPOIItem[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {

            pointArrayList.add(MapPoint.mapPointWithGeoCoord(arrayList.get(i).getTouristspotpoint_latitude(), arrayList.get(i).getTouristspotpoint_longitude()));
            markers[i] = new MapPOIItem();
            markers[i].setMapPoint(pointArrayList.get(i));
            markers[i].setItemName(arrayList.get(i).getTouristspotpoint_name());
            markers[i].setMarkerType(MapPOIItem.MarkerType.CustomImage);
            if (arrayList.get(i).getTouristhistory_idx() > 1) {
                markers[i].setCustomImageResourceId(R.drawable.marker_icon_success);
            } else {
                markers[i].setCustomImageResourceId(R.drawable.marker_icon_prev);
            }


            markers[i].setCustomImageAutoscale(true);
        }

        mapView.addPOIItems(markers);

        binding.tourSpotPointPgb.setVisibility(View.GONE);

    }

    private void removeMapView() {
        binding.mapLayout.removeView(mapView);
    }

    class TourSpotPointAdapter extends RecyclerView.Adapter<TourSpotPointAdapter.ViewHolder> {

        private final ArrayList<TouristSpotPoint> arrayList;
        //private final ItemOnClick listener;

        public TourSpotPointAdapter(ArrayList<TouristSpotPoint> arrayList) {
            this.arrayList = arrayList;
            //listener = (MainActivity) requireContext();
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
            holder.binding.bottomLine.setVisibility(position == arrayList.size() - 1 ? View.GONE : View.VISIBLE);
            holder.binding.bottomLayout.setVisibility(position == arrayList.size() - 1 ? View.GONE : View.VISIBLE);

            Glide.with(holder.itemView.getContext()).load(position % 2 == 0 ? R.drawable.icon_deep_blue : R.drawable.icon_sky_blue).into(holder.binding.locationImv);

            Log.wtf("history_idx", String.valueOf(list.get(position).getTouristhistory_idx()));
            Glide.with(holder.itemView.getContext()).load(list.get(position).getTouristhistory_idx() > 0 ? R.drawable.mainlogo : R.drawable.logo_gray).into(holder.binding.logoImv);
            holder.binding.spotName.setText(arrayList.get(position).getTouristspotpoint_name());
            holder.binding.explanTxv.setText(arrayList.get(position).getTouristspotpoint_explan());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final LocationReItemBinding binding;

            public ViewHolder(@NonNull LocationReItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                binding.topLayout.setOnClickListener(v -> {
                    removeMapView();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", arrayList.get(getAbsoluteAdapterPosition()));
                    Navigation.findNavController(requireActivity(), R.id.nav_host)
                            .navigate(R.id.action_fragment_tour_spot_point_to_fragment_tour_detail, bundle);
                    //listener.ItemGuidForDetail(arrayList.get(getAbsoluteAdapterPosition()));
                });

            }
        }
    }
}