package com.eju.tools.widget

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class ColorTransition(private val startColor:Int, private val endColor:Int, private val duration:Long,
        private val colorChangeCallback:(Int)->Unit){


    private var colorAnimator:ValueAnimator?=null

    fun start(){
        colorAnimator=ValueAnimator.ofObject(ArgbEvaluator(),startColor,endColor)
        colorAnimator?.duration=duration
        colorAnimator?.interpolator=LinearInterpolator()
        colorAnimator?.addUpdateListener {
            colorChangeCallback.invoke(it.animatedValue as Int)
        }
        colorAnimator?.start()
    }

    fun cancel(){
        colorAnimator?.cancel()
    }

}