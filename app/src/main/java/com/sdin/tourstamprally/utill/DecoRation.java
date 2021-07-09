package com.sdin.tourstamprally.utill;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DecoRation extends RecyclerView.ItemDecoration {

    private int size10;
    private int size5;

    private int spacing;
    private int topSpacing;

    public DecoRation(int spacing, int topSpacing) {
        this.spacing = spacing;
        this.topSpacing = topSpacing;
    }


    public DecoRation(Context context){
        size10 =dpToPx(context, 10);
        size5 = dpToPx(context, 5);
    }

    // dp -> pixel 단위로 변경
    private int dpToPx(Context context, int dp) {

        return (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int index = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        // Item 포지션
        int position = parent.getChildLayoutPosition(view);
        if (index == 0) {
            //좌측 Spacing 절반
            outRect.right = spacing/ 2;
        } else {
            //우측 Spacing 절반
            outRect.left = spacing/ 2;
        } // 상단 탑 Spacing 맨 위에 포지션 0, 1은 Spacing을 안 줍니다.
        if (position < 2) {
            outRect.top = 0;
        } else {
            outRect.top = topSpacing;
        }

    }
}
