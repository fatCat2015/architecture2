/**
 * Copyright 2020 Danny Keng
 */
package com.eju.tools.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import java.lang.reflect.Field
import kotlin.jvm.Throws

/**
 * App Bar Layout Behavior
 */
class AppBarLayoutBehavior @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppBarLayout.Behavior(context, attrs) {
    // fling上滑appbar然后迅速fling下滑recycler时, HeaderBehavior的mScroller并未停止, 会导致上下来回晃动
    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (type == ViewCompat.TYPE_NON_TOUCH && topAndBottomOffset == 0) { //recycler view的惯性比较大 ,会顶在头部一会儿, 到头直接干掉它的滑动
            ViewCompat.stopNestedScroll(target, type)
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: AppBarLayout, ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val scrollerField = getScrollerField()
            scrollerField?.isAccessible = true
            (scrollerField?.get(this) as OverScroller?)?.abortAnimation()
        }

        return super.onInterceptTouchEvent(parent, child, ev)
    }

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     * @return Field
     * @throws NoSuchFieldException
     */
    @Throws(NoSuchFieldException::class)
    private fun getScrollerField(): Field? {
        val superclass: Class<*>? = this.javaClass.superclass

        return try {
            // support design 27及一下版本
            val headerBehaviorType: Class<*>? = superclass?.superclass

            headerBehaviorType?.getDeclaredField("mScroller")
        } catch (e: NoSuchFieldException) {
//            e.printStackTrace()
            // 可能是28及以上版本
            val headerBehaviorType = superclass?.superclass?.superclass
            headerBehaviorType?.getDeclaredField("scroller")
        }
    }
}