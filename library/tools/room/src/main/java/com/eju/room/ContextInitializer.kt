package com.eju.room

import android.content.Context
import com.eju.startup.Initializer

class ContextInitializer:Initializer<Unit> {
    override fun create(context: Context) {
        sContext = context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
    companion object{
        lateinit var sContext: Context
            private set
    }
}