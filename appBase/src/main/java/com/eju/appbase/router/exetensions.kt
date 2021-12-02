package com.eju.appbase.router

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.utils.TextUtils
import java.net.URLEncoder

fun Postcard.navigation(fragment: Fragment, requestCode:Int){
    fragment.activity?.let {activity->
        LogisticsCenter.completion(this)
        fragment.startActivityForResult(
            Intent(activity,destination).putExtras(extras)
            ,requestCode)
    }
}


val Activity.aRouter:ARouter get() = ARouter.getInstance()

val Fragment.aRouter:ARouter get() = ARouter.getInstance()

fun <F:Fragment> newFragment(path:String,block:Postcard.()->Unit = {}):F{
    return ARouter.getInstance().build(path).apply {
        block()
    }.navigation() as F
}

/**
 * 获取原始的URI.通过AppLinkEntryActivity分发至的目标页面才有值,这个值是applink中的链接
 */
val Activity.rawUri:String? get() = intent.getStringExtra(ARouter.RAW_URI)


