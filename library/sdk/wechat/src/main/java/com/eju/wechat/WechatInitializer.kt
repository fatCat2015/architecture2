package com.eju.wechat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.startup.Initializer
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WechatInitializer:Initializer<Unit> {

    override fun create(context: Context) {
        api = WXAPIFactory.createWXAPI(context,BuildConfig.weChatAppId,BuildConfig.DEBUG)
        context.registerReceiver(object:BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                api.registerApp(BuildConfig.weChatAppId)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    companion object{
        internal lateinit var api: IWXAPI
            private set
    }
}


