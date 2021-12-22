package com.eju.baseadapter.section

import android.graphics.*
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.view.doOnLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CeilingItemDecoration:RecyclerView.ItemDecoration() {

    companion object{
        private const val createSectionBitmapAlways = false
    }

    private val sectionBitmapMap :HashMap<Int,Bitmap> by lazy {
        hashMapOf()
    }

    private val paint :Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun getItemOffsets(
        outRect: Rect,
        childView: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, childView, parent, state)

        val adapter = (parent.adapter as? SectionAdapter<*,*,*,*,*>) ?: return
        val position = parent.getChildAdapterPosition(childView)
        if(position != RecyclerView.NO_POSITION){
            if(adapter.isSectionItem(position)){
                if(createSectionBitmapAlways || !sectionBitmapMap.containsKey(position)){
                    childView.doOnLayout {
                        sectionBitmapMap[position] = view2Bitmap(childView)
                    }
                }
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val adapter = (parent.adapter as? SectionAdapter<*,*,*,*,*>) ?: return
        val layoutManager = (parent.layoutManager as? LinearLayoutManager) ?: return

        var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        if(firstVisiblePosition == RecyclerView.NO_POSITION){
            return
        }

        val sectionPosition = adapter.getSectionPositionFromItemPosition(firstVisiblePosition)
        if(sectionPosition == RecyclerView.NO_POSITION){
            return
        }

        sectionBitmapMap[sectionPosition]?.let {
            c.drawBitmap(it,null,Rect(
                parent.paddingLeft,
                parent.paddingTop,
                parent.width - parent.paddingRight,
                parent.paddingTop+it.height
            ),paint)
        }

    }

    /**
     * 可见view生成bitmap
     * @param view
     * @return
     */
    private fun view2Bitmap(view: View): Bitmap {
        var view = view
        when (view) {
            is ScrollView -> {
                view = view.getChildAt(0)
            }
            is NestedScrollView -> {
                view = view.getChildAt(0)
            }
            is HorizontalScrollView -> {
                view = view.getChildAt(0)
            }
        }
        val bmp = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)
        view.draw(canvas)
        return bmp
    }
}