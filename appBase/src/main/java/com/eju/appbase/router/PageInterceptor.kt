package com.eju.appbase.router

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import timber.log.Timber

@Interceptor(priority = 0,name = "全局拦截器")
class PageInterceptor:IInterceptor {
    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        val path=postcard.path
        val extra=postcard.extra
        Timber.i("PageInterceptor process: ${path} ${extra}")
        callback.onContinue(postcard)

    }

    override fun init(context: Context) {
        Timber.i("PageInterceptor init: ${context}")
    }
}