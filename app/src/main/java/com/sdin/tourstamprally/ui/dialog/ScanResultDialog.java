package com.sdin.tourstamprally.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.utill.DialogListener;
import com.sdin.tourstamprally.utill.ItemOnClick;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanResultDialog extends BaseDialog {

    private final Context context;

    // 0 -> 실패 , 1 -> 성공  2-> 이미 방문
    private final int isSucess;
    private final String title;
    private final String msg;
    private ItemOnClick itemOnClick;
    private int touristhistory_touristspotpoint_idx;
    private DialogListener dialogListener = null;

    public ScanResultDialog(@NonNull Context context, int isSucess, String title, String msg) {
        //성공
        super(context);
        this.context = context;
        this.isSucess = isSucess;
        this.title = title;
        this.msg = msg;
    }

    public ScanResultDialog(@NonNull Context context, int isSucess, String title, String msg, int touristhistory_touristspotpoint_idx) {
        //이미 완료
        super(context);
        this.context = context;
        this.isSucess = isSucess;
        this.title = title;
        this.msg = msg;
        this.itemOnClick = (ItemOnClick) context;
        this.touristhistory_touristspotpoint_idx = touristhistory_touristspotpoint_idx;
    }

    public ScanResultDialog(@NonNull Context context, int isSucess, String title, int touristhistory_touristspotpoint_idx, String msg) {
        //실패
        super(context);
        this.context = context;
        this.isSucess = isSucess;
        this.title = title;
        this.itemOnClick = (ItemOnClick) context;
        this.touristhistory_touristspotpoint_idx = touristhistory_touristspotpoint_idx;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scan_result_dialog);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        initView();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        ConstraintLayout constraintLayout = findViewById(R.id.backgrount_layout);
        Button resulButton = findViewById(R.id.guid_btn);
        TextView inner_result = findViewById(R.id.inner_result);
        TextView scan_result_txv = findViewById(R.id.scan_result_txv);

        findViewById(R.id.close_btn).setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onDissMiss();
            }
            dismiss();
        });

        if (isSucess == 1) {

            setBackgrountImg(R.drawable.nfc_tag_ok_bg, constraintLayout);

            resulButton.setBackground(ContextCompat.getDrawable(context, R.drawable.scan_button_bg));
            resulButton.setTextColor(ContextCompat.getColor(context, R.color.scan_dialog_text_color));
            resulButton.setText("확인하러가기 >");
            resulButton.setOnClickListener(v -> {
                // TODO: 8/10/21 선택 랠리맵으로 이동
                getData();

            });

            Glide.with(context).load(R.drawable.nfc_tag_ok_stamp).into((ImageView) findViewById(R.id.title_imv));
            Glide.with(context).load(R.drawable.mainlogo).into((ImageView) findViewById(R.id.logo));


            scan_result_txv.setText(title);
            scan_result_txv.setTextColor(ContextCompat.getColor(context, R.color.white));
            //((TextView)findViewById(R.id.inner_result)).setText("스탬프 랠리 획득!");
            inner_result.setText(msg);
            inner_result.setTextSize(25);
            inner_result.setTextColor(ContextCompat.getColor(context, R.color.scan_dialog_text_color));

        } else if (isSucess == 0){

            setBackgrountImg(R.drawable.nfc_tag_fail_bg, constraintLayout);

            resulButton.setBackground(ContextCompat.getDrawable(context, R.drawable.scan_fail_button_bg));
            resulButton.setTextColor(ContextCompat.getColor(context, R.color.white));
            resulButton.setText("재시도하기 >");
            resulButton.setOnClickListener(v -> {
                if (dialogListener != null) {
                    dialogListener.onDissMiss();
                }
                dismiss();
            });

            Glide.with(context).load(R.drawable.thumb_down).into((ImageView) findViewById(R.id.title_imv));
            Glide.with(context).load(R.drawable.main_logo_gray).into((ImageView) findViewById(R.id.logo));


            scan_result_txv.setText(title /*+ (scanner.equals("NFC") ? " 태깅" : "스캔") + " 실패"*/);
            scan_result_txv.setTextColor(ContextCompat.getColor(context, R.color.scan_fail_btn_Color));
            //((TextView)findViewById(R.id.inner_result)).setText(scanner + " 확인 하신 후 \n재시도 해주세요.");
            inner_result.setText(msg);
            inner_result.setTextSize(15);
            inner_result.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else if (isSucess == 2){

            // 이미 방문

            setBackgrountImg(R.drawable.tagging_already, constraintLayout);

            resulButton.setBackground(ContextCompat.getDrawable(context, R.drawable.scan_button_bg));
            resulButton.setTextColor(ContextCompat.getColor(context, R.color.scan_Al_text_color));
            resulButton.setText("확인하러가기 >");
            resulButton.setOnClickListener(v -> {
                // TODO: 8/10/21 선택 랠리맵으로 이동
                getData();

            });

            Glide.with(context).load(R.drawable.mainlogo).into((ImageView) findViewById(R.id.logo));

            scan_result_txv.setText(title);//스탬프확인
            scan_result_txv.setTextColor(ContextCompat.getColor(context, R.color.white));
            scan_result_txv.setTypeface(inner_result.getTypeface(), Typeface.BOLD);
            inner_result.setText(msg);//이미 획득 완료하신 스탬프 입니다.
            inner_result.setTextSize(15);

            inner_result.setTextColor(ContextCompat.getColor(context, R.color.Black));
        }
    }

    private void getData() {
        Log.wtf("getData!!", String.valueOf(touristhistory_touristspotpoint_idx));

        if (dialogListener != null) {
            dialogListener.onDissMiss();
        }

        apiService.select_success_data(String.valueOf(touristhistory_touristspotpoint_idx)).enqueue(new Callback<RallyMapDTO>() {
            @Override
            public void onResponse(@NotNull Call<RallyMapDTO> call, @NotNull Response<RallyMapDTO> response) {
                if (response.isSuccessful()) {
                    itemOnClick.ItemGuidForPoint(response.body());
                } else {
                    showToast();
                }

                dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<RallyMapDTO> call, @NotNull Throwable t) {
                showToast();
                dismiss();
                t.printStackTrace();

            }
        });

    }

    private void showToast() {
        Toast.makeText(context, "다시 시도 해주십시오.", Toast.LENGTH_SHORT).show();
    }

    private void setBackgrountImg(int drawable, View view) {

        Glide.with(context).asBitmap().load(drawable).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                BitmapDrawable drawable = new BitmapDrawable(context.getResources(), resource);
                view.setBackground(drawable);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
