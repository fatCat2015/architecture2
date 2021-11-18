package com.eju.tools.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

class ApplicationInitializer:Initializer<Unit>{

    override fun create(context: Context) {
        Timber.i("Initializer init ${this}")
        application = context as Application
    }

    companion object{
        internal lateinit var application: Application
            private set
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

}