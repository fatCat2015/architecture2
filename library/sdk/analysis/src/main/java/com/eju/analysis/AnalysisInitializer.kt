package com.eju.analysis

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.eju.startup.Initializer

class AnalysisInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        Thread.sleep(300)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    override fun autoCreated(): Boolean {
        return BuildConfig.isModule
    }

}


