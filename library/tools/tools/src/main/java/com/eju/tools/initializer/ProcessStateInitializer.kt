package com.eju.tools.initializer

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber

class ProcessStateInitializer:SimpleInitializer<Unit>()  {

    override fun create(context: Context) {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
            }
            override fun onResume(owner: LifecycleOwner) {
                foregroundFlag = true
                dispatchProcessResume()
            }
            override fun onStop(owner: LifecycleOwner) {
                foregroundFlag = false
                dispatchProcessStop()
            }
        })
    }

    private fun dispatchProcessResume(){
        Timber.i("dispatchProcessResume ${processStateObserverList.size}")
        processStateObserverList.forEach {
            it.onProcessResume()
        }
    }

    private fun dispatchProcessStop(){
        Timber.i("dispatchProcessStop ${processStateObserverList.size}")
        processStateObserverList.forEach {
            it.onProcessStop()
        }
    }


}

private val processStateObserverList= mutableListOf<ProcessStateObserver>()

internal var foregroundFlag=false

//以下是对外方法
val processInForeground:Boolean get() = foregroundFlag

fun addProcessStateObserver(appStateObserver: ProcessStateObserver){
    if(!processStateObserverList.contains(appStateObserver)){
        processStateObserverList.add(appStateObserver)
        if(processInForeground){
            appStateObserver.onProcessResume()
        }else{
            appStateObserver.onProcessStop()
        }
    }
}

fun removeProcessStateObserver(appStateObserver: ProcessStateObserver){
    processStateObserverList.remove(appStateObserver)
}

fun clearProcessStateObservers(){
    processStateObserverList.clear()
}

interface ProcessStateObserver{
    fun onProcessResume()
    fun onProcessStop()
}