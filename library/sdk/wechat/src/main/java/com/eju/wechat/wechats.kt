package com.eju.wechat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import com.eju.tools.*
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*


internal val api:IWXAPI by lazy {
    val context = ContextInitializer.sContext
    val api = WXAPIFactory.createWXAPI(context,BuildConfig.weChatAppId,BuildConfig.DEBUG)
    context.registerReceiver(object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            api.registerApp(BuildConfig.weChatAppId)
        }
    }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    Timber.i("IWXAPI初始化${api}")
    api
}

/**
 * WXMediaMessage.thumbData要求的最大值
 */
private const val thumbDataMaxSizeInKb = 32L

/**
 * 分享小程序时,WXMediaMessage.thumbData要求的最大值
 */
private const val miniProgramThumbDataMaxSizeInKb = 128L

/**
 * 要分享的图片的内容最大值
 */
private const val shareImageMaxSizeInKb = 10*1024L

private fun sendReq(request:BaseReq){
    api.sendReq(request)
}

/**
 * @param userOpenId todo ? 做啥的
 */
private fun assembleSendMessageRequest(msg:WXMediaMessage,scene:Int,transaction:String,userOpenId:String?):SendMessageToWX.Req{
    return SendMessageToWX.Req().apply {
        this.message = msg
        this.transaction = transaction
        this.scene = scene
        this.userOpenId = userOpenId
    }
}

private fun assembleMsg(mediaObj: WXMediaMessage.IMediaObject,title:String?,description:String?,thumbData:ByteArray? ):WXMediaMessage{
    return WXMediaMessage().apply {
        this.title = title
        this.description = description
        this.thumbData = thumbData
        this.mediaObject = mediaObj
    }
}


private fun randomTransaction(transaction:String?=null) = if(transaction.isNullOrEmpty()) "${UUID.randomUUID().toString().replace("-","")}" else transaction

private suspend fun getCompressedThumbData(thumbBitmap: Bitmap,isMiniProgram:Boolean = false):ByteArray{
    val thumbImageCacheFile = File(thumbBitmap.saveLocally(File(cacheDirPath,"wx_share_thumb_image_cache")))
    val compressedThumbImageFile = compressImageFile(thumbImageCacheFile, block = {
        resolution(screenWidth/3,screenWidth/3)
        size((if(isMiniProgram) miniProgramThumbDataMaxSizeInKb else thumbDataMaxSizeInKb)*1024)
    })
    return compressedThumbImageFile.toByteArray().apply {
        Timber.i("getCompressedThumbData:${this.size/1024}")
    }
}

internal var stateCache:String?=null

/**
 * wx授权(登录)
 * @param state 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止 csrf 攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加 session 进行校验
 */
suspend fun sendAuthRequest(callback: SendReqCallback,state: String = "${randomTransaction()}SESSIONSCK" ,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        sendReq(SendAuth.Req().apply {
            this.scope = "snsapi_userinfo"
            this.state = state
            this.transaction = transaction
        })
        stateCache = state
        callBackMap[transaction] = callback
    }
}

/**
 * 发起wx支付
 */
suspend fun requestWxPay(params: RequestWxPayParams,callback: SendReqCallback,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        sendReq(PayReq().apply {
            this.appId = params.appId
            this.partnerId = params.partnerId
            this.prepayId= params.prepayId
            this.packageValue = params.packageValue
            this.nonceStr= params.nonceStr
            this.timeStamp= params.timeStamp
            this.sign= params.sign
            this.transaction = transaction
        })
        callBackMap[transaction] = callback
    }
}

/**
 * 拉起小程序页面
 * @param userName 小程序原始id
 * @param path  拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
 * @param miniprogramType  可选打开 开发版，体验版和正式版
 */
suspend fun launchMiniProgram(userName:String,path:String?,miniprogramType:WeChatMiniProgramType,callback: SendReqCallback,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        sendReq(WXLaunchMiniProgram.Req().apply {
            this.userName = userName
            this.path = path
            this.miniprogramType = miniprogramType.type
            this.transaction = transaction
        })
        callBackMap[transaction] = callback
    }
}

/**
 * 是否安装了微信
 */
val isWXAppInstalled :Boolean = api.isWXAppInstalled

/**
 * 打开微信app
 */
val openWXApp :Boolean = api.openWXApp()


/**
 * 分享文本
 */
suspend fun shareTextToWechat(params:ShareTextParams, platform:WeChatPlatform, callback: SendReqCallback, transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        val msg = assembleMsg(WXTextObject().apply {
            this.text =params.text
        },null,params.description?:params.text,null)
        val request = assembleSendMessageRequest(msg,platform.scene,transaction,null)
        sendReq(request)
        callBackMap[transaction] = callback
    }
}
suspend fun shareTextToWechat(text:String, platform:WeChatPlatform, callback: SendReqCallback, transaction: String = randomTransaction()){
    shareTextToWechat(ShareTextParams(text,text),platform,callback,transaction)
}

/**
 * 分享图片
 */
