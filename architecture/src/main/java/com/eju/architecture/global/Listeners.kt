package com.eju.architecture.global

import android.view.View


open class OnIntervalClickListener(private val clickIntervals:Long = 300L,private val block:(View)->Unit):View.OnClickListener{

    private val viewKey = -101

    override fun onClick(v: View?) {
        v?.let { view ->
            val currentTime = System.currentTimeMillis()
            val lastClickTime :Long = (view.getTag(viewKey) as? Long)?:0L
            if((currentTime-lastClickTime)>clickIntervals){
                view.setTag(viewKey,currentTime)
                block(view)
            }
        }

    }
}