package com.sdin.tourstamprally.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.adapter.VisitReAdapter;
import com.sdin.tourstamprally.adapter.swipe.ItemDecoration;
import com.sdin.tourstamprally.adapter.swipe.SwipeController;
import com.sdin.tourstamprally.adapter.swipe.SwipeControllerActions;
import com.sdin.tourstamprally.adapter.swipe.SwipeHelperCallback;
import com.sdin.tourstamprally.databinding.FragmentVisithistoryBinding;
import com.sdin.tourstamprally.databinding.VisithistoryItemBinding;
import com.sdin.tourstamprally.model.VisitHistory_Model;

import java.util.ArrayList;
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
        List<VisitHistory_Model> datas = new ArrayList<>();
        datas.add(new VisitHistory_Model(
                1,
                "한일교류의길",
                "부산관역시 동구 초량동",
                "21.08.17"

        ));
        datas.add(new VisitHistory_Model(
                2,
                "초량이바구길",
                "부산관역시 동구 초량동",
                "21.08.17"

        ));
        datas.add(new VisitHistory_Model(
                1,
                "부산포 개항가도",
                "부산관역시 동구 초량동",
                "21.08.17"

        ));
        datas.add(new VisitHistory_Model(
                1,
                "호랭이어슬렁길",
                "부산관역시 동구 초량동",
                "21.08.17"

        ));



        binding.visitRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.visitRecyclerView.setAdapter(new VisitReAdapter(new ArrayList<>(datas)));
        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                Toast.makeText(requireContext(), "ONLEFTCLICK", Toast.LENGTH_SHORT).show();
            }

        }, requireContext());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(binding.visitRecyclerView);

        binding.visitRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        /*VisitReAdapter adapter = new VisitReAdapter(new ArrayList<>(datas));
        SwipeHelperCallback swipeHelperCallback = new SwipeHelperCallback();
        swipeHelperCallback.setClamp(200f);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelperCallback);
        itemTouchHelper.attachToRecyclerView(binding.visitRecyclerView);
        //itemTouchHelper.attachToRecyclerView(binding.visitRecyclerView);
        binding.visitRecyclerView.setAdapter(adapter);
        binding.visitRecyclerView.addItemDecoration(new ItemDecoration());
        binding.visitRecyclerView.setOnTouchListener((v, event) -> {
            swipeHelperCallback.removePreviousClamp(binding.visitRecyclerView);
            return false;
        });*/


    }

    /*interface ItemActionListener{
        void onItemSwiped(int position);
    }*/


    /*class VisitReAdapter extends RecyclerView.Adapter<VisitReAdapter.ViewHolder> implements ItemActionListener{

        private ArrayList<VisitHistory_Model> arrayList;

        private VisitReAdapter(ArrayList<VisitHistory_Model> arrayList){
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(VisithistoryItemBinding.inflate(
                    LayoutInflater.from(
                            parent.getContext()), parent,
                    false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.titleTxv.setText(arrayList.get(position).getSpot_name());
            holder.binding.explanTxv.setText(arrayList.get(position).getSpot_explan());
            holder.binding.dateTxv.setText(arrayList.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public void onItemSwiped(int position) {

        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private VisithistoryItemBinding binding;
            public ViewHolder(@NonNull VisithistoryItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.heartImv.setOnClickListener(v -> {
                    Snackbar.make(v, "$label click", Snackbar.LENGTH_SHORT).show();
                });
            }
        }
    }

    class ItemDecoraion extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = 15;
            outRect.bottom = 15;
        }
    }*/


}
