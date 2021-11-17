package com.sdin.tourstamprally.adapter.swipe

import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.VisitReAdapter
import kotlin.math.max
import kotlin.math.min

class SwipeHelperCallback : ItemTouchHelper.Callback(){

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f


    override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.absoluteAdapterPosition
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.absoluteAdapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        // 현재 View가 고정되어있지 않고 사용자가 -clamp 이상 swipe시 isClamped true로 변경 아닐시 false로 변경


        setTag(viewHolder, !isClamped && currentDx >= -clamp) // 우측 스와이프 고정시켜주기


        // 현재 View가 고정되어있지 않고 사용자가 -clamp 이상 swipe시 isClamped true로 변경 아닐시 false로 변경
        //setTag(viewHolder, !isClamped && currentDx <= -clamp) // 좌측 스와이프 고정시켜주기
        return 2f
    }

    override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val x =  clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)

            (viewHolder as VisitReAdapter.SwipeViewHolder).binding?.let {
                if (isClamped) {
                    it.titleTxv.setTextColor(ContextCompat.getColor(viewHolder.binding.titleTxv.context, R.color.mainColor))
                    it.backgroundContainer.setBackgroundColor(ContextCompat.getColor(it.backgroundContainer.context, R.color.gray_e))
                }
                else {
                    it.titleTxv.setTextColor(ContextCompat.getColor(viewHolder.binding.titleTxv.context, R.color.black))
                    it.backgroundContainer.setBackgroundColor(ContextCompat.getColor(it.backgroundContainer.context, R.color.white))
                }

            }

            currentDx = x
            getDefaultUIUtil().onDraw(
                    c,
                    recyclerView,
                    view,
                    x,
                    dY,
                    actionState,
                    isCurrentlyActive
            )



        }
    }

    private fun clampViewPositionHorizontal(
            view: View,
            dX: Float,
            isClamped: Boolean,
            isCurrentlyActive: Boolean
    ) : Float {

        val min: Float = 0f
        // LTFT 방향으로 swipe 막기
        val max: Float = view.width.toFloat()/2
        // View의 가로 길이의 절반까지만 swipe 되도록


        /*val min: Float = -view.width.toFloat()/2
        // RIGHT 방향으로 swipe 막기
        val max: Float = 0f*/

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(min, x), max)
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        // isClamped를 view의 tag로 관리
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
        // isClamped를 view의 tag로 관리
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder) : View {
        return (viewHolder as VisitReAdapter.SwipeViewHolder).binding.swipeView
    }

    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition)
            return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
            (viewHolder as VisitReAdapter.SwipeViewHolder).binding?.let {
                it.titleTxv.setTextColor(ContextCompat.getColor(viewHolder.binding.titleTxv.context, R.color.Black))
                it.backgroundContainer.setBackgroundColor(ContextCompat.getColor(it.backgroundContainer.context, R.color.white))
            }
        }
    }
}