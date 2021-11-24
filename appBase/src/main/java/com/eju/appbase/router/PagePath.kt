package com.eju.appbase.router

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard

object RouterExtras{

}

fun Postcard.navigation(fragment: Fragment,requestCode:Int){
    fragment.activity?.let {activity->
        LogisticsCenter.completion(this)
        fragment.startActivityForResult(
            Intent(activity,destination).putExtras(extras)
            ,requestCode)
    }

}

object PagePath {

    object Start{
        //启动页
        const val Splash="/Start/Splash"
        //引导页
        const val Guide="/Start/Guide"
        //登录页
        const val Login="/Start/Login"
    }

    object Main{
        //首页
        const val Home="/Main/Home"
    }

    object DemoModule{
        const val Demo="/DemoModule/Demo"
        const val ImageDetail="/DemoModule/ImageDetial"
    }

    fun createPathUri(path:String):Uri{
        return Uri.parse("eju://mobile.app.yilou${path}")
    }

    fun createPathUri1(path:String):Uri{
        return Uri.parse("http://www.jiandanhome.com${path}")
    }
}

