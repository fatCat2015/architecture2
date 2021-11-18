package com.eju.tools.initializer

import androidx.startup.Initializer
import timber.log.Timber

abstract class SimpleInitializer<T>:Initializer<T> {

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(LogInitializer::class.java)
    }

}