package com.eju.tools.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class ApplicationInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        application = context as Application
    }

    companion object{
        internal lateinit var application: Application
            private set
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf()
    }

}