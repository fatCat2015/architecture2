package com.eju.analysis

import android.content.Context
import androidx.startup.Initializer

class ContextInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        sContext = context
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    companion object{
        internal lateinit var sContext: Context
            private set
    }
}


