package com.eju.tools

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Looper
import com.eju.tools.initializer.ApplicationInitializer


val application :Application get() = ApplicationInitializer.application

val packageName: String get() = application.packageName

val isInMainThread :Boolean get() = Looper.getMainLooper().thread == Thread.currentThread()

/**
 * 获取当前进程的名称，默认进程名称是包名
 */
val currentProcessName: String?
    get() {
        val pid = android.os.Process.myPid()
        val mActivityManager = application.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        for (appProcess in mActivityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

val versionName:String?
    get(){
        return try {
            application.packageManager.getPackageInfo(application.packageName,0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

val versionCode:Int?
    get(){
        return try {
            application.packageManager.getPackageInfo(application.packageName,0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

val isAppDarkMode: Boolean
    get() = (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES



