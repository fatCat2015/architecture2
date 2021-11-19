package com.eju.appbase.router

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import timber.log.Timber


@Route(path = "/xxx/xxx")
class DegradeServiceImpl :DegradeService{
    override fun onLost(context: Context?, postcard: Postcard?) {
        Timber.i("DegradeServiceImpl onLost: ${postcard}")
    }

    override fun init(context: Context?) {
        Timber.i("DegradeServiceImpl init: ${context}")
    }
}