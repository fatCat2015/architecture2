package com.eju.tools

import android.app.Activity
import android.app.FragmentManager
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import java.util.*
import kotlin.reflect.KClass

internal val activityCache = LinkedList<Activity>()

val topActivity : Activity? get() = activityCache.lastOrNull()

val activityList : List<Activity> get() = activityCache.toList()

fun finishAllActivities(){
    activityCache.forEach {
        it.finish()
    }
    activityCache.clear()
}

fun finishAllActivitiesExceptTop() {
    topActivity?.let { topActivity ->
        activityCache.removeAll {
            if (it != topActivity) it.finish()
            it != topActivity
        }
    }
}


fun startActivity(intent: Intent) = topActivity?.startActivity(intent)

fun <T:Activity> isActivityExists(clazz: KClass<T>) = activityCache.any { it.javaClass == clazz }

fun <T:Activity> finishActivities(clazz: KClass<T>) = activityCache.removeAll { activity->
    (activity.javaClass == clazz).apply {
        if(this){
            activity.finish()
        }
    }
}


/**
 * move task to back instead finish activity when current activity is task root
 */
fun ComponentActivity.moveTaskToBackWhenPressBack() {
    if(isTaskRoot){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveTaskToBack(false)
            }
        })
    }
}


inline fun ComponentActivity.pressBackTwiceToExitApp(delayMillis: Long = 2000, crossinline onFirstBackPressed: () -> Unit) {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        private var lastBackTime: Long = 0
        override fun handleOnBackPressed() {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackTime > delayMillis) {
                onFirstBackPressed()
                lastBackTime = currentTime
            } else {
                finishAllActivities()
            }
        }
    })
}
 fun ComponentActivity.fixAndroidQMemoryLeak(){
    if(isTaskRoot && Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){
        try {
            val fallbackOnBackPressedFiled = onBackPressedDispatcher.javaClass.getDeclaredField(
                "mFallbackOnBackPressed"
            )
            fallbackOnBackPressedFiled.isAccessible = true
            fallbackOnBackPressedFiled.set(onBackPressedDispatcher, Runnable {
                val fragmentManager: FragmentManager = fragmentManager
                if (!fragmentManager.isStateSaved && fragmentManager.popBackStackImmediate()) {
                    //do nothing
                }else{
                    finishAfterTransition()
                }
            })
            fallbackOnBackPressedFiled.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


