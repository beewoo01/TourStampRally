package com.sdin.tourstamprally.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.DeabsItemBinding;
import com.sdin.tourstamprally.databinding.FragmentDeabsBinding;
import com.sdin.tourstamprally.model.InterestModel;
import com.sdin.tourstamprally.model.Location;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeabsFragment extends BaseFragment {

    private FragmentDeabsBinding binding;
    //private List<Tour_Spot2> list;
    private ArrayList<InterestModel> interestList;
    private List<Location> location_list;
    private DeabsAdapter deabsAdapter;
    private DeapLocationAdapter deapLocationAdapter;
    private Location nowLocation;
    private int nowCategory = 0; // 0 -> 전체  1 -> 관광지 2 -> 매장
    private Map<Integer, Pair<RelativeLayout, TextView>> map;

    public DeabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deabs, container, false);
        binding.setFragment(this);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        map = new HashMap<>();
        map.put(binding.storeTxvBg.getId(), new Pair<>(binding.storeTxvBg, binding.storeTxv));
        map.put(binding.allTxvBg.getId(), new Pair<>(binding.allTxvBg, binding.allTxv));
        map.put(binding.tourTxvBg.getId(), new Pair<>(binding.tourTxvBg, binding.tourTxv));
        binding.fragmentDeabsPgb.setVisibility(View.VISIBLE);
        getData();
    }



    public void buttonClick(View view) {
        for (Integer key : map.keySet()) {
            int txvId = map.get(key).second.getId();
            Drawable img;
            if (key == view.getId()) {
                map.get(key).first.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.icon_bg_blue_resize));
                map.get(key).second.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

                if (txvId == binding.allTxv.getId()) {
                    img = ContextCompat.getDrawable(requireContext(), R.drawable.folder_icon_white_resize);
                    nowCategory = 0;

                    //setDeabsCate(0);

                } else if (txvId == binding.storeTxv.getId()) {
                    img = ContextCompat.getDrawable(requireContext(), R.drawable.store_icon_white_resize);
                    nowCategory = 2;
                    //setDeabsCate(1);
                    //sort();

                } else {
                    nowCategory = 1;
                    //setDeabsCate(2);
                    img = ContextCompat.getDrawable(requireContext(), R.drawable.map_icon_white_resize);
                }

                map.get(key).second.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);

            } else {
                map.get(key).first.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.icon_bg_gray_resize));
                map.get(key).second.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));

                if (txvId == binding.allTxv.getId()) {
                    img = ContextCompat.getDrawable(requireContext(), R.drawable.folder_icon_black_resize);

                } else if (txvId == binding.storeTxv.getId()) {
                    img = ContextCompat.getDrawable(requireContext(), R.drawable.store_icon_resize);

                } else {
                    img = ContextCompat.getDrawable(requireContext(), R.drawable.map_icon_resize);

                }

                map.get(key).second.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
            }

        }

        deabsAdapter.change();
    }

    private void getData() {

        apiService.getLocations().enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(@NotNull Call<List<Location>> call, @NotNull Response<List<Location>> response) {
                if (response.isSuccessful()) {
                    location_list = response.body();
                    nowLocation = new Location(0, "전체");
                    //location_list.add(0, nowLocation);
                    location_list.add(0, nowLocation);
                    deapLocationAdapter = new DeapLocationAdapter(new ArrayList<>(location_list));
                    binding.locationRe.setAdapter(deapLocationAdapter);
                    binding.fragmentDeabsPgb.setVisibility(View.GONE);

                    deapLocationAdapter.itemOnclickLo = location -> {
                        nowLocation = location;
                        deabsAdapter.change();
                    };

                } else {

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Location>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

        apiService.getAllInterest(Utils.User_Idx).enqueue(new Callback<List<InterestModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<InterestModel>> call, @NotNull Response<List<InterestModel>> response) {
                if (response.isSuccessful()) {
                    List<InterestModel> list = response.body();
                    if (list != null) {
                        interestList = new ArrayList<>(list);
                        ArrayList<InterestModel> arrayList = new ArrayList<>(interestList);
                        deabsAdapter = new DeabsAdapter(arrayList);

                        binding.deabsRe.setAdapter(deabsAdapter);
                        binding.deabsRe.setItemAnimator(null);
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<List<InterestModel>> call, @NotNull Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public interface ItemOnclickLo {
        void onItemClick(Location location);
    }

    class DeapLocationAdapter extends RecyclerView.Adapter<DeapLocationAdapter.ViewHolder> {

        private final ArrayList<Location> arrayList;

        private ItemOnclickLo itemOnclickLo;
        private int selectedItem = 0;

        public DeapLocationAdapter(ArrayList<Location> arrayList) {
            this.arrayList = arrayList;
        }

        public void setOnClickListener(ItemOnclickLo itemOnclickLo) {
            this.itemOnclickLo = itemOnclickLo;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.deabs_location_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.textView.setText(arrayList.get(position).getLocation_name());

            holder.textView.setOnClickListener(v -> {
                itemOnclickLo.onItemClick(arrayList.get(position));
                int finalPosition = position;
                selectedItem = finalPosition;
                notifyDataSetChanged();
            });

            if (selectedItem == position) {
                holder.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            } else {
                holder.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray));
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.location_for_deabs);
            }
        }
    }


    class DeabsAdapter extends RecyclerView.Adapter<DeabsAdapter.ViewHolder> {

        private final ArrayList<InterestModel> arrayList;

        public DeabsAdapter(ArrayList<InterestModel> arrayList) {
            this.arrayList = arrayList;
        }

        public void change(){
            arrayList.clear();
            if (nowCategory == 0 && nowLocation.getLocation_idx() == 0){
                arrayList.addAll(interestList);
            }else {
                for (InterestModel model : interestList){


                    if (nowCategory == model.getTs_type() && nowLocation.getLocation_idx() == model.getLocation_idx()){
                        arrayList.add(model);
                    }else if (nowLocation.getLocation_idx() == 0 && nowCategory == model.getTs_type()){
                        arrayList.add(model);
                    }else if (nowCategory == 0 && nowLocation.getLocation_idx() == model.getLocation_idx()){
                        arrayList.add(model);
                    }
                }
            }
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(
                    DeabsItemBinding.inflate(
                            LayoutInflater.from(requireContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int finalPosition = position;
            Glide.with(requireContext())
                    .load("http://coratest.kr/imagefile/bsr/"
                            + arrayList.get(finalPosition).getTs_img())
                    .error(R.drawable.sample_bg)
                    .into(holder.binding.tourImageImv);
            holder.binding.deabImv.setVisibility(View.VISIBLE);
            holder.binding.title.setText(arrayList.get(finalPosition).getTs_name());
            holder.binding.tag.setText(arrayList.get(finalPosition).getTs_tag());
            holder.binding.deabImv.setOnClickListener(v -> {
                apiService.remove_intest(arrayList.get(finalPosition).getInter_idx(),
                        arrayList.get(finalPosition).getTs_type()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                        if (response.isSuccessful()) {
                            //Log.wtf("RESULT", response.body().toString());
                            if (response.body() == 1) {
                                removeData(finalPosition);

                            }
                        } else {
                            //Log.wtf("ERROR", response.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            });
        }


        private void removeData(int position){
            interestList.remove(arrayList.get(position));
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position, arrayList.size());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            DeabsItemBinding binding;

            public ViewHolder(@NonNull DeabsItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

            }
        }
    }


}