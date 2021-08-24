package com.sdin.tourstamprally.adapter.swipe;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ToggleAnimation {

    public boolean toggleArrow(View view, boolean isExpanded){

        if (isExpanded){
            view.animate().setDuration(200).rotation(180f);
            return true;
        }else {

            view.animate().rotation(200).rotation(0f);
            return false;
        }

    }

    public void expand(View view){
        Animation animation = expandAction(view);
        view.startAnimation(animation);
    }

    private Animation expandAction(View view){
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int actualHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1f){
                    view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }else {
                    view.getLayoutParams().height = (int)(actualHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };

        animation.setDuration((long) (actualHeight / view.getContext().getResources().getDisplayMetrics().density));

        view.startAnimation(animation);

        return animation;

    }

    private void collapse(View view){
        int actualHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1f){
                    view.setVisibility(View.GONE);
                }else {
                    view.getLayoutParams().height =((int) (actualHeight - (actualHeight * interpolatedTime)));
                    view.requestLayout();
                }
            }
        };

        animation.setDuration((long) (actualHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }
}
