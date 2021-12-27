package com.eju.liveeventbus

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

//liveData反射
//MutableLiveData当前数据的mVersion
val MutableLiveData<*>.liveDataVersion :Int get() {
    val mVersionField = this.javaClass.superclass.getDeclaredField("mVersion")
    mVersionField.isAccessible = true
    return mVersionField.get(this) as Int
}
//LiveData的开始数据的mVersion
val startLiveDataVersion :Int by lazy {
    val startVersionField = LiveData::class.java.getDeclaredField("START_VERSION")
    startVersionField.isAccessible = true
    startVersionField.get(null) as Int
}

//activity
val ComponentActivity.liveEventBus :LiveEventBus by lazy {
    LiveEventBus.instance
}

fun <T> ComponentActivity.observeEventSticky(key:String, observer: Observer<T>) {
    liveEventBus.observe(this,key,true, observer )
}

fun <T> ComponentActivity.observeEvent(key:String, observer: Observer<T>) {
    liveEventBus.observe(this,key,false,observer)
}

fun <T> ComponentActivity.postEvent(key:String, event:T, useLatest :Boolean = false) {
    liveEventBus.post(key,event,useLatest)
}

//fragment
val Fragment.liveEventBus :LiveEventBus by lazy {
    LiveEventBus.instance
}

fun <T> Fragment.observeEventSticky(key:String, observer: Observer<T>) {
    liveEventBus.observe(this,key,true, observer )
}

fun <T> Fragment.observeEvent(key:String, observer:Observer<T>) {
    liveEventBus.observe(this,key,false,observer)
}

fun <T> Fragment.postEvent(key:String, event:T, useLatest :Boolean = false) {
    liveEventBus.post(key,event,useLatest)
}

