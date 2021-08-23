package com.sdin.tourstamprally.adapter.swipe;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.adapter.VisitReAdapter;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;

public class SwipeHelperCallback extends ItemTouchHelper.Callback {
    

    private int currentPosition = -1;
    private int previousPosition = -1;
    private float currentDx = 0f;
    private float clamp = 0f;

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, recyclerView.getLeft() | recyclerView.getRight());
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        currentDx = 0f;
        getDefaultUIUtil().clearView(getView(viewHolder));
        previousPosition = viewHolder.getAdapterPosition();
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder != null){
            currentPosition = viewHolder.getAdapterPosition();
            getDefaultUIUtil().onSelected(getView(viewHolder));
        }
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 10;
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        boolean isClamped = getTag(viewHolder);
        setTag(viewHolder, !isClamped && currentDx <= -clamp);
        return 2f;

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View view = getView(viewHolder);
            boolean isClamped = getTag(viewHolder);
            float x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive);

            currentDx = x;

            getDefaultUIUtil().onDraw(
                    c,
                    recyclerView,
                    view,
                    x,
                    dY,
                    actionState,
                    isCurrentlyActive
            );
        }
    }

    private float clampViewPositionHorizontal(View view, float dX, boolean isClamped, boolean isCurrentlyActive){
        float min = ((float) -view.getWidth()) / 2;
        float max = 150f;

        float x;
        if (isClamped){
            if (isCurrentlyActive) {
               x = dX - clamp;
            }else {
               x = -clamp;
            }
        }else {
            x = dX;
        }
        return Math.min(Math.max(min, x), max);
    }



    private void setTag(RecyclerView.ViewHolder viewHolder, boolean isClamped){
        viewHolder.itemView.setTag(isClamped);
    }

    private boolean getTag(RecyclerView.ViewHolder viewHolder){
        if (viewHolder.itemView.getTag() instanceof Boolean){
            return (boolean) viewHolder.itemView.getTag();
        }else {
            return false;
        }

        //return ((boolean)viewHolder.itemView.getTag())? (boolean) viewHolder.itemView.getTag() : false;
    }

    private View getView(RecyclerView.ViewHolder viewHolder){
        return ((VisitReAdapter.ViewHolder) viewHolder).itemView.findViewById(R.id.swipe_view);
    }

    public void setClamp(float clamp){
        this.clamp = clamp;
    }

    public void removePreviousClamp(RecyclerView recyclerView){
        if (currentPosition == previousPosition){
            return;
        }
        if (previousPosition != -1){
            if (recyclerView.findViewHolderForAdapterPosition(previousPosition) != null){
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(previousPosition);
                getView(viewHolder).setTranslationX(0f);
                setTag(viewHolder , false);
                previousPosition = -1;
            }else {
                return;
            }

        }
    }


    /*private boolean getTag(RecyclerView.ViewHolder viewHolder){
        if (viewHolder.itemView.getTag() instanceof Boolean){

        }
        return viewHolder.itemView.getTag() instanceof Boolean?: false;
    }*/
}
