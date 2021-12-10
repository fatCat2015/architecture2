package com.eju.appbase.router

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.BuildConfig
import com.eju.startup.Initializer

class ARouterInitializer:Initializer<Unit> {
    override fun create(context: Context) {
        if(BuildConfig.DEBUG){  //ARouter
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(context as Application)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}