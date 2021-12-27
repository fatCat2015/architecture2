package com.eju.liveeventbus

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.*
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class LiveEventBus {

    private val liveDataMap :HashMap<String,MutableLiveData<*>> by lazy {
        hashMapOf()
    }

    private val mainHandler :Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val nonStickyObserverMap :HashMap<Observer<*>,NonStickyObserver<*>> by lazy {
        hashMapOf()
    }


    private fun <T> createIfAbsent(key :String):MutableLiveData<T>{
        return (liveDataMap[key] as? MutableLiveData<T>) ?:MutableLiveData<T>().also {
            liveDataMap[key] = it
        }
    }

    /**
     * 发送事件时,只有当lifecycleOwner处于active状态时,才会调用observer的onChanged
     * 不需要手动调用removeObserver,lifecycleOwner处于DESTROYED时会自动取消注册
     * @param lifecycleOwner
     * @param key
     * @param sticky true:注册时,如果liveData已经有数据了,会触发observer的onChanged; false:注册时,不会触发数据回调
     * @param observer
     */
    @MainThread
    fun <T> observe(lifecycleOwner: LifecycleOwner,key:String, sticky:Boolean = false,observer:Observer<T>){
        assertMainThread("observe")
        val eventLiveData = createIfAbsent<T>(key)
        if(sticky){
            eventLiveData.observe(lifecycleOwner,observer)
        }else{
            if(nonStickyObserverMap[observer] !=null){
                return
            }
            eventLiveData.observe(
                lifecycleOwner,NonStickyObserver(
                    key = key,
                    observer =  observer,
                    liveDataVersionWhenObserve = eventLiveData.liveDataVersion,
                ).also {
                    nonStickyObserverMap[observer] = it
                }.also {
                    lifecycleOwner.lifecycle.addObserver(it)
                }
            )
        }
    }

    /**
     * 发送事件时,会立即调用observer的onChanged
     * 需要手动调用removeObserver
     * @param key
     * @param sticky true:注册时,如果liveData已经有数据了,会触发observer的onChanged; false:注册时,不会触发数据回调
     * @param observer
     */
    @MainThread
    fun <T> observeForever(key:String, sticky:Boolean = false,observer:Observer<T>){
        assertMainThread("observeForever")
        val eventLiveData = createIfAbsent<T>(key)
        if(sticky){
            eventLiveData.observeForever(observer)
        }else{
            if(nonStickyObserverMap[observer] !=null){
                return
            }
            eventLiveData.observeForever(
                NonStickyObserver(
                    key = key,
                    observer =  observer,
                    liveDataVersionWhenObserve = eventLiveData.liveDataVersion,
                ).also {
                    nonStickyObserverMap[observer] = it
                }
            )
        }
    }

    @MainThread
    fun <T> removeObserver(key:String,observer:Observer<T>){
        assertMainThread("removeObserver")
        (nonStickyObserverMap[observer] as? NonStickyObserver<T>)?.let {
            (liveDataMap[key] as? MutableLiveData<T>)?.removeObserver(it)
        }
        nonStickyObserverMap.remove(observer)
    }

    /**
     * 发送事件
     * @param key
     * @param event
     * @param useLatest true:连续多次调用post,前面的数据会丢失,只会触发最后一个的数据回调; false:每次post,都会触发回调
     */
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

    private inner class NonStickyObserver<T>(
        private val key :String,
        private val observer:Observer<T>,
        private val liveDataVersionWhenObserve:Int,
    ):Observer<T>,LifecycleEventObserver{

        private var firstFlag:Boolean = true

        override fun onChanged(data: T) {
            if(firstFlag ){
                if(liveDataVersionWhenObserve == startLiveDataVersion){
                    observer.onChanged(data)
                }
                firstFlag = false
            }else{
                observer.onChanged(data)
            }
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            val currentState: Lifecycle.State = source.lifecycle.currentState
            if (currentState == Lifecycle.State.DESTROYED) {
                removeObserver(key,observer)
            }
        }
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

    companion object {
        val instance :LiveEventBus by lazy {
            LiveEventBus()
        }
    }

}