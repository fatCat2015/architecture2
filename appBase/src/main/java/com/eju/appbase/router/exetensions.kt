package com.eju.appbase.router

import android.content.Intent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard

fun Postcard.navigation(fragment: Fragment, requestCode:Int){
    fragment.activity?.let {activity->
        LogisticsCenter.completion(this)
        fragment.startActivityForResult(
            Intent(activity,destination).putExtras(extras)
            ,requestCode)
    }

}