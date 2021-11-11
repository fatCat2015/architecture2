package com.eju.architecture

import android.app.Application

open class BaseApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object{
        internal lateinit var application:Application
    }
}