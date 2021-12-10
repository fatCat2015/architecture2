package com.eju.tools.initializer

import com.eju.startup.Initializer


abstract class SimpleInitializer<T>: Initializer<T> {

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return mutableListOf(LogInitializer::class.java)
    }

}