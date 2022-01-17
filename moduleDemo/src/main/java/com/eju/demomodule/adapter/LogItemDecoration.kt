package com.eju.demomodule.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eju.tools.dp

class LogItemDecoration:RecyclerView.ItemDecoration() {

    private val lineColor = Color.parseColor("#ff0000")
    private val lineWidth = 1.dp
    private val topLineHeight = 7.dp
    private val lineCircleInterval = 2.dp
    private val circleColor = Color.parseColor("#D9DBDF")
    private val circleRadius = 5.dp

    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    } }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = circleRadius.toInt()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
//            val position = parent.getChildAdapterPosition(child).let { if(it==RecyclerView.NO_POSITION) null else it }?:continue
            drawTopLine(parent, child, c)
            drawCircle(parent, child, c)
            drawBottomLine(parent, child, c)
        }

    }

    private fun drawTopLine(
        parent: RecyclerView,
        child: View,
        c: Canvas
    ) {
        paint.color = lineColor
        val left = parent.paddingLeft + circleRadius - lineWidth / 2
        val right = left + lineWidth
        val top = child.top.toFloat()
        val bottom = top + topLineHeight
        c.drawRect(left, top, right, bottom, paint)
    }

    private fun drawCircle(
        parent: RecyclerView,
        child: View,
        c: Canvas
    ) {
        paint.color = circleColor
        val circleX = parent.paddingLeft+circleRadius
        val circleY = child.top+topLineHeight+lineCircleInterval+circleRadius
        c.drawCircle(circleX,circleY,circleRadius,paint)
    }

    private fun drawBottomLine(
        parent: RecyclerView,
        child: View,
        c: Canvas
    ) {
        paint.color = lineColor
        val left = parent.paddingLeft + circleRadius - lineWidth / 2
        val right = left + lineWidth
        val top = child.top+topLineHeight+lineCircleInterval+circleRadius*2+lineCircleInterval
        val bottom = child.bottom.toFloat()
        c.drawRect(left, top, right, bottom, paint)
    }


}