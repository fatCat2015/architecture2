package com.eju.permissions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * 用于权限检查,每次都使用一个新对象
 */
private val permissionChecker: PermissionChecker get() =  PermissionCheckerImpl()

fun FragmentActivity.requestPermissions(vararg permissions: String, deniedCallback:((deniedList:List<String>)->Unit)?=null,
                                        allGrantedCallback:(grantedList:List<String>)->Unit){
    permissionChecker.requestPermissions(this,*permissions,deniedCallback=deniedCallback,allGrantedCallback = allGrantedCallback)
}

fun Fragment.requestPermissions(vararg permissions: String, deniedCallback:((deniedList:List<String>)->Unit)?=null,
                                allGrantedCallback:(grantedList:List<String>)->Unit){
    permissionChecker.requestPermissions(this,*permissions,deniedCallback=deniedCallback,allGrantedCallback = allGrantedCallback)
}
