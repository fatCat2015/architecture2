package com.eju.architecture.initializer

import android.content.Context
import androidx.startup.Initializer
import com.eju.architecture.BuildConfig
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

class MMKVInitializer:SimpleInitializer<Unit>() {
    override fun create(context: Context) {
        MMKV.initialize(context,if(BuildConfig.DEBUG) MMKVLogLevel.LevelDebug else MMKVLogLevel.LevelNone)
    }
}