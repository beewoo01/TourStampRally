package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentWriteReviewBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.VisitCountModel;
import com.sdin.tourstamprally.ui.activity.MainActivity;
import com.sdin.tourstamprally.utill.ItemOnClick;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class WriteReviewFragment extends BaseFragment {

    private FragmentWriteReviewBinding  binding;

    private String nameArgu = null;
    private int idxArgu = 0;
    private ItemOnClick itemOnClick;


    public static WriteReviewFragment newInstance(int spotIdx, String spotName) {

        Bundle args = new Bundle();

        WriteReviewFragment fragment = new WriteReviewFragment();
        args.putString("spotName", spotName);
        args.putInt("spotIdx", spotIdx);
        fragment.setArguments(args);
        return fragment;
    }

    public WriteReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameArgu = getArguments().getString("spotName");
            idxArgu = getArguments().getInt("spotIdx");
            itemOnClick = (MainActivity) requireActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_review, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.titleTxv.setText(nameArgu);

    }

    public void sendReview(){

        float review_score = binding.ratingbar.getRating();
        String review_contents = binding.reviewEdt.getText().toString();

        if (TextUtils.isEmpty(review_contents)){
            showToast("리뷰를 작성해 주세요");
            return;
        }


        apiService.insert_writeReview(Utils.User_Idx, idxArgu, review_score, review_contents).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if (response.isSuccessful()){
                    try {
                        int result = response.body();
                        if (result == 1) {
                            showToast("리뷰 등록에 성공하셨습니다.");
                            itemOnClick.onWriteReviewSuccess();
                        }else {
                            showToast("리뷰 등록에 실패하셨습니다.");
                        }
                    }catch (Exception e){
                        showToast("리뷰 등록에 실패하셨습니다.");
                        e.printStackTrace();
                    }


                }else {
                    showToast("리뷰 등록에 실패하셨습니다.");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
                showToast("리뷰 등록에 실패하셨습니다.");
            }
        });
    }

    private void showToast(String msg){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
