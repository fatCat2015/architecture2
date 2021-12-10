package com.eju.retrofit

import android.app.Application
import android.content.Context
import com.eju.startup.Initializer
import com.facebook.stetho.Stetho

class RetrofitInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        application = context as Application
        if(BuildConfig.showStethoInfo){
            Stetho.initializeWithDefaults(context)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    companion object{
        internal lateinit var application: Application
            private set
    }
}