package com.eju.tools.initializer

import android.content.Context
import androidx.startup.Initializer
import com.eju.tools.BuildConfig
import com.eju.tools.saveCrashLogLocally
import timber.log.Timber

class LogInitializer:Initializer<Unit>  {
    override fun create(context: Context) {
        if(BuildConfig.saveCrashLogLocally){
            saveCrashLogLocally()
        }
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        Timber.i("Initializer init ${this}")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(ApplicationInitializer::class.java)
    }

}