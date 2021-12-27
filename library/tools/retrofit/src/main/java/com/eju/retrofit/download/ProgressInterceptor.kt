package com.eju.retrofit.download

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

@Deprecated("unuse")
class ProgressInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(ProgressResponseBody(originalResponse.body!!, object:ProgressListener{
                override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    Timber.tag("DownloadProxy").i("ProgressListener11111111111111:${bytesRead} / ${contentLength}")

                }
            }))
            .build()
    }
}