package com.sdin.tourstamprally.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
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
import com.sdin.tourstamprally.databinding.FragmentMainBinding;
import com.sdin.tourstamprally.databinding.StepRallyLocationItemBinding;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.ui.dialog.GuidDialog;
import com.sdin.tourstamprally.utill.ItemOnClick;

public class MainFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentMainBinding binding;

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
        RallyRecyclerviewAdapter adapter = new RallyRecyclerviewAdapter(getContext());
        binding.rallyRecyclerview.setAdapter(adapter);
        binding.rallyRecyclerview.addItemDecoration(new RallyRecyclerviewAdapterDeco(2, 50, true));
        return binding.getRoot();
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


    private class RallyRecyclerviewAdapter extends RecyclerView.Adapter<RallyRecyclerviewAdapter.ViewHolder> {

        private GuidDialog guidDialog;
        //private DefaultDialog dialog;
        private Context context;
        private ItemOnClick listener;

        private ItemOnClick itemOnClick = new ItemOnClick() {
            @Override
            public void onClick(int position) {

            }

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

        public RallyRecyclerviewAdapter(Context context){
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    StepRallyLocationItemBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.stepRallyBg.setOnClickListener(v -> {
                listener = (MainActivity) context;
                guidDialog = new GuidDialog(context);
                guidDialog.show();
                guidDialog.setClickListener(itemOnClick);
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }



        public class ViewHolder extends RecyclerView.ViewHolder{

            StepRallyLocationItemBinding binding;

            public ViewHolder(@NonNull StepRallyLocationItemBinding binding) {
                super(binding.getRoot());

                this.binding = binding;
            }
        }
    }
}