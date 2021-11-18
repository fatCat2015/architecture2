package com.eju.cache

import android.content.Context
import com.eju.cache.core.DiskCache
import com.eju.cache.core.MemoryCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class CacheProxy(
    private var context:Context,
    private var maxCountOfMemoryCacheObj:Int = 30,
    private var useMemoryCache:Boolean = false,
    private var useDiskcache:Boolean = true,
){

    private val diskCache: DiskCache<CacheBlock<*>> by lazy {
        DiskCache<CacheBlock<*>>(context)
    }

    private val memoryCache: MemoryCache<String, CacheBlock<*>> by lazy {
        MemoryCache<String, CacheBlock<*>>(
            objSizeCalculator = { _, _ -> 1 },
            maxSize = maxCountOfMemoryCacheObj
        )
    }

    fun <T> put(key:String,data:T,cachedTime:Long,cachedTimeUnit: TimeUnit){
        if(!useMemoryCache&&!useDiskcache){
            return
        }
        val cacheBlock= CacheBlock(data, System.currentTimeMillis()+cachedTimeUnit.toMillis(cachedTime))
        if(useMemoryCache){
            memoryCache.put(key,cacheBlock)    //内存缓存
        }
        if(useDiskcache){
            diskCache.put(key,cacheBlock)          //磁盘缓存
        }
    }

    fun <T> get(key:String):T?{
        var data:T?=null
        if(useMemoryCache){
            val cacheBlock= memoryCache.get(key)
            if(cacheBlock!=null){     //内存缓存存在
                if(cacheBlock.isOutOfDate()){   //内存缓存过期
                    memoryCache.remove(key)
                }else{
                    data=cacheBlock.data as? T?  //使用内存缓存数据
                }
            }
        }
        if(useDiskcache&&data==null){
            val cacheBlock= diskCache.get(key)
            if(cacheBlock!=null){
                if(cacheBlock.isOutOfDate()){   //磁盘缓存过期
                    diskCache.remove(key)
                }else{
                    if(useMemoryCache){
                        memoryCache.put(key,cacheBlock)   //内存缓存
                    }
                    data=cacheBlock.data as? T?  //使用磁盘缓存
                }
            }
        }
        return data
    }


    fun remove(key:String){
        if(useMemoryCache){
            memoryCache.remove(key)
        }
        if(useDiskcache){
            diskCache.remove(key)
        }

    }

    fun clear(){
        if(useMemoryCache){
            memoryCache.clear()
        }
        if(useDiskcache){
            diskCache.clear()
        }
    }





}