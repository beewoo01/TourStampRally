package com.sdin.tourstamprally.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.databinding.FragmentWriteReviewBinding;
import com.sdin.tourstamprally.model.ReviewWriter;
import com.sdin.tourstamprally.utill.listener.ItemOnClick;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReviewFragment extends BaseFragment {

    private FragmentWriteReviewBinding binding;

    private String nameArgu = null;
    private int idxArgu = 0;
    private ItemOnClick itemOnClick;
    private float review_score = 0f;
    private String review_contents = "";
    private boolean isFirst = false;
    private int review_idx;
    private ReviewWriter reviewWriter;

    public static WriteReviewFragment newInstance(ReviewWriter reviewWriter) {
        Bundle args = new Bundle();

        WriteReviewFragment fragment = new WriteReviewFragment();
        args.putSerializable("model", reviewWriter);

        fragment.setArguments(args);
        return fragment;
    }

    public WriteReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reviewWriter = (ReviewWriter) getArguments().getSerializable("model");
            nameArgu = reviewWriter.getSpotName();
            idxArgu = reviewWriter.getSpotIdx();
            isFirst = reviewWriter.isFirst();
            if (!isFirst) {
                review_idx = reviewWriter.getReview_idx();
                review_score = reviewWriter.getReview_score();
                review_contents = reviewWriter.getReview_contents();

            }

            //itemOnClick = (MainActivity) requireActivity();

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
        if (!isFirst) {
            binding.reviewEdt.setText(review_contents);
            binding.ratingbar.setRating(review_score);
        }


    }

    public void sendReview() {
        float review_score = binding.ratingbar.getRating();
        String review_contents = binding.reviewEdt.getText().toString();
        if (isFirst) {
            insertReview(review_score, review_contents);
        } else {
            updateReview(review_score, review_contents);
        }

    }

    private void updateReview(float review_score, String review_contents) {
        apiService.updateReview(review_score, review_contents, review_idx)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                        if (integer > 0) {
                            Toast.makeText(requireContext(), "리뷰 업데이트에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            itemOnClick.onWriteReviewSuccess();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(requireContext(), "리뷰 업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

    }

    private void insertReview(float review_score, String review_contents) {


        if (TextUtils.isEmpty(review_contents)) {
            showToast("리뷰를 작성해 주세요");
            return;
        }
        apiService.insert_writeReview(Utils.User_Idx, idxArgu, review_score, review_contents).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {

                if (response.isSuccessful()) {
                    try {
                        int result = response.body();
                        if (result == 1) {
                            showToast("리뷰 등록에 성공하엿습니다.");
                            itemOnClick.onWriteReviewSuccess();
                        } else {
                            showToast("리뷰 등록에 실패하였습니다.");
                        }
                    } catch (Exception e) {
                        showToast("리뷰 등록에 실패하였습니다.");
                        e.printStackTrace();
                    }


                } else {
                    showToast("리뷰 등록에 실패하였습니다.");
                }
            }

            @Override
            public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                t.printStackTrace();
                showToast("리뷰 등록에 실패하였습니다.");
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
