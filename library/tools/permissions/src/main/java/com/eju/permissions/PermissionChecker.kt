package com.eju.permissions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface PermissionChecker{
    fun requestPermissions(activity: FragmentActivity, vararg permissions: String,
                           deniedCallback:((deniedList:List<String>)->Unit)?=null,
                           allGrantedCallback:(grantedList:List<String>)->Unit)

    fun requestPermissions(fragment: Fragment, vararg permissions: String,
                           deniedCallback:((deniedList:List<String>)->Unit)?=null,
                           allGrantedCallback:(grantedList:List<String>)->Unit)

    fun showForwardToSettingsDialog(deniedList:List<String>)

    fun forwardToSettings(deniedList: List<String>)

}