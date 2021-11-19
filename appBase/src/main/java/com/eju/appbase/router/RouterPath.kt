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

object RouterPath {

    object DemoModule{
        const val Demo="/DemoModule/Demo"
        const val ImageDetail="/DemoModule/ImageDetial"
    }

    object Module2{
        const val main="/module2/main"
    }

    fun createPathUri(path:String):Uri{
        return Uri.parse("eju://mobile.app.yilou${path}")
    }

    fun createPathUri1(path:String):Uri{
        return Uri.parse("http://www.jiandanhome.com${path}")
    }
}

