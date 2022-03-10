package com.sdin.tourstamprally.utill

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class CustommDecoration(
    val height: Float,
    val startPadding: Float,
    val endPadding: Float,
    val color: Int
) : RecyclerView.ItemDecoration() {
    private val paint = Paint()

    init {
        paint.color = color
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val start = parent.paddingStart + startPadding
        val end = parent.paddingEnd + endPadding

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = (child.bottom + params.bottomMargin).toFloat()
            val bottom = top + height

            c.drawRect(start, top, end, bottom, paint)

        }


    }

}