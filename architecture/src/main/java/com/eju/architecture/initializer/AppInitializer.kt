package com.eju.architecture.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class AppInitializer:SimpleInitializer<Unit>() {

    override fun create(context: Context) {
        application = context as Application
    }

    companion object{
        internal lateinit var application: Application
    }

}