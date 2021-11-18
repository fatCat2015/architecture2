package com.eju.cache.core

import android.util.LruCache

open class MemoryCache<K,V>(
    val objSizeCalculator:(K,V)->Int,
    private val maxSize:Int= (Runtime.getRuntime().totalMemory()/8).toInt(),
    private val create:(K)->V? = { null },
    private val onEntryRemoved:(evicted: Boolean, key: K, oldValue: V?, newValue: V?)->Unit = { _,_,_,_  ->Unit},
): Cache<K, V> {

    private val lruCache:LruCache<K,V> by lazy {
        object:LruCache<K,V>(maxSize){
            override fun sizeOf(key: K, value: V)=objSizeCalculator.invoke(key,value)
            override fun entryRemoved(evicted: Boolean, key: K, oldValue: V?, newValue: V?) {
                onEntryRemoved.invoke(evicted,key,oldValue,newValue)
            }
            /**
             * lruCache.get(Key)的时候 cache中没有对应的value 会调用create()方法尝试创建value
             */
            override fun create(key: K): V? {
                return create.invoke(key)
            }
        }

    }

    override fun size(): Long {
        return lruCache.size().toLong()
    }

    override fun maxSize(): Long {
        return lruCache.maxSize().toLong()
    }

    override fun remove(key: K) {
        lruCache.remove(key)
    }

    override fun put(key: K, value: V) {
        lruCache.put(key,value)
    }

    override fun get(key: K): V? {
        return lruCache.get(key)
    }

    override fun clear() {
        lruCache.evictAll()
    }
}