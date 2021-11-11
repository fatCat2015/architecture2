package com.eju.architecture.global

import android.content.Context
import android.os.Build


/**
 * true 表示已经获取了允许安装未知来源的权限
 */
fun canRequestPackageInstalls():Boolean{
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        return true
    }
    return application.packageManager.canRequestPackageInstalls()
}