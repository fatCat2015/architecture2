package com.eju.cache.core

interface Cache<K,V> {

    fun size():Long

    fun maxSize():Long

    fun remove(key:K)

    fun put(key:K, value:V)

    fun get(key:K):V?

    fun clear()

}