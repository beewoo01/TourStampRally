package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentLocationBinding;
import com.sdin.tourstamprally.databinding.LocationReItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;

import java.util.ArrayList;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private String location_name;

    public static LocationFragment newInstance(String location_name) {

        Bundle args = new Bundle();

        LocationFragment fragment = new LocationFragment();
        args.putString("", location_name);
        fragment.setArguments(args);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false);
        return binding.getRoot();
    }


    class LocationFragAdapter extends RecyclerView.Adapter<LocationFragAdapter.ViewHolder>{

        private ArrayList<Tour_Spot> arrayList;

        public LocationFragAdapter(ArrayList<Tour_Spot> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LocationReItemBinding.inflate(
                            LayoutInflater.from(requireContext()), parent,  false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.topLine.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE );
            holder.binding.bottomLine.setVisibility(position == arrayList.size() -1 ? View.VISIBLE : View.GONE);
            holder.binding.bottomLayout.setVisibility(position == arrayList.size() -1 ? View.VISIBLE : View.GONE);
            Glide.with(holder.itemView.getContext()).load(position % 2 == 0 ? R.drawable.icon_sky_blue : R.drawable.icon_deep_blue).into(holder.binding.locationImv);

            holder.binding.spotName.setText(arrayList.get(position).getTouristspot_name());
            holder.binding.spotName.setText(arrayList.get(position).getTouristspot_explan());


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