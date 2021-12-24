package com.eju.liveeventbus

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.*
import java.lang.IllegalStateException

object LiveEventBus {

    private val liveDataMap :HashMap<String,MutableLiveData<*>> by lazy {
        hashMapOf()
    }

    private val mainHandler :Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    internal val START_DATA_VERSION :Int by lazy {
        val startVersionField = LiveData::class.java.getDeclaredField("START_VERSION")
        startVersionField.isAccessible = true
        startVersionField.get(null) as Int
    }

    private val observerMap :HashMap<Observer<*>,EventObserver<*>> by lazy {
        hashMapOf()
    }

    @MainThread
    fun <T> observe(lifecycleOwner: LifecycleOwner,key:String, sticky:Boolean = false,observer:Observer<T>){
        assertMainThread("observe")

        val eventLiveData = (liveDataMap[key] as? MutableLiveData<T>) ?:MutableLiveData<T>().also {
            liveDataMap[key] = it
        }

        eventLiveData.observe(lifecycleOwner,EventObserver(
            observer =  observer,
            liveDataVersionWhenObserve = if(sticky) START_DATA_VERSION else getCurrentLiveDataVersion(eventLiveData),
            sticky = sticky
        ).also { observerMap[observer] = it })

        lifecycleOwner.lifecycle.addObserver(object: LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                val currentState: Lifecycle.State = source.lifecycle.currentState
                if (currentState == Lifecycle.State.DESTROYED) {
                    observerMap.remove(observer)
                }
            }
        })
    }

    @MainThread
    fun <T> observeForever(key:String, sticky:Boolean =false,observer:Observer<T>){
        assertMainThread("observeForever")
        val eventLiveData = (liveDataMap[key] as? MutableLiveData<T>) ?:MutableLiveData<T>().also {
            liveDataMap[key] = it
        }
        eventLiveData.observeForever(EventObserver(
            observer =  observer,
            liveDataVersionWhenObserve = if(sticky) START_DATA_VERSION else getCurrentLiveDataVersion(eventLiveData),
            sticky = sticky
        ).also { observerMap[observer] = it })
    }

    @MainThread
    fun <T> removeObserver(key:String,observer:Observer<T>){
        assertMainThread("removeObserver")
        val eventLiveData = (liveDataMap[key] as? MutableLiveData<T>) ?:MutableLiveData<T>().also {
            liveDataMap[key] = it
        }
        (observerMap[observer] as? EventObserver<T>)?.let {
            eventLiveData.removeObserver(it)
            observerMap.remove(observer)
        }
    }

    fun <T> post( key:String, event:T , useLatest :Boolean = false){
        (liveDataMap[key] as? MutableLiveData<T>)?.let { liveData ->
            if(useLatest){
                liveData.postValue(event)
            }else{
                if(isInMainThread()){
                    liveData.value = event
                }else{
                    mainHandler.post {
                        liveData.value = event
                    }
                }
            }
        }
    }

    private fun getCurrentLiveDataVersion(mutableLiveData: MutableLiveData<*>):Int{
        val mVersionField = mutableLiveData.javaClass.superclass.getDeclaredField("mVersion")
        mVersionField.isAccessible = true
        return mVersionField.get(mutableLiveData) as Int
    }

    private fun assertMainThread(methodName: String) {
        if (!isInMainThread()) {
            throw IllegalStateException(
                "Cannot invoke " + methodName + " on a background"
                        + " thread"
            )
        }
    }

    private fun isInMainThread() :Boolean = Looper.getMainLooper() == Looper.myLooper()


}