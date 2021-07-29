package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.internal.$Gson$Types;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.FragmentNoticeBinding;
import com.sdin.tourstamprally.databinding.NoticeItemBinding;
import com.sdin.tourstamprally.model.Notice;

import java.util.ArrayList;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpinnerItem = strings[i];
                if (noticeAdapter != null){
                    noticeAdapter.setNoticeList(i);
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
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    ArrayList arrayList = new ArrayList(list);
                    noticeAdapter = new NoticeAdapter(arrayList);
                    binding.noticeRe.setAdapter(noticeAdapter);
                    Log.wtf("onResponse", "Success");
                }else {
                    Log.wtf("onResponse", "notSuccess");
                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
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

        public void setNoticeList(int type){
            this.type = type;
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

            if (arrayList.get(position).getNotice_type() == 1){
                holder.binding.noticeKindOfTxv.setText("공지사항");
                holder.binding.noticeKindOfTxv.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_category_selected));
                holder.binding.noticeKindOfTxv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            }
            else{
                holder.binding.noticeKindOfTxv.setText("이벤트");
                holder.binding.noticeKindOfTxv.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_category));
                holder.binding.noticeKindOfTxv.setTextColor(ContextCompat.getColor(requireContext(), R.color.mainColor));
            }
            holder.binding.dateTxv.setText(arrayList.get(position).getNotice_updatetime());
            holder.binding.titleTxv.setText(arrayList.get(position).getNotice_title());

            holder.binding.arrowImv.setOnClickListener( v -> {
                holder.binding.contentTxv.setVisibility(View.VISIBLE);
            });
            holder.binding.newIcImv.setVisibility(View.INVISIBLE);
            holder.binding.contentTxv.setText(arrayList.get(position).getNotice_content());
            holder.itemView.setOnClickListener( v -> {
                holder.binding.contentTxv.setVisibility(holder.binding.contentTxv.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
               /* if (selectedItems.get(position)){
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(position);
                }else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(position, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;*/
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