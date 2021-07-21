package com.sdin.tourstamprally.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentMainBinding;
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.GuidDialog;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.ItemOnClickAb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentMainBinding binding;
    private List<Tour_Spot> tourList;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.setFragment(this);
        binding.tourRallyPgb.setVisibility(View.VISIBLE);

        binding.rallyRecyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2){

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        getTop4Location();

        return binding.getRoot();
    }

    public void moreClick(){
        listener = (MainActivity) requireActivity();
        listener.SetFragment("direction_guid");
    }

    private void getTop4Location(){
        apiService.getTour(Utils.User_Idx).enqueue(new Callback<List<Tour_Spot>>() {
            @Override
            public void onResponse(Call<List<Tour_Spot>> call, Response<List<Tour_Spot>> response) {
                if (response.isSuccessful()){
                    tourList = response.body();
                    binding.tourRallyPgb.setVisibility(View.GONE);
                    Log.d("?????", tourList.get(0).toString());
                    List<Tour_Spot> paramlist = new ArrayList<>();
                    paramlist.add(tourList.get(0));
                    paramlist.add(tourList.get(2));
                    paramlist.add(tourList.get(4));
                    paramlist.add(tourList.get(6));
                    RallyRecyclerviewAdapter adapter = new RallyRecyclerviewAdapter(requireContext(), paramlist);
                    binding.rallyRecyclerview.setAdapter(adapter);
                    binding.rallyRecyclerview.addItemDecoration(new RallyRecyclerviewAdapterDeco(2, 50, true));

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Tour_Spot>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private class RallyRecyclerviewAdapterDeco extends RecyclerView.ItemDecoration{
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

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
    }


    private class RallyRecyclerviewAdapter extends RecyclerView.Adapter<RallyRecyclerviewAdapter.ViewHolder>{

        private GuidDialog guidDialog;
        //private DefaultDialog dialog;
        private Context context;
        private ItemOnClick listener;
        private List<Tour_Spot> list;

        private ItemOnClick itemOnClick = new ItemOnClickAb() {
            @Override
            public void ItemGuid(int position) {
                Log.d("dialog Onclick Listener", String.valueOf(position));
                switch (position){
                    case 0 :
                        listener.ItemGuid(0);
                        break;
                    case 1 :
                        listener.ItemGuid(1);
                        //NFC
                        break;
                    case 2 :
                        listener.ItemGuid(2);
                        //QR
                        break;
                    case 3 :
                        listener.ItemGuid(3);
                        break;
                }
            }
        };

        public RallyRecyclerviewAdapter(Context context, List<Tour_Spot> list){
            this.context = context;
            this.list = list;
        }


        public void sortList(){
            list.sort((o1, o2) -> {
                /*if (o1.getTouristspot_createtime() > o2.getTouristspot_createtime()){

                }*/
                return 0;
            });
        }

        private int average(){
            int sum = 0;
            for (int i = 0; i < list.size(); i++){
                sum += list.get(i).getTouristspot_checkin_count();
            }

            return sum / list.size();
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

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.stepRallyBg.setOnClickListener(v -> {
                listener = (MainActivity) requireActivity();
                guidDialog = new GuidDialog(requireContext());
                guidDialog.show();
                guidDialog.setClickListener(itemOnClick);
            });

            /*long today = System.currentTimeMillis();
            long diff = today - list.get(position).getTouristspot_createtime();*/

            Log.d("DiffTime", String.valueOf((((System.currentTimeMillis() - list.get(position).getTouristspot_createtime()) / 1000 ) / (24 * 60 * 60))));
            Log.d("AVERAGE", String.valueOf(average()));


            holder.binding.location.setText(list.get(position).getLocation_name());

            if (list.get(position).getTouristspot_checkin_count() > average()) {
                //list 내 CheckCount 의 평균값을 구한 뒤 평균 이상이면 HOT 띄우기
                holder.binding.newsImv.setVisibility(View.VISIBLE);
                Glide.with(holder.binding.newsImv.getContext()).load(R.drawable.hot_icon).into(holder.binding.newsImv);
            }else if (((System.currentTimeMillis() - list.get(position).getTouristspot_createtime()) / 10000 ) / (24 * 60 * 60) < 8){
                //현재 시간과 관광지 등록시간이 7일 이하면 NEW 띄우기
                holder.binding.newsImv.setVisibility(View.VISIBLE);
                Glide.with(holder.binding.newsImv.getContext()).load(R.drawable.new_icon).into(holder.binding.newsImv);
            }else {
                holder.binding.newsImv.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return 4;
        }



        public class ViewHolder extends RecyclerView.ViewHolder{

            public StepRallyLocationItemBinding binding;

            public ViewHolder(@NonNull StepRallyLocationItemBinding binding) {
                super(binding.getRoot());

                this.binding = binding;
            }
        }
    }
}