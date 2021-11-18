package com.eju.retrofit

import com.eju.cache.CacheProxy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

object RemoveCache {

    private val cacheProxy:CacheProxy by lazy {
        CacheProxy(context = RetrofitInitializer.application,
            maxCountOfMemoryCacheObj = 60,
            useMemoryCache = false,
            useDiskcache = true
        )
    }


    fun <T> request(key:String,cachedTime:Long, cachedTimeUnit: TimeUnit, cacheStrategy: CacheStrategy, block: suspend ()->T): Flow<T> {
        return when(cacheStrategy){
            CacheStrategy.NONE ->{
                flow{
                    emit(block())
                }
            }
            CacheStrategy.USE_CACHE_IF_FAILED ->{
                useCacheIfRemoteFailed(key,cachedTime,cachedTimeUnit,block)
            }
            CacheStrategy.USE_CACHE_IF_EXISTS ->{
                useCacheIfExists(key,cachedTime,cachedTimeUnit,block)
            }
            CacheStrategy.FIRST_CACHE_THEN_REMOTE ->{
                firstCacheThenRemote(key,cachedTime,cachedTimeUnit,block)
            }
        }
        val a:StackTraceElement?=null
    }


    /**
     * 1.block()执行成功,缓存结果,返回结果
     * 2.block()执行失败,尝试获取并返回缓存,并且还是会抛出异常
     */
     private fun <T> useCacheIfRemoteFailed(key:String,cachedTime:Long, cachedTimeUnit: TimeUnit, block: suspend ()->T):Flow<T>{
        return flow {
            emit(block().also {
                cacheProxy.put(key, it, cachedTime, cachedTimeUnit)
            })
        }.catch { exception->
            cacheProxy.get<T>(key)?.let {
                emit(it)
            }
            throw exception
        }
    }


    /**
     * 1.有缓存并且缓存未过期返回缓存,不会执行block
     * 2.无缓存或者缓存过期,执行block,缓存结果,返回结果
     */
    private fun <T> useCacheIfExists(key:String,cachedTime:Long, cachedTimeUnit: TimeUnit, block: suspend ()->T):Flow<T>{
        return flow {
            cacheProxy.get<T>(key)?.let {
                emit(it)
            }?:emit(block().also {
                cacheProxy.put(key, it, cachedTime, cachedTimeUnit)
            })
        }
    }


    /**
     * 有缓存并且缓存未过期,发射缓存data,然后执行block,缓存结果,返回结果
     */
    private fun <T> firstCacheThenRemote(key:String, cachedTime:Long, cachedTimeUnit: TimeUnit, block:suspend ()->T): Flow<T> {
        return flow {
            cacheProxy.get<T>(key)?.let {
                emit(it)
            }
            emit(block().also {
                cacheProxy.put(key, it, cachedTime, cachedTimeUnit)
            })
        }
    }



    enum class CacheStrategy{
        NONE,
        USE_CACHE_IF_FAILED,
        USE_CACHE_IF_EXISTS,
        FIRST_CACHE_THEN_REMOTE,
    }

}
