package com.aichongyou.icy.wxapi

import android.content.Intent
import android.os.Bundle
import com.eju.appbase.base.AppBaseActivity
import com.eju.wechat.R
import com.eju.wechat.api
import com.eju.wechat.callBackMap
import com.eju.wechat.databinding.ActivityWxCallbackBinding
import com.eju.wechat.stateCache
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import timber.log.Timber

/**
 * wx分享,wx授权登录等回调页面
 */
class WXEntryActivity:AppBaseActivity<ActivityWxCallbackBinding>(),IWXAPIEventHandler {

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

    override fun onReq(p0: BaseReq?) {

    }

    override fun onResp(resp: BaseResp?) {
        resp?.let {
            when (it.type) {
                ConstantsAPI.COMMAND_SENDAUTH ->{
                    handleSendAuthResult(it as SendAuth.Resp)
                }
                ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX ->{
                    handleSendMsgResult(it as SendMessageToWX.Resp)
                }
                ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM ->{
                    handleLaunchWxMiniProgramResult(it as WXLaunchMiniProgram.Resp)
                }
            }
        }
        finish()
    }

    /**
     * todo 处理会回调两次的问题
     */
    private fun handleSendAuthResult(resp: SendAuth.Resp){
        val transaction = resp.transaction
        val callback = callBackMap[transaction]
        Timber.i("handleSendAuthResult ${callback} ")
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                if(resp.state == stateCache){
                    callback?.onSuccess(resp.code)
                }else{
                    callback?.onFailed("state 校验不通过")
                }
            }
            BaseResp.ErrCode.ERR_USER_CANCEL ,BaseResp.ErrCode.ERR_AUTH_DENIED-> {
                callback?.onCancel()
            }
            else -> {
                callback?.onFailed(resp.errStr)
                showToast("${getString(R.string.wx_send_auth_failed)},${resp.errStr}")
            }
        }
        callBackMap.remove(transaction)
    }


    private fun handleSendMsgResult(resp: SendMessageToWX.Resp){
        val transaction = resp.transaction
        val callback = callBackMap[transaction]
        Timber.i("handleSendMsgResult ${callback} ")
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                callback?.onSuccess(transaction)
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback?.onCancel()
            }
            else -> {
                callback?.onFailed(resp.errStr)
                showToast("${getString(R.string.wx_share_failed)},${resp.errStr}")
            }
        }
        callBackMap.remove(transaction)
    }

    private fun handleLaunchWxMiniProgramResult(resp: WXLaunchMiniProgram.Resp){
        val transaction = resp.transaction
        val callback = callBackMap[transaction]
        Timber.i("handleLaunchWxMiniProgramResult ${callback} ")
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                callback?.onSuccess(resp.extMsg)
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback?.onCancel()
            }
            else -> {
                callback?.onFailed(resp.errStr)
                showToast("${getString(R.string.wx_share_failed)},${resp.errStr}")
            }
        }
        callBackMap.remove(transaction)
    }


}