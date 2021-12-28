package com.eju.tools

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.lifecycle.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import timber.log.Timber
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * android R (11,sdkInt:30) Toast的限制
 * 1.text toast 不允许自定义,setGravity()和setMargin()不会有效果
 * 2.custom toast,在后台时不能展示toast
 * 3.新增Toast.Callback回调,Callback object to be called when the toast is shown or hidden.
 * 4.setView被标记为Deprecated
 * 总结:老老实实使用Toast.makeText(context,text,duration).show()
 */
class Toaster{

    private val toastMap :MutableMap<LifecycleOwner,WrappedToast> by lazy {
        mutableMapOf()
    }

    private val mainHandler :Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val isInMainThread :Boolean get() = Looper.getMainLooper() == Looper.myLooper()

    private inner class ToastData(var text: CharSequence,@ToastDuration var duration: Int)

    private inner class WrappedToast(
        private val lifecycleOwner: LifecycleOwner,
        private val toast:Toast,
        private val toastDataLiveData:MutableLiveData<ToastData>,
    ):LifecycleEventObserver,Observer<ToastData>{

        init {
            toastDataLiveData.observe(lifecycleOwner,this)
            lifecycleOwner.lifecycle.addObserver(this)
        }

        fun setValue(text: CharSequence,@ToastDuration duration: Int){
            val newToastData :ToastData = toastDataLiveData.value?.apply {
                this.text = text
                this.duration = duration
            }?:ToastData(text,duration)
            if(isInMainThread){
                toastDataLiveData.value = newToastData
            }else{
                mainHandler.post { toastDataLiveData.value = newToastData }
            }
        }

        override fun onChanged(t: ToastData?) {
            t?.let {
                Timber.i("show toast --> LifecycleOwner:${lifecycleOwner} text:${it.text}")
                toast.setText(it.text)
                toast.duration = it.duration
                toast.show()
            }
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if(source.lifecycle.currentState==Lifecycle.State.DESTROYED){
                Timber.i("LifecycleOwner destroyed --> LifecycleOwner:${source}")
                toast.cancel()
                toastMap.remove(source)
                toastMap.forEach {
                    Timber.i("LifecycleOwner destroyed --> ${it.value} = ${it.value}")
                }
            }
        }

    }


    private val toastCreator:(Context)-> Toast by lazy {
        {
            Toast.makeText(it,"",Toast.LENGTH_SHORT)
        }
    }

    @Synchronized
    fun showToast(context: Context,
                  lifecycleOwner: LifecycleOwner,
                  text:CharSequence,
                  @ToastDuration duration:Int = Toast.LENGTH_SHORT
    ){
        if(lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED){
            return
        }
        toastMap[lifecycleOwner]?.let {
            it.setValue(text,duration)
        }?:WrappedToast(
            lifecycleOwner = lifecycleOwner,
            toast = toastCreator(context),
            toastDataLiveData = MutableLiveData(ToastData(text,duration))
        ).also {
            toastMap[lifecycleOwner] = it
        }
    }

    fun showToast(context: Context,msg:CharSequence,@ToastDuration duration:Int = Toast.LENGTH_SHORT){
        toastCreator(context).apply {
            setText(msg)
            setDuration(duration)
        }.show()
    }
}


@IntDef(value = [Toast.LENGTH_SHORT, Toast.LENGTH_LONG])
@Retention(
    RetentionPolicy.SOURCE
)
annotation class ToastDuration

@IntDef(value = [BaseTransientBottomBar.LENGTH_SHORT, BaseTransientBottomBar.LENGTH_LONG,BaseTransientBottomBar.LENGTH_INDEFINITE])
@Retention(
    RetentionPolicy.SOURCE
)
annotation class SnackDuration
