package com.sdin.tourstamprally.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.DirectionGuid_Tag_Adapter;
import com.sdin.tourstamprally.adapter.VisitReAdapter;
import com.sdin.tourstamprally.adapter.swipe.ItemDecoration;

import com.sdin.tourstamprally.adapter.swipe.SwipeHelperCallback;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.databinding.FragmentVisithistoryBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.VisitCountModel;
import com.sdin.tourstamprally.model.VisitHistory_Model;
import com.sdin.tourstamprally.model.history_spotModel;
import com.sdin.tourstamprally.model.history_spotModel2;
import com.sdin.tourstamprally.ui.activity.LoginActivity;
import com.sdin.tourstamprally.utill.ItemCliclListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitHistoryFragment extends BaseFragment {

    private FragmentVisithistoryBinding binding;
    private List<history_spotModel2> history_spotList;
    private VisitReAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_visithistory, container, false);
        return binding.getRoot();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.VISIBLE);
        setVisitInit();
        setCatecory();

        binding.searchEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(binding.searchEdt.getText().toString());
            }

        });

        binding.searchBtn.setOnClickListener(v -> {
            search(binding.searchEdt.getText().toString());
        });


    }

    private void insert_intest(int touristspot_idx) {
        apiService.insert_intest(Utils.User_Idx, touristspot_idx).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                if (response.isSuccessful()) {
                    ShowToast("찜하기에 성공 하셨습니다.", requireContext());
                } else {
                    //Log.wtf("찜하기 실패", response.toString());
                    ShowToast("찜하기에 실패 하셨습니다.", requireContext());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setVisitInit() {
        apiService.getHistorySpot(Utils.User_Idx).enqueue(new Callback<List<history_spotModel2>>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResponse(@NotNull Call<List<history_spotModel2>> call, @NotNull Response<List<history_spotModel2>> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    if (response.isSuccessful()) {

                        if (response.body() != null) {

                            history_spotList = new ArrayList<>(response.body());

                            /*for (int i = 0; i < history_spotList.size(); i++) {
                                Log.wtf("spotNAm222e", history_spotList.get(i).getTouristspot_name());
                                Log.wtf("getAllCount!!!", String.valueOf(history_spotList.get(i).getAllCount()));
                                Log.wtf("getMyCount!!!", String.valueOf(history_spotList.get(i).getMyCount()));
                            }*/

                            SwipeHelperCallback callback = new SwipeHelperCallback();
                            callback.setClamp(-200f);
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                            itemTouchHelper.attachToRecyclerView(binding.visitRecyclerView);
                            ArrayList<history_spotModel2> arrayList = new ArrayList<>(history_spotList);
                            adapter = new VisitReAdapter(arrayList, requireContext());

                            binding.visitRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            binding.visitRecyclerView.setAdapter(adapter);
                            binding.visitRecyclerView.addItemDecoration(new ItemDecoration());
                            binding.visitRecyclerView.setOnTouchListener((v, event) -> {
                                //  swipeHelperCallback2.removePreviousClamp(binding.visitRecyclerView);
                                callback.removePreviousClamp(binding.visitRecyclerView);
                                return false;
                            });
                            setRecyclerView();

                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<history_spotModel2>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setRecyclerView() {
        adapter.itemCilcListener(new ItemCliclListener() {
            @Override
            public void deapsClick(int position, history_spotModel2 model) {
                apiService.select_interest_status(Utils.User_Idx, model.getTouristspot_idx()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                        if (response.isSuccessful()) {
                            //ShowToast("찜하기에 성공 하셨습니다.", requireContext());
                            //Log.wtf("response.body()", String.valueOf(response.body()));
                            if (response.body() == 0) {
                                //  Log.wtf("onResponse", "찜하기 000000");
                                insert_intest(model.getTouristspot_idx());
                            }

                        } else {
                            //Log.wtf("찜하기 실패", response.toString());
                            ShowToast("찜하기에 실패 하셨습니다.", requireContext());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                        ShowToast("찜하기에 실패 하셨습니다.", requireContext());
                        t.printStackTrace();
                    }

                });
            }

            @Override
            public void delReview(int review_idx, int position) {
                //리뷰 삭제
                apiService.deleteReview(review_idx).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Integer>() {
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                                if (integer > 0){
                                    adapter.setChange(position);
                                    Toast.makeText(requireContext(), "리뷰 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }
                        });
            }

        });
    }


    private void setCatecory() {

        CaterotyAdaper caterotyAdaper = new CaterotyAdaper(new ArrayList<>(Arrays.asList(requireContext().getResources().getStringArray(R.array.area_direction))));
        binding.categoryRecyclerView.setAdapter(caterotyAdaper);
        caterotyAdaper.itemOnClick = location -> {
            if (location.equals("전체")) {
                location = "";
            }
            search(location);
        };
    }


    private void search(String searchData) {

        ArrayList<history_spotModel2> arrayList = new ArrayList<>();
        if (searchData.length() == 0) {

            arrayList.addAll(history_spotList);

        } else {

            for (int i = 0; i < history_spotList.size(); i++) {
                if (history_spotList.get(i).getLocation_name().toLowerCase().contains(searchData)) {
                    arrayList.add(history_spotList.get(i));
                }

                if (history_spotList.get(i).getTouristspot_name().toLowerCase().contains(searchData)) {
                    arrayList.add(history_spotList.get(i));
                }

            }
        }

        adapter.setList(arrayList);
        adapter.notifyDataSetChanged();
    }


    interface ItemOnClick {
        void onCategoryClick(String location);
    }

    private class CaterotyAdaper extends RecyclerView.Adapter<CaterotyAdaper.ViewHolder> {

        private final ArrayList<String> arrayList;

        private ItemOnClick itemOnClick;

        private int selectedItem = 0;
        private int prevSelected = -1;

        public CaterotyAdaper(ArrayList<String> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            DirectionGuidTagItemBinding binding = DirectionGuidTagItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.tagItemTxv.setText(arrayList.get(position));
            setBackground(position, holder);
        }


        private void setBackground(int position, CaterotyAdaper.ViewHolder holder) {
            if (selectedItem == position) {
                holder.binding.tagItemTxv.setBackground(ContextCompat.getDrawable(holder.binding.tagItemTxv.getContext(), R.drawable.bg_rounded_category_selected));
                holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.getContext(), R.color.white));

            } else {
                holder.binding.tagItemTxv.setBackground(ContextCompat.getDrawable(holder.binding.tagItemTxv.getContext(), R.drawable.bg_rounded_category));
                holder.binding.tagItemTxv.setTextColor(ContextCompat.getColor(holder.binding.tagItemTxv.getContext(), R.color.mainColor));
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            DirectionGuidTagItemBinding binding;

            public ViewHolder(@NonNull DirectionGuidTagItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.tagItemTxv.setOnClickListener(v -> {
                    prevSelected = selectedItem;
                    selectedItem = getAbsoluteAdapterPosition();
                    if (getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                        if (itemOnClick != null) {
                            itemOnClick.onCategoryClick(arrayList.get(getAbsoluteAdapterPosition()));
                            notifyItemChanged(selectedItem);
                            notifyItemChanged(prevSelected);
                        }
                    }
                });

            }
        }
    }
}
