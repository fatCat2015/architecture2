package com.eju.liveeventbus

import androidx.lifecycle.Observer

internal class EventObserver<T>(
    private val observer:Observer<T>,
    private val liveDataVersionWhenObserve:Int,
    private val sticky:Boolean,
) :Observer<T> {

    private var firstFlag:Boolean = true

    override fun onChanged(data: T) {
        if(firstFlag ){
            if(sticky || liveDataVersionWhenObserve == LiveEventBus.START_DATA_VERSION){
                observer.onChanged(data)
            }
            firstFlag = false
        }else{
            observer.onChanged(data)
        }

    }


}