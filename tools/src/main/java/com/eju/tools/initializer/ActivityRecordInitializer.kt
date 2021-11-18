package com.eju.tools.initializer

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.startup.Initializer
import com.eju.tools.activityCache
import timber.log.Timber

class ActivityRecordInitializer:SimpleInitializer<Unit>(){

    override fun create(context: Context) {
        Timber.i("Initializer init ${this}")
        (context as? Application)?.let {
            it.registerActivityLifecycleCallbacks(object:Application.ActivityLifecycleCallbacks{
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    activityCache.add(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    activityCache.remove(activity)
                }

            })
        }
    }


}