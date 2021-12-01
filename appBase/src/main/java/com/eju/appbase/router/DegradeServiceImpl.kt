package com.eju.appbase.router

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.launcher.ARouter
import timber.log.Timber


/**
 * 全局降级策略,通过path没有找到目标页面
 */
@Route(path = "/xxx/xxx")
class DegradeServiceImpl :DegradeService{
    override fun onLost(context: Context?, postcard: Postcard?) {
        Timber.i("DegradeServiceImpl onLost: ${postcard}")
        ARouter.getInstance().build(PagePath.Other.NotFound)
            .withString("path",postcard?.path)
            .navigation()
    }

    override fun init(context: Context?) {
        Timber.i("DegradeServiceImpl init: ${context}")
    }
}