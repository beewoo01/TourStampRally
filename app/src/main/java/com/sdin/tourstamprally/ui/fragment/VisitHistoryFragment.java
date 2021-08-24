package com.sdin.tourstamprally.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.adapter.VisitReAdapter;
import com.sdin.tourstamprally.adapter.swipe.ItemDecoration;

import com.sdin.tourstamprally.adapter.swipe.SwipeHelperCallback;
import com.sdin.tourstamprally.databinding.DirectionGuidTagItemBinding;
import com.sdin.tourstamprally.databinding.FragmentVisithistoryBinding;
import com.sdin.tourstamprally.model.VisitHistory_Model;
import com.sdin.tourstamprally.utill.ItemCliclListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisitHistoryFragment extends Fragment {

    private FragmentVisithistoryBinding binding;


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

        setVisitInit();
        setCatecory();


       /* adapter.itemCilcListener((position, model) -> {
            // TODO: 8/24/21 Deap 등록
        });*/


    }

    @SuppressLint("ClickableViewAccessibility")
    private void setVisitInit(){
        List<VisitHistory_Model> datas = new ArrayList<>();

        datas.add(new VisitHistory_Model(
                1,
                "한일교류의길",
                "부산관역시 동구 초량동",
                "21.08.17",

                false,
                null,
                null,
                null,
                null,
                0

        ));
        datas.add(new VisitHistory_Model(
                2,
                "초량이바구길",
                "부산관역시 동구 초량동",
                "21.08.17",

                false,
                null,
                null,
                null,
                null,
                0

        ));
        datas.add(new VisitHistory_Model(
                1,
                "부산포 개항가도",
                "부산관역시 동구 초량동",
                "21.08.17",

                false,
                null,
                null,
                null,
                null,
                0

        ));
        datas.add(new VisitHistory_Model(
                1,
                "호랭이어슬렁길",
                "부산관역시 동구 초량동",
                "21.08.17",

                false,
                null,
                null,
                null,
                null,
                0
        ));



        VisitReAdapter adapter = new VisitReAdapter(new ArrayList<>(datas));

        SwipeHelperCallback callback = new SwipeHelperCallback();
        callback.setClamp(-200f);

        //SwipeHelperCallback2 swipeHelperCallback2 = new SwipeHelperCallback2();
        //swipeHelperCallback2.setClamp(-200f);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.visitRecyclerView);

        binding.visitRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.visitRecyclerView.setAdapter(adapter);
        binding.visitRecyclerView.addItemDecoration(new ItemDecoration());
        binding.visitRecyclerView.setOnTouchListener((v, event) -> {
            //  swipeHelperCallback2.removePreviousClamp(binding.visitRecyclerView);
            callback.removePreviousClamp(binding.visitRecyclerView);
            return false;
        });
    }

    private void setCatecory(){
        binding.categoryRecyclerView.setAdapter(new CaterotyAdaper(new ArrayList<>(Arrays.asList(requireContext().getResources().getStringArray(R.array.area_direction)))));
    }


    class CaterotyAdaper extends RecyclerView.Adapter<CaterotyAdaper.ViewHolder>{

        private ArrayList<String> arrayList;

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
            }
        }
    }
}
