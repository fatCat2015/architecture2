package com.eju.tools

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

private val toastMap = mutableMapOf<LifecycleOwner,AppToast>()


fun Application.showToast(msg:CharSequence?,@ToastDuration duration:Int){
    if(msg.isNullOrEmpty()){
        return
    }
    val toast=Toast.makeText(this, "",duration)
    toast.setText(msg)
    toast.show()
}


fun showToast(context: Context?,lifecycleOwner: LifecycleOwner,msg:CharSequence?,@ToastDuration duration:Int){
    if(msg.isNullOrEmpty()){
        return
    }
    if(context==null){
        return
    }
    val appToast=toastMap[lifecycleOwner]
    val lifecycle=lifecycleOwner.lifecycle
    if(appToast==null){
        if(lifecycle.currentState==Lifecycle.State.DESTROYED){
            return
        }else{
            toastMap[lifecycleOwner]= AppToast(
                Toast.makeText(context,"",Toast.LENGTH_SHORT),
                MutableLiveData<CharSequence>(msg),
                duration
            ).also { appToast->
                appToast.toastStr.observe(lifecycleOwner,  Observer{ str->
                    appToast.toast.setText(str)
                    appToast.toast.duration = appToast.toastLength
                    appToast.toast.show()
                })
                lifecycle.addObserver(object:DefaultLifecycleObserver{
                    override fun onDestroy(owner: LifecycleOwner) {
                        super.onDestroy(owner)
                        appToast.toast.cancel()
                        toastMap.remove(lifecycleOwner)
                    }
                })
            }
        }
    }else{
        appToast.toastLength=duration
        if(isInMainThread){
            appToast.toastStr.value=msg
        }else{
            appToast.toastStr.postValue(msg)
        }
    }
}


internal data class AppToast(
    val toast:Toast,
    val toastStr:MutableLiveData<CharSequence>,
    var toastLength:Int
)

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
