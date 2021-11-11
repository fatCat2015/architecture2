package com.eju.retrofit

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.facebook.stetho.Stetho

class RetrofitInitializer:Initializer<Unit> {
    override fun create(context: Context) {
        if(BuildConfig.showStethoInfo){
            Stetho.initializeWithDefaults(context as Application)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}