package com.eju.architecture.core

import android.os.Bundle
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ContentFrameLayout
import androidx.viewbinding.ViewBinding
import com.eju.tools.hideSoftInput
import com.eju.tools.isShouldHideSoftInput
import com.eju.tools.initializer.NetworkState
import com.eju.tools.initializer.observeNetworkStatus
import com.eju.architecture.viewBinding
import com.eju.tools.fixAndroidQMemoryLeak


abstract class BaseActivity<V:ViewBinding>:AppCompatActivity(), IViewBehavior,IExceptionHandler {

    protected val binding:V by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fixAndroidQMemoryLeak()
        setContentView(binding.root)
        addTitle()
        setStatusBar()
        observe()
        afterCreate(savedInstanceState)
        setListeners()
        observeNetworkStatus { connected, networkState ->
            onNetworkStateChanged(connected,networkState)
        }
    }

    private fun addTitle(){
        if(showTitle()){
            titleView<ViewBinding>()?.let{ titleView->
                val contentFrameLayout = findViewById<ContentFrameLayout>(android.R.id.content)
                val contentView=contentFrameLayout.getChildAt(0)
                contentFrameLayout.removeViewAt(0)
                contentFrameLayout.addView(LinearLayout(this).apply {
                    orientation= LinearLayout.VERTICAL
                    val titleBinding=titleView.binding
                    titleView.onBindView(titleBinding)
                    addView(titleBinding.root)
                    addView(contentView)
                },contentView.layoutParams)
            }
        }
    }

    open fun showTitle() = true

    open fun <V:ViewBinding> titleView():ITitleView<V>?=null

    private fun setStatusBar(){
        if(immersionBarEnabled()){
            setImmersionBar()
        }
    }

    open fun immersionBarEnabled() = true

    open fun setImmersionBar(){

    }

    abstract fun observe()

    abstract fun afterCreate(savedInstanceState: Bundle?)

    open fun setListeners() = Unit

    open fun onNetworkStateChanged(connected: Boolean, newNetworkState: NetworkState){

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //处理非EditText处点击时,软键盘的收起
        //这种方案有问题,多个EditText点击切换时,会先关闭一次键盘再打开
        ev?.let {
            if(it.action != MotionEvent.ACTION_DOWN){
                if(isShouldHideSoftInput(currentFocus, ev)){
                    hideSoftInput(currentFocus?.windowToken, this)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}