suspend fun shareImageToWechat(params:ShareImageParams,platform:WeChatPlatform,callback: SendReqCallback,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        val shareImageBitmap = try {
            params.shareImageBitmap
        } catch (e: Exception) {
            callback.onFailed(e.message)
            return@withContext
        }
        val thumbImageBitmap = try {
            params.thumbImage?.thumbImageBitmap?:shareImageBitmap
        } catch (e: Exception) {
            shareImageBitmap
        }
        val msg = assembleMsg(WXImageObject().apply {

            //使用imagePath而不使用imageData是因为,imageData数据过大时,和wx进程通信时,会超过binder能够携带的数据最大值
//            this.imageData = shareImageBitmap.toByteArray()

            val sharedImageFile = File(shareImageBitmap.saveLocally(File(cacheDirPath,"wx_share_image_cache")))
            if(sharedImageFile.length()>=shareImageMaxSizeInKb*1024){  //分享的图片大小大于shareImageMaxSizeInKb,要进行压缩
                this.imagePath = compressImageFile(sharedImageFile, shareImageMaxSizeInKb*1024).absolutePath
            }else{
                this.imagePath = sharedImageFile.absolutePath
            }

        },null,null, getCompressedThumbData(thumbImageBitmap))
        val request = assembleSendMessageRequest(msg,platform.scene,transaction,null)
        sendReq(request)
        callBackMap[transaction] = callback
    }
}
suspend fun shareImageToWechat(imageBitmap:Bitmap,platform:WeChatPlatform,callback: SendReqCallback,transaction: String = randomTransaction()){
    shareImageToWechat(ShareImageParams(imageBitmap = imageBitmap),platform,callback,transaction)
}
suspend fun shareImage(imagePath:String,platform:WeChatPlatform,callback: SendReqCallback,transaction: String = randomTransaction()){
    shareImageToWechat(ShareImageParams(imageBitmap = null, imagePath = imagePath),platform,callback,transaction)
}

/**
 * 分享音乐
 */
suspend fun shareMusicToWechat(params:ShareMusicParams,platform:WeChatPlatform,callback: SendReqCallback,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        val thumbImageBitmap = try {
            params.thumbImage.thumbImageBitmap
        } catch (e: Exception) {
            callback.onFailed(e.message)
            return@withContext
        }
        val msg = assembleMsg(WXMusicObject().apply {
            this.musicUrl = params.musicUrl
            this.musicLowBandUrl = params.musicLowBandUrl
            this.musicDataUrl = params.musicDataUrl
            this.musicLowBandDataUrl = params.musicLowBandDataUrl
        },params.title,params.description, getCompressedThumbData(thumbImageBitmap))
        val request = assembleSendMessageRequest(msg,platform.scene,transaction,null)
        sendReq(request)
        callBackMap[transaction] = callback
    }
}

/**
 * 分享视频
 */
suspend fun shareVideoToWechat(params:ShareVideoPrams,platform:WeChatPlatform,callback: SendReqCallback,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        val thumbImageBitmap = try {
            params.thumbImage.thumbImageBitmap
        } catch (e: Exception) {
            callback.onFailed(e.message)
            return@withContext
        }
        val msg = assembleMsg(WXVideoObject().apply {
            this.videoUrl = params.videoUrl
            this.videoLowBandUrl = params.videoLowBandUrl
        },params.title,params.description, getCompressedThumbData(thumbImageBitmap))
        val request = assembleSendMessageRequest(msg,platform.scene,transaction,null)
        sendReq(request)
        callBackMap[transaction] = callback
    }
}

/**
 * 分享网页
 */
suspend fun shareWebPageToWechat(params:ShareWebPageParams,platform:WeChatPlatform,callback: SendReqCallback,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        val thumbImageBitmap = try {
            params.thumbImage.thumbImageBitmap
        } catch (e: Exception) {
            callback.onFailed(e.message)
            return@withContext
        }
        val msg = assembleMsg(WXWebpageObject().apply {
            this.webpageUrl = params.webpageUrl
        },params.title,params.description, getCompressedThumbData(thumbImageBitmap))
        val request = assembleSendMessageRequest(msg,platform.scene,transaction,null)
        sendReq(request)
        callBackMap[transaction] = callback
    }
}

/**
 * 分享小程序
 */
suspend fun shareMiniProgramToWechat(params:ShareMiniProgramParams,platform:WeChatPlatform,callback: SendReqCallback,transaction: String = randomTransaction()){
    withContext(Dispatchers.IO){
        val thumbImageBitmap = try {
            params.thumbImage.thumbImageBitmap
        } catch (e: Exception) {
            callback.onFailed(e.message)
            return@withContext
        }
        val msg = assembleMsg(WXMiniProgramObject().apply {
            this.webpageUrl = params.webpageUrl
            this.userName = params.userName
            this.path = params.path
            this.withShareTicket = params.withShareTicket
            this.miniprogramType = params.miniprogramType.type
        },params.title,params.description, getCompressedThumbData(thumbImageBitmap,true))
        val request = assembleSendMessageRequest(msg,platform.scene,transaction,null)
        sendReq(request)
        callBackMap[transaction] = callback
    }
}

//todo 音乐视频类型分享