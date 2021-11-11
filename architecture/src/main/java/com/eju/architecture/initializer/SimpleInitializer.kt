package com.eju.architecture.initializer

import androidx.startup.Initializer

abstract class SimpleInitializer<T>:Initializer<T> {
    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}