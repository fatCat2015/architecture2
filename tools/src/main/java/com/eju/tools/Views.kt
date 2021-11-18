package com.eju.tools

import android.graphics.Paint
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.TextView
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



inline fun View.doOnClick(clickIntervals:Long = 300, crossinline block:()->Unit){
    setOnClickListener(OnIntervalClickListener(clickIntervals){
        block()
    })
}


var TextView.isPasswordVisible: Boolean
    get() = transformationMethod != PasswordTransformationMethod.getInstance()
    set(value) {
        transformationMethod = if (value) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
    }

fun TextView.addUnderline() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
}

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

fun RecyclerView.scrollToTop(){
    var layoutManager=this.layoutManager
    when(layoutManager){
        is LinearLayoutManager -> layoutManager.scrollToPositionWithOffset(0,0)
        is GridLayoutManager -> layoutManager.scrollToPositionWithOffset(0,0)
        is StaggeredGridLayoutManager -> layoutManager.scrollToPositionWithOffset(0,0)
    }
}

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


fun TabLayout.setUpWithViewPager2(viewPager2: ViewPager2, titles:List<CharSequence>){
    setUpWithViewPager2(viewPager2){tab, position ->
        tab.text = titles.getOrNull(position)
    }
}

inline fun TabLayout.setUpWithViewPager2(viewPager2: ViewPager2,
                                         crossinline onConfigureTab:(TabLayout.Tab, Int)->Unit){
    TabLayoutMediator(this,viewPager2,true,true
    ) { tab, position ->
        onConfigureTab.invoke(tab, position)
    }.attach()
}