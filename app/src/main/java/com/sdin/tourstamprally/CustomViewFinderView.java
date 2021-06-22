package com.sdin.tourstamprally;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ViewfinderView;


public class CustomViewFinderView extends ViewfinderView {

    private Context context;

    public CustomViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //refreshSizes();
        if (framingRect == null || previewSize == null) {
            return;
        }
        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect frame = framingRect;
        borderPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        //inside onDraw
        int distance = (frame.bottom - frame.top) / 8;
        int thickness = 15;

        //top left corner
        canvas.drawRect(frame.left - thickness, frame.top - thickness, distance + frame.left, frame.top, borderPaint);
        canvas.drawRect(frame.left - thickness, frame.top, frame.left, distance + frame.top, borderPaint);

        //top right corner
        canvas.drawRect(frame.right - distance, frame.top - thickness, frame.right + thickness, frame.top, borderPaint);
        canvas.drawRect(frame.right, frame.top, frame.right + thickness, distance + frame.top, borderPaint);

        //bottom left corner
        canvas.drawRect(frame.left - thickness, frame.bottom, distance + frame.left, frame.bottom + thickness, borderPaint);
        canvas.drawRect(frame.left - thickness, frame.bottom - distance, frame.left, frame.bottom, borderPaint);

        //bottom right corner
        canvas.drawRect(frame.right - distance, frame.bottom, frame.right + thickness, frame.bottom + thickness, borderPaint);
        canvas.drawRect(frame.right, frame.bottom - distance, frame.right + thickness, frame.bottom, borderPaint);

    }
}
