package com.sdin.tourstamprally.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentLocationBinding;
import com.sdin.tourstamprally.databinding.ItemReRallyMapBinding;
import com.sdin.tourstamprally.databinding.LocationReItemBinding;
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.GuidDialog;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.ItemOnClickAb;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends BaseFragment {

    private FragmentLocationBinding binding;
    private String location_name;
    private Location_four location_four;
    private List<RallyMapDTO> list;
    private Map<Integer, Integer> spot_poinMap;
    private Map<Integer, Integer> spot_HistoryMap;


    public static LocationFragment newInstance(Location_four location_four) {

        Bundle args = new Bundle();

        LocationFragment fragment = new LocationFragment();
        //args.putSerializable("model", location_four);
        args.putParcelable("model", location_four);
        fragment.setArguments(args);
        Log.wtf("Locationa newInstance", String.valueOf(MainActivity.keyboardState));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //location_four = (Location_four) getArguments().getSerializable("model");
            location_four = getArguments().getParcelable("model");
            location_name = location_four.getLocation_name();
            Log.wtf("Locationa", String.valueOf(MainActivity.keyboardState));


        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false);
        binding.locationTxv.setText(location_name);


        if (!TextUtils.isEmpty(location_four.getLocation_img())) {
            Glide.with(requireContext()).load("http://coratest.kr/imagefile/bsr/" + location_four.getLocation_img()).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    binding.topLayout.setBackground(resource);

                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }

        getData();
        return binding.getRoot();
    }

    private void getData() {
        binding.locationPgb.setVisibility(View.VISIBLE);
        apiService.getTourLocation_for_spot(Utils.User_Idx, location_four.getLocation_idx()).enqueue(new Callback<List<RallyMapDTO>>() {
            @Override
            public void onResponse(@NotNull Call<List<RallyMapDTO>> call, @NotNull Response<List<RallyMapDTO>> response) {
                list = response.body();
                setRecylcerviewAdapter();
            }

            @Override
            public void onFailure(@NotNull Call<List<RallyMapDTO>> call, @NotNull Throwable t) {

            }
        });


    }

    private void setRecylcerviewAdapter() {

        LocationFragAdapter adapter = new LocationFragAdapter(new ArrayList<>(list));

        binding.recyclerviewLocationRe.setAdapter(adapter);
        binding.recyclerviewLocationRe.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        setProgress();
    }

    @SuppressLint("SetTextI18n")
    private void setProgress() {

        //int clear = 0;

        int AllLocationPercent = 0;
        int MyLocationPercent = 0;
        for (RallyMapDTO model : list) {
            AllLocationPercent += model.getAllCount();
            MyLocationPercent += model.getMyCount();
        }
        int allCountd = (int) ((double) MyLocationPercent / AllLocationPercent * 100);

        binding.seekBarLocation.setMax(AllLocationPercent);
        binding.seekBarLocation.setProgress(MyLocationPercent);
        binding.seekPercentTxv.setText(allCountd + "%");
        binding.locationPgb.setVisibility(View.GONE);

    }

    class LocationFragAdapter extends RecyclerView.Adapter<LocationFragAdapter.ViewHolder> {

        private final ArrayList<RallyMapDTO> arrayList;
        //private final ItemOnClick listener;
        private RallyMapDTO send_model;
        private Geocoder geocoder;


        public LocationFragAdapter(ArrayList<RallyMapDTO> arrayList) {
            this.arrayList = arrayList;
            //listener = (MainActivity) requireActivity();
            geocoder = new Geocoder(requireContext());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(
                    ItemReRallyMapBinding.inflate(
                            LayoutInflater.from(requireContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Glide.with(holder.itemView.getContext()).load(position % 2 == 0 ? R.drawable.icon_deep_blue : R.drawable.icon_sky_blue).into(holder.binding.locationImv);
            Glide.with(holder.itemView.getContext()).load(setProgress(position) ? R.drawable.stemp_ic : R.drawable.logo_gray).into(holder.binding.logoImv);
            holder.binding.spotName.setText(arrayList.get(position).getTouristspot_name());
            if (arrayList.get(position).getTouristspot_address() != null
                    && arrayList.get(position).getTouristspot_address().equalsIgnoreCase("null")) {
                holder.binding.explanTxv.setText(arrayList.get(position).getTouristspot_address());
            }

            Glide.with(holder.binding.spotImv.getContext())
                    .load("http://coratest.kr/imagefile/bsr/" + arrayList.get(position).getTouristspot_img())
                    /*.apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))*/
                    .error(R.drawable.sample_bg)
                    .into(holder.binding.spotImv);

        }

        private boolean setProgress(int position) {

            return arrayList.get(position).getAllCount() <= arrayList.get(position).getMyCount();

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemReRallyMapBinding binding;

            public ViewHolder(@NonNull ItemReRallyMapBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                binding.topLayout.setOnClickListener(v -> {
                    send_model = arrayList.get(getAbsoluteAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", send_model);
                    bundle.putString("title", send_model.getTouristspot_name());
                    Navigation
                            .findNavController(requireActivity(), R.id.nav_host)
                            .navigate(R.id.action_fragment_location_to_fragment_tour_spot_point, bundle);
                    //listener.ItemGuidForPoint(send_model);
                    /*GuidDialog guidDialog = new GuidDialog(requireContext());
                    guidDialog.show();
                    guidDialog.setClickListener(itemOnClick);*/
                });
            }
        }


        /*private final ItemOnClick itemOnClick = new ItemOnClickAb() {
            @Override
            public void ItemGuid(int position) {
                Log.d("dialog Onclick Listener", String.valueOf(position));
                if (position == 1 || position == 2){
                    listener.ItemGuid(position);
                }else {
                    listener.ItemGuidForPoint(send_model);
                }
            }
        };*/
    }


}