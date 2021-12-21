package com.eju.baseadapter.section

import android.graphics.Canvas
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CeilingItemDecoration:RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val adapter = (parent.adapter as? SectionAdapter<*,*,*,*,*>) ?: return
        val layoutManager = (parent.layoutManager as? LinearLayoutManager) ?: return

        var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        if(firstVisiblePosition == RecyclerView.NO_POSITION){
            return
        }



//        val childCount = parent.childCount
//        for (index in 0 until childCount) {
//            val childView = parent.getChildAt(index)
//            val position = parent.getChildAdapterPosition(childView)
//            if(position == RecyclerView.NO_POSITION){
//                continue
//            }
//            val item = adapter.getItem(position)
//            if(){
//
//            }
//        }

    }
}