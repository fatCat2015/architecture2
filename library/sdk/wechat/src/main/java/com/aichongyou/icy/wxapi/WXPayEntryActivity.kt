package com.aichongyou.icy.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.eju.appbase.base.AppBaseActivity
import com.eju.wechat.api
import com.eju.wechat.callBackMap
import com.eju.wechat.databinding.ActivityWxCallbackBinding
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import timber.log.Timber

/**
 * wx支付回调页面
 */
class WXPayEntryActivity :AppBaseActivity<ActivityWxCallbackBinding>(),IWXAPIEventHandler{

    override fun showTitle(): Boolean {
        return false
    }
    override fun observe() {

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        api.handleIntent(intent,this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            api.handleIntent(intent,this)
        }
    }

    override fun onResp(resp: BaseResp?) {
        resp?.let {
            when (it.type) {
                ConstantsAPI.COMMAND_PAY_BY_WX -> {
                    handlePayResult(it)
                }
            }
        }
        finish()
    }

    private fun handlePayResult(resp: BaseResp){
        val transaction = resp.transaction
        val callback = callBackMap[transaction]
        when (resp.errCode) {
            //如果支付成功则去后台查询支付结果再展示用户实际支付结果。注意一定不能以客户端返回作为用户支付的结果，
            //应以服务器端的接收的支付通知或查询API返回的结果为准
            BaseResp.ErrCode.ERR_OK -> {
                callback?.onSuccess(transaction)
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback?.onCancel()
            }
            else -> {
                callback?.onFailed(resp.errStr)
            }
        }
        callBackMap.remove(transaction)
    }


    override fun onReq(resp: BaseReq?) {


    }
}
