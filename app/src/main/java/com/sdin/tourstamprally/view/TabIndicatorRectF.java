package com.sdin.tourstamprally.view;

import android.graphics.RectF;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.lang.reflect.Field;

public class TabIndicatorRectF extends RectF implements Serializable {
    private RectF temp = new RectF();
    private IndicatorBoundsModifier indicatorBoundsModifier;

    public TabIndicatorRectF(IndicatorBoundsModifier indicatorBoundsModifier) {
        this.indicatorBoundsModifier = indicatorBoundsModifier;
    }

    private interface IndicatorBoundsModifier {
        void modify(RectF bounds);
    }

    @Override
    public void set(float left, float top, float right, float bottom) {
        temp.set(left, top, right, bottom);
        indicatorBoundsModifier.modify(temp);

        super.set(temp);
    }

    public void replaceBoundsRectF(TabLayout tabLayout) {
        try {
            Field field = TabLayout.class.getDeclaredField("tabViewContentBounds");
            field.setAccessible(true);
            field.set(tabLayout, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static class FixedWidthModifier implements IndicatorBoundsModifier {
        private float halfWidth;

        public FixedWidthModifier(float width) {
            this.halfWidth = Math.abs(width) / 2;
        }

        @Override
        public void modify(RectF bounds) {
            float centerX = bounds.centerX();
            bounds.left = centerX - halfWidth;
            bounds.right = centerX + halfWidth;
        }
    }

}