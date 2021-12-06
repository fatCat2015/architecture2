package com.eju.architecture.core

import androidx.lifecycle.MutableLiveData
import com.eju.tools.isInMainThread

class MainSaveLiveData<T>:MutableLiveData<T>() {

    override fun setValue(value: T) {
        if(isInMainThread){
            super.setValue(value)
        }else{
            postValue(value)
        }
    }
}