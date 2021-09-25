package com.sdin.tourstamprally.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentMainBinding;
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding;
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.utill.ItemOnClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentMainBinding binding;
    private List<Tour_Spot> tourList;
    private List<Location_four> tourList_test;
    private ItemOnClick listener;

    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.setFragment(this);
        //binding.tourRallyPgb.setVisibility(View.VISIBLE);

        binding.rallyRecyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2) {

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        getTop4Location();

        return binding.getRoot();
    }

    public void cuverClick() {
        //공지사항
        listener = (MainActivity) requireActivity();
        listener.onClick(1);
    }

    public void moreClick() {
        listener = (MainActivity) requireActivity();
        //listener.SetFragment("direction_guid");
        listener.SetFragment(new ArrayList<>(tourList_test));
    }

    private void getTop4Location() {


        apiService.getFourLocations(Utils.User_Idx).enqueue(new Callback<List<Location_four>>() {
            @Override
            public void onResponse(@NotNull Call<List<Location_four>> call, @NotNull Response<List<Location_four>> response) {
                binding.tourRallyPgb.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    tourList_test = response.body();
                    if (tourList_test != null) {
                        setData(new ArrayList<>(tourList_test));
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Location_four>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private void setData(ArrayList<Location_four> list) {

        RallyRecyclerviewAdapter adapter = new RallyRecyclerviewAdapter(list);
        binding.rallyRecyclerview.setAdapter(adapter);
        //binding.rallyRecyclerview.addItemDecoration(new RallyRecyclerviewAdapterDeco(2, 50, true));
    }


    private class RallyRecyclerviewAdapter extends RecyclerView.Adapter<RallyRecyclerviewAdapter.ViewHolder> {

        private final ArrayList<Location_four> adapterList;
        private final ItemOnClick itemOnClick = (MainActivity) requireActivity();

        public RallyRecyclerviewAdapter(ArrayList<Location_four> adapterList) {
            this.adapterList = adapterList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    StepRallyLocationItemBinding.inflate(
                            LayoutInflater.from(requireContext()),
                            parent,
                            false)
            );
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.location.setText(adapterList.get(position).getLocation_name());

            if (adapterList.get(position).getLocation_img() != null && !adapterList.get(position).getLocation_img().equals("null")) {

                Glide.with(requireContext()).load("http://coratest.kr/imagefile/bsr/" + adapterList.get(position).getLocation_img())
                        .error(R.drawable.sample_bg)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                holder.binding.mainLayout.setBackground(resource);

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            } else {

                Glide.with(requireContext()).load(R.drawable.sample_bg)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                holder.binding.mainLayout.setBackground(resource);

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }

            if (adapterList.get(position).getPopular() > adapterList.get(position).getAllPointCount()) {
                holder.binding.newsImv.setVisibility(View.VISIBLE);
                holder.binding.newsImv.setImageResource(R.drawable.hot_icon);
            } else {
                holder.binding.newsImv.setVisibility(View.VISIBLE);
                holder.binding.newsImv.setImageResource(R.drawable.new_icon);
            }

            Log.wtf("getAllPointCount", String.valueOf(adapterList.get(position).getAllPointCount()));
            Log.wtf("getMyInterCount", String.valueOf(adapterList.get(position).getMyInterCount()));
            if (adapterList.get(position).getAllSpotCount() > 0) {
                if (adapterList.get(position).getAllSpotCount() == adapterList.get(position).getMyInterCount()) {
                    holder.binding.dibsImv.setImageResource(R.drawable.full_heart_resize);
                } else {
                    holder.binding.dibsImv.setImageResource(R.drawable.heart_resize);
                }
            }


            holder.binding.dibsImv.setOnClickListener(v -> {
                holder.binding.dibsImv.setEnabled(false);
                deapClick(position, (ImageButton) v);
            });


            if (adapterList.get(position).getTouristspotpoint_tag() != null) {
                holder.binding.hashtagTxv.setText(adapterList.get(position).getTouristspotpoint_tag());
            }


            int allCountd = (int) ((double) adapterList.get(position).getMyHistoryCount() / (double) adapterList.get(position).getAllPointCount() * 100);
            holder.binding.seekbar.setMax(adapterList.get(position).getAllPointCount());
            holder.binding.seekbar.setProgress(adapterList.get(position).getMyHistoryCount());
            holder.binding.seekTxv.setText(allCountd + "%");

        }

        private void deapClick(int position, ImageButton deapBtn) {
            if (adapterList.get(position).getAllSpotCount() ==
                    adapterList.get(position).getMyInterCount()) {
                //DEAP 삭제
                Glide.with(deapBtn.getContext()).load(R.drawable.heart_resize).into(deapBtn);
                apiService.multipleDelDeaps(Utils.User_Idx,
                        adapterList.get(position).getLocation_idx()).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Integer>() {
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                                deapBtn.setEnabled(true);
                                Log.wtf("Result===", String.valueOf(integer));
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.wtf("Result===", "error");
                                deapBtn.setEnabled(true);
                                e.printStackTrace();
                            }
                        });



            } else {
                //DEAP 추가
                Glide.with(deapBtn.getContext()).load(R.drawable.full_heart_resize).into(deapBtn);
                apiService.multipleInserDeaps(Utils.User_Idx,
                        adapterList.get(position).getLocation_idx()).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Integer>() {
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                                deapBtn.setEnabled(true);
                                Log.wtf("Result===", String.valueOf(integer));
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                deapBtn.setEnabled(true);
                                Log.wtf("Result===", "error");
                                e.printStackTrace();
                            }
                        });


            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private StepRallyLocationItemBinding binding;

            public ViewHolder(@NonNull StepRallyLocationItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.stepRallyBg.setOnClickListener(v -> itemOnClick.onItemClick(adapterList.get(getAbsoluteAdapterPosition())));
            }
        }

    }


}