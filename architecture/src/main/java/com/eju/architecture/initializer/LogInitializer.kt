package com.eju.architecture.initializer

import android.content.Context
import androidx.startup.Initializer
import com.eju.architecture.BuildConfig
import com.eju.architecture.global.saveCrashLogLocally
import timber.log.Timber

class LogInitializer:SimpleInitializer<Unit>()  {
    override fun create(context: Context) {
        if(BuildConfig.saveCrashLogLocally){
            saveCrashLogLocally()
        }
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(AppInitializer::class.java)
    }
}