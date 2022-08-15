package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentNoticeBinding;
import com.sdin.tourstamprally.databinding.NoticeItemBinding;
import com.sdin.tourstamprally.model.Notice;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeFragment extends BaseFragment {

    private FragmentNoticeBinding binding;
    private List<Notice> list;
    private String[] strings;
    private String selectedSpinnerItem;
    private NoticeAdapter noticeAdapter;


    public NoticeFragment() {

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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice , container, false);
        initView();
        return binding.getRoot();
    }

    private void initView(){
        getData();
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.notice_arry, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //binding.spinner.setSele
        //selectedSpinnerItem = binding.spinner.getSelectedItem().toString();
        binding.spinner.setAdapter(spinnerAdapter);
        binding.spinner.setSelection(0);
        strings = getResources().getStringArray(R.array.notice_arry);
        //selectedSpinnerItem = binding.spinner.getSelectedItem().toString();
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedSpinnerItem = strings[position];
                //Log.wtf("onItemSelected", String.valueOf(position));
                if (noticeAdapter != null){
                    // 0 - 공지 1 - 이벤트
                    // 0 - 전체 1 - 공지 - 2 이벤트
                    ArrayList<Notice> arrayList = new ArrayList<>();
                    if (position == 0){
                        arrayList = new ArrayList<>(list);

                    }else if (position == 1){
                        for (int i = 0; i < list.size(); i++){
                            if (list.get(i).getNotice_type() == 0){
                                arrayList.add(list.get(i));
                            }
                        }
                    }else if (position == 2){
                        for (int i = 0; i < list.size(); i++){
                            if (list.get(i).getNotice_type() == 1){
                                arrayList.add(list.get(i));
                            }
                        }
                    }

                    noticeAdapter.setNoticeList(arrayList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getData(){
        apiService.getNotice().enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(@NotNull Call<List<Notice>> call, @NotNull Response<List<Notice>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    ArrayList<Notice> arrayList = new ArrayList<>(list);
                    noticeAdapter = new NoticeAdapter(arrayList);
                    binding.noticeRe.setAdapter(noticeAdapter);
                    binding.noticeRe.setLayoutManager(new LinearLayoutManager(requireContext()));
                    Log.wtf("onResponse", "Success");
                }else {
                    Log.wtf("onResponse", "notSuccess");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Notice>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

        private ArrayList<Notice> arrayList;
        private int type = 0;

        /*private SparseBooleanArray selectedItems = new SparseBooleanArray();

        private int prePosition = -1;*/

        public NoticeAdapter(ArrayList<Notice> arrayList) {
            this.arrayList = arrayList;

        }

        public void setNoticeList(ArrayList<Notice> arrayList){
            this.arrayList = arrayList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    NoticeItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (type == 0){
                holder.itemView.setVisibility(View.VISIBLE);
            }else {
                holder.itemView.setVisibility(list.get(position).getNotice_type() == type ? View.VISIBLE : View.GONE);
            }

            // 0 - 공지 1 - 이벤트
            if (arrayList.get(position).getNotice_type() == 0){
                holder.binding.noticeKindOfTxv.setText("공지사항");
                holder.binding.noticeKindOfTxv.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_category_selected));
                holder.binding.noticeKindOfTxv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            }
            else{
                holder.binding.noticeKindOfTxv.setText("이벤트");
                holder.binding.noticeKindOfTxv.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_category));
                holder.binding.noticeKindOfTxv.setTextColor(ContextCompat.getColor(requireContext(), R.color.mainColor));
            }

            String oriDate = arrayList.get(position).getNotice_updatetime();
            Log.wtf("oridata", oriDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(oriDate);
                String viewDate = format.format(date);
                holder.binding.dateTxv.setText(viewDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.binding.titleTxv.setText(arrayList.get(position).getNotice_title());

            holder.binding.arrowImv.setOnClickListener( v -> {
                holder.binding.contentTxv.setVisibility(View.VISIBLE);
            });
            holder.binding.newIcImv.setVisibility(View.INVISIBLE);
            holder.binding.contentTxv.setText(arrayList.get(position).getNotice_content());

            holder.binding.arrowImv.setOnClickListener( v -> {
                if (holder.binding.contentTxv.getVisibility() == View.VISIBLE ){
                    holder.binding.contentTxv.setVisibility(View.GONE);
                    Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down_24)).into(holder.binding.arrowImv);
                    //holder.binding.arrowImv.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down_24));
                }else {
                    holder.binding.contentTxv.setVisibility(View.VISIBLE);
                    Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up_24)).into(holder.binding.arrowImv);
                }

            });
            holder.itemView.setOnClickListener( v -> {
                if (holder.binding.contentTxv.getVisibility() == View.VISIBLE ){
                    holder.binding.contentTxv.setVisibility(View.GONE);
                    Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down_24)).into(holder.binding.arrowImv);
                }else {
                    holder.binding.contentTxv.setVisibility(View.VISIBLE);
                    Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up_24)).into(holder.binding.arrowImv);
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private NoticeItemBinding binding;
            public ViewHolder(@NonNull NoticeItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;


            }

            /*void onBind(int position){
                changeVisibility(selectedItems.get(position));
            }*/
        }

        /*private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(animation -> {
                // value는 height 값
                int value = (int) animation.getAnimatedValue();
                // imageView의 높이 변경
                imageView2.getLayoutParams().height = value;
                imageView2.requestLayout();
                // imageView가 실제로 사라지게하는 부분
                imageView2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            });
            // Animation start
            va.start();
        }*/
    }




}