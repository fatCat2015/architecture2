package com.eju.tools.initializer

import android.content.Context
import com.eju.tools.BuildConfig
import com.eju.tools.application
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import timber.log.Timber

class MMKVInitializer:SimpleInitializer<Unit>() {
    override fun create(context: Context) {
        Timber.i("Initializer init ${this}")
        MMKV.initialize(context,if(BuildConfig.DEBUG) MMKVLogLevel.LevelDebug else MMKVLogLevel.LevelNone)
    }

}