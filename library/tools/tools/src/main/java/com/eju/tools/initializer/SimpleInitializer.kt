package com.eju.tools.initializer

import androidx.startup.Initializer


abstract class SimpleInitializer<T>: Initializer<T> {

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(LogInitializer::class.java)
    }

}