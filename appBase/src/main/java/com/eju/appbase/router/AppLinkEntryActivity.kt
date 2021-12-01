package com.eju.appbase.router

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.databinding.ActivityAppLinkEntryBinding
import com.eju.tools.realPath
import com.eju.tools.realQueryParameters
import timber.log.Timber

/**
 * AppLink的入口page,aRouter会自动解析uri,通过uri的path路由跳转对应的页面,并携带uri中的query参数
 * note1: path是区分大小写的
 * note2: 通过ARouter的uri方式,query中的参数不会添加到目标页面的intent中,必须使用ARouter的AutoWired和inject结合使用,才能正确获取query中的参数,并且此时这些参数才会添加到intent中
 */
class AppLinkEntryActivity:AppBaseActivity<ActivityAppLinkEntryBinding>() {

    private val useUri:Boolean = false

    override fun showTitle(): Boolean {
        return false
    }

    override fun observe() {

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        seek(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        seek(intent)
    }

    private fun seek(intent: Intent?){
        intent?.data?.let { uri->
            if(useUri){
                ARouter.getInstance().build(uri).navigation(this)
            }else{
                ARouter.getInstance().build(uri.realPath).apply {
                    uri.realQueryParameters.forEach { queryEntry->
                        withString(queryEntry.key,queryEntry.value)
                    }
                }.navigation(this)
            }
            finish()
        }?:finish()
    }

}