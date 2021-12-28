package com.eju.tools

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.roundToInt



//common view
/**
 * view点击事件
 * @param clickIntervals 点击触发的事件间隔
 * @param block          执行点击的block
 */
inline fun View.doOnClick(clickIntervals:Long = 300, crossinline block:(View)->Unit){
    setOnClickListener(OnIntervalClickListener(clickIntervals){
        block(it)
    })
}

/**
 * 可见view(已绘制view)生成bitmap
 */
fun View.toBitmap():Bitmap{
    val view = when (this) {
        is ScrollView -> {
            this.getChildAt(0)
        }
        is NestedScrollView -> {
            this.getChildAt(0)
        }
        is HorizontalScrollView -> {
            this.getChildAt(0)
        }
        else -> this
    }
    val bmp = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    canvas.drawColor(Color.TRANSPARENT)
    view.draw(canvas)
    return bmp
}


//textView

/**
 * 设置密码的可见性
 */
var TextView.isPasswordVisible: Boolean
    get() = transformationMethod != PasswordTransformationMethod.getInstance()
    set(value) {
        transformationMethod = if (value) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
    }

/**
 * 添加下划线
 */
fun TextView.addUnderline() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
}

/**
 * textView展示倒计时
 */
inline fun TextView.startCountDown(
    lifecycleOwner: LifecycleOwner,
    secondInFuture: Int = 60,
    onStart: TextView.() -> Unit,
    crossinline onTick: TextView.(secondUntilFinished: Int) -> Unit,
    crossinline onFinish: TextView.() -> Unit,
) {
    val countDownTimer = object : CountDownTimer(secondInFuture * 1000L, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            onTick((millisUntilFinished / 1000f).roundToInt())
        }
        override fun onFinish() {
            this@startCountDown.onFinish()
        }
    }
    countDownTimer.start()
    onStart()
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            countDownTimer.cancel()
        }
    })
}

/**
 * 监听textView输入变化
 * @param debounceTime 输入变化触发的事件间隔
 */
inline fun TextView.doAfterTextChanged(debounceTime:Long=300L, crossinline action: (text: String) -> Unit){
    val textChangedRunnable =
        kotlinx.coroutines.Runnable {
            action.invoke(text.toString())
        }
    doAfterTextChanged {
        handler?.removeCallbacks(textChangedRunnable)
        handler?.postDelayed(textChangedRunnable,debounceTime)
    }
}


//recyclerView
/**
 * 滚动到顶部
 */
fun RecyclerView.scrollToTop(){
    var layoutManager=this.layoutManager
    when(layoutManager){
        is LinearLayoutManager -> layoutManager.scrollToPositionWithOffset(0,0)
        is GridLayoutManager -> layoutManager.scrollToPositionWithOffset(0,0)
        is StaggeredGridLayoutManager -> layoutManager.scrollToPositionWithOffset(0,0)
    }
}



//tabLayout

/**
 * tabLayout和ViewPager2关联
 */
fun TabLayout.setUpWithViewPager2(viewPager2: ViewPager2, titles:List<CharSequence>,autoRefresh : Boolean = true , smoothScroll : Boolean = true){
    setUpWithViewPager2(viewPager2,autoRefresh,smoothScroll){tab, position ->
        tab.text = titles.getOrNull(position)
    }
}
inline fun TabLayout.setUpWithViewPager2(viewPager2: ViewPager2,
                                         autoRefresh : Boolean = true , smoothScroll : Boolean = true,
                                         crossinline onConfigureTab:(TabLayout.Tab, Int)->Unit){
    TabLayoutMediator(this,viewPager2,autoRefresh,smoothScroll
    ) { tab, position ->
        onConfigureTab.invoke(tab, position)
    }.attach()
}

//ViewPager2
/**
 * 设置ViewPager2中的RecyclerView的OverScrollMode为never
 */
fun ViewPager2.setNeverOverScroll(){
    val childCount = childCount
    for (index in 0 until childCount) {
        val childView = getChildAt(0)
        if(childView is RecyclerView){
            childView.overScrollMode = View.OVER_SCROLL_NEVER
            return
        }
    }

}

