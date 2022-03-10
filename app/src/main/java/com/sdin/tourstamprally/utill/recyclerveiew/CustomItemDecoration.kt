package com.sdin.tourstamprally.utill.recyclerveiew

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(
    val topPadding: Int?,
    val bottomPadding: Int?,
    val leftPadding: Int?,
    val rightPadding: Int?
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.apply {
            if (topPadding != null) {
                top = topPadding
            }

            if (bottomPadding != null) {
                bottom = bottomPadding
            }

            if (leftPadding != null) {
                left = leftPadding
            }

            if (rightPadding != null) {
                right = rightPadding
            }
        }
    }
}