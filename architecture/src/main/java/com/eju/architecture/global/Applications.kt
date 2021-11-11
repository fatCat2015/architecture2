package com.eju.architecture.global

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Looper
import android.os.Process
import android.provider.Settings
import com.eju.architecture.BaseApplication
import com.eju.architecture.initializer.AppInitializer


val application :Application get() = AppInitializer.application

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


fun launchAppDetailsSettings(): Boolean =
    startSystemActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .apply { data = Uri.fromParts("package", packageName, null) })

fun relaunchApp(killProcess: Boolean = true) =
    application.packageManager.getLaunchIntentForPackage(packageName)?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(it)
        if (killProcess) Process.killProcess(Process.myPid())
    }

