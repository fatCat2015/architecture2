package com.eju.tools.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.lang.IllegalArgumentException
import kotlin.math.roundToInt

class DividerItemDecoration(private val divider:Drawable,private val showDividers:Int = SHOW_NONE):RecyclerView.ItemDecoration() {

    constructor(context: Context,@DrawableRes drawableRes: Int,showDividers:Int = SHOW_NONE):this(ContextCompat.getDrawable(context,drawableRes)?: throw IllegalArgumentException("Drawable cannot be null."),showDividers)

    constructor(dividerWidth:Int, dividerHeight:Int, @ColorInt dividerColor:Int = Color.TRANSPARENT, showDividers:Int = SHOW_NONE):this(GradientDrawable().apply {
        this.setColor(dividerColor)
        this.setSize(dividerWidth,dividerHeight)
    },showDividers)

    constructor(dividerSize:Int,@ColorInt dividerColor:Int = Color.TRANSPARENT,showDividers:Int = SHOW_NONE):this(dividerSize,dividerSize,dividerColor,showDividers)

    private val dividerWidth :Int = divider.intrinsicWidth

    private val dividerHeight :Int = divider.intrinsicHeight

    private val showBeginning :Boolean get() = showDividers and SHOW_BEGINNING == SHOW_BEGINNING

    private val showEnd :Boolean get() = showDividers and SHOW_END == SHOW_END

    private val bounds :Rect by lazy {
        Rect()
    }

    private val dividerBounds :Rect by lazy {
        Rect()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager?:return
        val itemCount = parent.adapter?.itemCount?.let { if(it==0) null else it }?:return
        val position = parent.getChildAdapterPosition(view).let { if(it==RecyclerView.NO_POSITION) null else it }?:return
        when(layoutManager){
            is GridLayoutManager->{
                setItemOffsetWhenGridLayoutManager(
                    layoutManager,
                    view,
                    outRect,
                    position,
                    itemCount
                )
            }
            is LinearLayoutManager->{
                setItemOffsetsWhenLinearLayoutManager(position,itemCount,layoutManager.orientation,outRect)
            }
        }
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager?:return
        val itemCount = parent.adapter?.itemCount?.let { if(it==0) null else it }?:return
        when(layoutManager){
            is GridLayoutManager->{
                //do not draw
//                drawDividerWhenGridLayoutManager(itemCount,layoutManager.spanCount,layoutManager.orientation,parent,c)
            }
            is LinearLayoutManager->{
                drawDividerWhenLinearLayoutManager(itemCount,layoutManager.orientation,parent,c)
            }
        }
    }

    private fun setItemOffsetsWhenLinearLayoutManager(
        position: Int,
        itemCount: Int,
        orientation:Int,
        outRect: Rect
    ) {
        val beginningItemOffsetSetter = if(orientation == LinearLayoutManager.VERTICAL){
            {
                outRect.top = dividerHeight
            }
        }else{
            {
                outRect.left = dividerWidth
            }
        }
        val endItemOffsetSetter = if(orientation == LinearLayoutManager.VERTICAL){
            {
                outRect.bottom = dividerHeight
            }
        }else{
            {
                outRect.right = dividerWidth
            }
        }
        if(isFirstRowOrColumn(position,1 ) && showBeginning){
            beginningItemOffsetSetter()
        }
        if(isLastRowOrColumn(position,1,itemCount) && showEnd){
            endItemOffsetSetter()
        }
        if(!isLastRowOrColumn(position,1,itemCount)){
            endItemOffsetSetter()
        }
    }

    private fun setItemOffsetWhenGridLayoutManager(
        layoutManager: GridLayoutManager,
        view: View,
        outRect: Rect,
        position: Int,
        itemCount: Int
    ) {
        val orientation = layoutManager.orientation
        val spaceItemOffsetSetter = if (orientation == LinearLayoutManager.VERTICAL) {
            {
                val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
                val spanCount = layoutManager.spanCount
                val averageSpace = (spanCount - 1) * dividerWidth / spanCount

                outRect.left = spanIndex * dividerWidth - spanIndex * averageSpace
                outRect.right = averageSpace - outRect.left
            }
        } else {
            {
                val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
                val spanCount = layoutManager.spanCount
                val averageSpace = (spanCount - 1) * dividerHeight / spanCount
                outRect.top = spanIndex * dividerHeight - spanIndex * averageSpace
                outRect.bottom = averageSpace - outRect.top
            }
        }
        val beginningItemOffsetSetter = if (orientation == LinearLayoutManager.VERTICAL) {
            {
                outRect.top = dividerHeight
            }
        } else {
            {
                outRect.left = dividerWidth
            }
        }
        val endItemOffsetSetter = if (orientation == LinearLayoutManager.VERTICAL) {
            {
                outRect.bottom = dividerHeight
            }
        } else {
            {
                outRect.right = dividerWidth
            }
        }

        val spanCount = layoutManager.spanCount
        spaceItemOffsetSetter()
        if (isFirstRowOrColumn(position, spanCount) && showBeginning) {
            beginningItemOffsetSetter()
        }
        if (isLastRowOrColumn(position, spanCount, itemCount) && showEnd) {
            endItemOffsetSetter()
        }
        if (!isLastRowOrColumn(position, spanCount, itemCount)) {
            endItemOffsetSetter()
        }
    }

