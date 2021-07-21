package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.DirectionGuidLocationItemBinding;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.databinding.FragmentDirectionGuidBinding;
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionGuidFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionGuidFragment extends BaseFragment {
    //길안내 관광지
    private FragmentDirectionGuidBinding binding;
    private List<Tour_Spot> tourList;

    public DirectionGuidFragment() {
        // Required empty public constructor
    }

    public static DirectionGuidFragment newInstance() {
        DirectionGuidFragment fragment = new DirectionGuidFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private void getData(){
        apiService.getTour(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    tourList = response.body();
                    Log.d("?????", tourList.get(0).toString());
                    RallyRecyclerviewAdapter adapter = new RallyRecyclerviewAdapter(tourList);
                    binding.locationRe.setAdapter(adapter);
                    binding.locationRe.setHasFixedSize(true);

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //direction_guid_location_item
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direction_guid, container, false);
        getData();
        return binding.getRoot();
    }

    private class RallyRecyclerviewAdapter extends RecyclerView.Adapter<RallyRecyclerviewAdapter.Viewholder>{

        private List<Tour_Spot> list;

        public RallyRecyclerviewAdapter(List<Tour_Spot> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new Viewholder(
                    DirectionGuidLocationItemBinding.inflate(
                            LayoutInflater.from(requireContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            holder.binding.locationSpotTxv.setText(list.get(position).getLocation_name());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            DirectionGuidLocationItemBinding binding;
            public Viewholder(@NonNull DirectionGuidLocationItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private class TagAdpater extends RecyclerView.Adapter<TagAdpater.Viewholder>{

        private List<Tour_Spot> list;

        public TagAdpater(List<Tour_Spot> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Viewholder(
                    DirectionGuidTagItemBinding.inflate(
                            LayoutInflater.from(requireContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            holder.binding.tagItemTxv.setText(list.get(position).getTouristspot_tag());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder{
            DirectionGuidTagItemBinding binding;

            public Viewholder(@NonNull DirectionGuidTagItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }



}