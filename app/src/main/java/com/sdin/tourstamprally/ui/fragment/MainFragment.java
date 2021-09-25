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

            // binding.rallyRecyclerview.addItemDecoration(new RallyRecyclerviewAdapterDeco(2, 50, true));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);*/
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

    /*private class RallyRecyclerviewAdapterDeco extends RecyclerView.ItemDecoration{
        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public RallyRecyclerviewAdapterDeco(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }*/

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
                /*if (adapterList.get(position).getAllPointCount() == adapterList.get(position).getMyHistoryCount()){
                    holder.binding.dibsImv.setImageResource(R.drawable.full_heart_resize);
                }*/
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


    /*private class RallyRecyclerviewAdapter extends RecyclerView.Adapter<RallyRecyclerviewAdapter.ViewHolder>{

        private GuidDialog guidDialog;
        private final Context context;
        private final ArrayList<Tour_Spot> adapterList;
        private final Map<Integer, Integer> progress_Map;
        private final Map<Integer, Integer> history_Map;
        private final ItemOnClick itemOnClick;


        public RallyRecyclerviewAdapter(Context context, ArrayList<Tour_Spot> adapterList, Map<Integer, Integer> progress_Map, Map<Integer, Integer> history_Map){
            this.context = context;
            this.adapterList = adapterList;
            this.progress_Map = progress_Map;
            this.history_Map = history_Map;
            this.itemOnClick = (ItemOnClick) requireActivity();
        }

        private int average(){
            int sum = 0;
            for (int i = 0; i < adapterList.size(); i++){
                sum += adapterList.get(i).getTouristspot_checkin_count();
            }

            return sum / adapterList.size();
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


            if (!TextUtils.isEmpty(adapterList.get(position).getLocation_img())){
                Log.wtf("이미지??", "???");
                Glide.with(context).load("http://coratest.kr/imagefile/bsr/" + adapterList.get(position).getLocation_img())
                        .error(ContextCompat.getDrawable(requireContext(), R.drawable.sample_bg))
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

            if (adapterList.get(position).getTouristspot_checkin_count() > average()) {
                //list 내 CheckCount 의 평균값을 구한 뒤 평균 이상이면 HOT 띄우기
                holder.binding.newsImv.setVisibility(View.VISIBLE);
                Glide.with(holder.binding.newsImv.getContext()).load(R.drawable.hot_icon).into(holder.binding.newsImv);
            }else if (((System.currentTimeMillis() - adapterList.get(position).getTouristspot_createtime()) / 10000 ) / (24 * 60 * 60) < 8){
                //현재 시간과 관광지 등록시간이 7일 이하면 NEW 띄우기
                holder.binding.newsImv.setVisibility(View.VISIBLE);
                Glide.with(holder.binding.newsImv.getContext()).load(R.drawable.new_icon).into(holder.binding.newsImv);
            }else {
                holder.binding.newsImv.setVisibility(View.GONE);
            }

            if (progress_Map.get(adapterList.get(position).getLocation_idx()) != null
                    &&  history_Map.get(adapterList.get(position).getLocation_idx()) != null) {


                int allContents = progress_Map.get(adapterList.get(position).getLocation_idx());
                int clearCount = history_Map.get(adapterList.get(position).getLocation_idx());

                holder.binding.seekbar.setMax(allContents);
                holder.binding.seekbar.setProgress(clearCount);
                int allCountd = (int) ((double) clearCount /  (double) allContents * 100);
                holder.binding.seekTxv.setText(allCountd + "%");

            }else {
                holder.binding.seekbar.setProgress(0);
                holder.binding.seekTxv.setText(0 + "%");
            }


            Glide.with(holder.binding.dibsImv.getContext()).load(ContextCompat.getDrawable(requireContext()
                    , Integer.parseInt(holder.binding.dibsImv.getTag().toString()) == 0?
                             R.drawable.heart_resize : R.drawable.full_heart_resize))
                    .into(holder.binding.dibsImv);

            holder.binding.dibsImv.setOnClickListener( v -> {

                if (Integer.parseInt(holder.binding.dibsImv.getTag().toString()) == 0) {
                    holder.binding.dibsImv.setTag(1);
                    Glide.with(holder.binding.dibsImv.getContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.full_heart_resize)).into(holder.binding.dibsImv);
                } else {
                    holder.binding.dibsImv.setTag(0);
                    Glide.with(holder.binding.dibsImv.getContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.heart_resize)).into(holder.binding.dibsImv);
                }

            });



        }

        @Override
        public int getItemCount() {
            if (adapterList.size() > 4){
                return 4;
            }else {
                return adapterList.size();
            }

        }



        public class ViewHolder extends RecyclerView.ViewHolder{

            public StepRallyLocationItemBinding binding;

            public ViewHolder(@NonNull StepRallyLocationItemBinding binding) {
                super(binding.getRoot());

                this.binding = binding;

                binding.stepRallyBg.setOnClickListener( v -> itemOnClick.onItemClick(adapterList.get(getAbsoluteAdapterPosition())));
            }
        }
    }*/
}