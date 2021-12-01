package com.eju.appbase.router

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.eju.tools.isInMainThread
import timber.log.Timber

/**
 * 拦截跳转过程，面向切面编程
 * 比较经典的应用就是在跳转过程中处理登陆事件，这样就不需要在目标页重复做登陆检查
 * 拦截器会在跳转之间执行，多个拦截器会按优先级顺序依次执行
 */
@Interceptor(priority = 0,name = "全局拦截器")
class PageInterceptor:IInterceptor {
    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        val path=postcard.path   //目标页面Route注解中的path
        val extra=postcard.extra  //目标页面Route注解中的extras
        Timber.i("PageInterceptor process: ${path} ${extra} ${isInMainThread}")
        callback.onContinue(postcard)
    }

    override fun init(context: Context) {
        Timber.i("PageInterceptor init: ${context}")
    }
}