    private fun drawDividerWhenLinearLayoutManager( itemCount: Int,orientation:Int,parent: RecyclerView, c: Canvas) {
        c.save()
        if(orientation == LinearLayoutManager.VERTICAL){
            dividerBounds.left = if(parent.clipToPadding) parent.paddingLeft else 0
            dividerBounds.right = if(parent.clipToPadding) parent.width-parent.paddingRight else parent.width
            if(parent.clipToPadding){
                c.clipRect(dividerBounds.left,parent.paddingTop,dividerBounds.right,parent.height-parent.paddingBottom)
            }
        }else{
            dividerBounds.top = if(parent.clipToPadding) parent.paddingTop else 0
            dividerBounds.bottom = if(parent.clipToPadding) parent.height-parent.paddingBottom else parent.height
            if(parent.clipToPadding){
                c.clipRect(parent.paddingLeft,dividerBounds.top,parent.width-parent.paddingRight,dividerBounds.bottom)
            }
        }
        val dividerBoundsBeginningSetter = if(orientation == LinearLayoutManager.VERTICAL){
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.top = bounds.top + child.translationY.roundToInt()
                dividerBounds.bottom = dividerBounds.top + dividerHeight
            }
        }else{
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.left = bounds.left + child.translationX.roundToInt()
                dividerBounds.right = dividerBounds.left + dividerWidth
            }
        }
        val dividerBoundsEndSetter = if(orientation == LinearLayoutManager.VERTICAL){
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.bottom = bounds.bottom + child.translationY.roundToInt()
                dividerBounds.top = dividerBounds.bottom - dividerHeight
            }
        }else{
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.right = bounds.right + child.translationX.roundToInt()
                dividerBounds.left = dividerBounds.right - dividerWidth
            }
        }

        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            val position = parent.getChildAdapterPosition(child).let { if(it==RecyclerView.NO_POSITION) null else it }?:continue
            if(isFirstRowOrColumn(position,1 ) && showBeginning){
                dividerBoundsBeginningSetter(child,parent)
                drawDivider(c)
            }
            if(isLastRowOrColumn(position,1,itemCount) && showEnd){
                dividerBoundsEndSetter(child,parent)
                drawDivider(c)
            }
            if(!isLastRowOrColumn(position,1,itemCount)){
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBoundsEndSetter(child,parent)
                drawDivider(c)
            }
        }
        c.restore()
    }

    private fun drawDividerWhenGridLayoutManager( itemCount: Int, spanCount: Int,orientation:Int,parent: RecyclerView, c: Canvas) {

        val dividerBoundsSpaceSetter = if (orientation == LinearLayoutManager.VERTICAL) {
            {child:View,parent:RecyclerView ->
                dividerBounds.left = child.right
                dividerBounds.right = dividerBounds.left+dividerWidth
                dividerBounds.top = child.top
                dividerBounds.bottom = child.bottom
            }
        } else {
            {child:View,parent:RecyclerView ->
                dividerBounds.left = child.left
                dividerBounds.right = child.right
                dividerBounds.top = child.bottom
                dividerBounds.bottom = dividerBounds.top+dividerHeight
            }
        }
        val dividerBoundsBeginningSetter = if(orientation == LinearLayoutManager.VERTICAL){
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.left = child.left
                dividerBounds.right = child.right
                dividerBounds.top = bounds.top + child.translationY.roundToInt()
                dividerBounds.bottom = dividerBounds.top + dividerHeight
            }
        }else{
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.left = bounds.left + child.translationX.roundToInt()
                dividerBounds.right = dividerBounds.left + dividerWidth
                dividerBounds.top = child.top
                dividerBounds.bottom = child.bottom
            }
        }
        val dividerBoundsEndSetter = if(orientation == LinearLayoutManager.VERTICAL){
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.left = child.left
                dividerBounds.right = child.right
                dividerBounds.bottom = bounds.bottom + child.translationY.roundToInt()
                dividerBounds.top = dividerBounds.bottom - dividerHeight
            }
        }else{
            {child:View,parent:RecyclerView ->
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBounds.right = bounds.right + child.translationX.roundToInt()
                dividerBounds.left = dividerBounds.right - dividerWidth
                dividerBounds.top = child.top
                dividerBounds.bottom = child.bottom
            }
        }

        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            val position = parent.getChildAdapterPosition(child).let { if(it==RecyclerView.NO_POSITION) null else it }?:continue
            if(isFirstRowOrColumn(position,spanCount ) && showBeginning){
                dividerBoundsBeginningSetter(child,parent)
                drawDivider(c)
            }
            if(isLastRowOrColumn(position,spanCount,itemCount) && showEnd){
                dividerBoundsEndSetter(child,parent)
                drawDivider(c)
            }
            if(!isLastRowOrColumn(position,spanCount,itemCount)){
                parent.getDecoratedBoundsWithMargins(child,bounds)
                dividerBoundsEndSetter(child,parent)
                drawDivider(c)
            }
            val spanIndex = (child.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            if(spanIndex!=spanCount-1){
                dividerBoundsSpaceSetter(child,parent)
                drawDivider(c)
            }
        }
    }

    private fun drawDivider(canvas: Canvas){
        divider.bounds = dividerBounds
        divider.draw(canvas)
    }


    private fun isOnlyOneRowOrColumn(spanCount:Int,itemCount:Int):Boolean{
        return itemCount<=spanCount
    }

    private fun isLastRowOrColumn(position:Int,spanCount:Int,itemCount:Int): Boolean {
        val tempValue = itemCount % spanCount
        val rowCount = itemCount / spanCount
        return if (tempValue == 0) position >= (rowCount - 1) * spanCount else position >= rowCount * spanCount
    }

    private fun isFirstRowOrColumn(position:Int,spanCount:Int): Boolean {
        return position < spanCount
    }

    companion object{
        const val SHOW_NONE = 0b0000_0000
        const val SHOW_BEGINNING = 0b0000_0001
        const val SHOW_END = 0b0000_0010
    }

}
