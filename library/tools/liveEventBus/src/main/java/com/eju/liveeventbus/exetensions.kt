package com.eju.liveeventbus

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

fun <T> ComponentActivity.observeEventSticky(key:String, observer: Observer<T>) {
    LiveEventBus.observe(this,key,true, observer )
}

fun <T> ComponentActivity.observeEvent(key:String, observer: Observer<T>) {
    LiveEventBus.observe(this,key,false,observer)
}

fun <T> ComponentActivity.postEvent(key:String, event:T, useLatest :Boolean = false) {
    LiveEventBus.post(key,event,useLatest)
}

fun <T> Fragment.observeEventSticky(key:String, observer: Observer<T>) {
    LiveEventBus.observe(this,key,true, observer )
}

fun <T> Fragment.observeEvent(key:String, observer:Observer<T>) {
    LiveEventBus.observe(this,key,false,observer)
}

fun <T> Fragment.postEvent(key:String, event:T, useLatest :Boolean = false) {
    LiveEventBus.post(key,event,useLatest)
}

