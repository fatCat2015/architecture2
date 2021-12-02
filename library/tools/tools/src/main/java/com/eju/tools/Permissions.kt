package com.eju.tools

import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionMediator
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ForwardScope
import timber.log.Timber


/**
 * true 表示已经获取了允许安装未知来源的权限
 */
fun canRequestPackageInstalls():Boolean{
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        return true
    }
    return application.packageManager.canRequestPackageInstalls()
}

interface PermissionChecker{
    fun requestPermissions(activity: FragmentActivity,vararg permissions: String,
                           deniedCallback:((deniedList:List<String>)->Unit)?=null,
                           allGrantedCallback:(grantedList:List<String>)->Unit)

    fun requestPermissions(fragment: Fragment,vararg permissions: String,
                           deniedCallback:((deniedList:List<String>)->Unit)?=null,
                           allGrantedCallback:(grantedList:List<String>)->Unit)

    fun showForwardToSettingsDialog(deniedList:List<String>)

    fun forwardToSettings(deniedList: List<String>)

}

class PermissionCheckerImpl: PermissionChecker {

    private var callback: ForwardScope?=null

    private var fragment:Fragment?=null

    private var activity:FragmentActivity?=null

    private var showForwardToSettingsDialogOnce:Boolean = false

    private val context:Context?
        get() = activity?:fragment?.activity

    override fun requestPermissions(
        activity: FragmentActivity,
        vararg permissions: String,
        deniedCallback: ((deniedList: List<String>) -> Unit)?,
        allGrantedCallback: (grantedList: List<String>) -> Unit
    ) {
        this.activity=activity
        request(PermissionX.init(activity),*permissions,deniedCallback = deniedCallback,allGrantedCallback = allGrantedCallback)
    }

    override fun requestPermissions(
        fragment: Fragment,
        vararg permissions: String,
        deniedCallback: ((deniedList: List<String>) -> Unit)?,
        allGrantedCallback: (grantedList: List<String>) -> Unit
    ) {
        this.fragment=fragment
        request(PermissionX.init(fragment),*permissions,deniedCallback = deniedCallback,allGrantedCallback = allGrantedCallback)
    }

    private fun request(permissionMediator: PermissionMediator, vararg permissions: String,
                        deniedCallback:((deniedList:List<String>)->Unit)?,
                        allGrantedCallback:(grantedList:List<String>)->Unit){
        permissionMediator.permissions(permissions.toList())
            .onForwardToSettings { callback, deniedList ->
                if(!showForwardToSettingsDialogOnce){
                    this.callback = callback
                    showForwardToSettingsDialog(deniedList)
                    showForwardToSettingsDialogOnce = true
                }
            }
            .request { allGranted, grantedList, deniedList ->
                if(allGranted){
                    allGrantedCallback.invoke(grantedList)
                }else{
                    deniedCallback?.invoke(deniedList)
                }
                Timber.i("requestPermissions result allGranted:${allGranted},grantedList:${grantedList},deniedList:${deniedList}")
                this.callback = null
                this.fragment = null
                this.activity = null
            }
    }

    override fun showForwardToSettingsDialog(deniedList: List<String>) {
        context?.let { context->
            callback?.showForwardToSettingsDialog(deniedList,context.getString(R.string.allow_necessary_permissions),
                context.getString(R.string.confirm),
                context.getString(R.string.cancel)
            )
        }
    }

    override fun forwardToSettings(deniedList: List<String>) {

    }

}

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




