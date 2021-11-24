/**
 * Copyright 2019 Danny Keng
 */
package com.eju.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Add Cookies Interceptor
 */
class AddHeadersInterceptor(private val headers:Map<String,String>?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val commonHeaders = headers?.filter { it.key.isNotBlank()&&it.value.isNotBlank() }
        return if(commonHeaders.isNullOrEmpty()){
            chain.proceed(chain.request())
        }else{
            chain.request().newBuilder().run {
                commonHeaders.forEach{
                    addHeader(it.key,it.value)
                }
                chain.proceed(build())
            }
        }

    }
}
