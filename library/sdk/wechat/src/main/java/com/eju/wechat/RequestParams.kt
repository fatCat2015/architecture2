package com.eju.wechat

import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class RequestWxPayParams(
    val appId :String,
    val partnerId :String,
    val prepayId:String,
    val packageValue:String,
    val nonceStr:String,
    val timeStamp:String,
    val sign:String,
)

/**
 * text必填
 * description非内容字段(wx页面选择好友之后确定弹框,会展示description),null时使用text作为description
 */
data class ShareTextParams(
    val text:String,  //长度需大于 0 且不超过 10KB
    val description:String? = null  //限制长度不超过 1KB
)

/**
 * 本地图片,参数3选一
 */
data class ShareImageParams(
    val imageBitmap:Bitmap?,
    val imagePath:String? = null,     //对应图片内容大小不超过 10MB
    val imageData:ByteArray? = null,  //内容大小不超过 10MB
    val thumbImage: ThumbImage? = null //缩略图,为null时使用分享的图片生成缩略图 限制内容大小不超过 32KB
){
    val shareImageBitmap:Bitmap
        get() {
            return imageBitmap?.let {
                it
            }?:imageData?.let {
                BitmapFactory.decodeByteArray(it,0,it.size)?:throw NullPointerException("BitmapFactory.decodeByteArray() return null,thumb image is null")
            }?:imagePath?.let {
                BitmapFactory.decodeFile(it)?:throw NullPointerException("BitmapFactory.decodeFile() return null,thumb image is null")
            }?:throw NullPointerException("thumb image is null")
        }
}


/**
 * 对应WXMediaMessage.thumbData,参数三选一
 * WXMediaMessage.thumbData:ByteArray 限制内容大小不超过 32KB
 * 该module会处理缩略图的大小检测和压缩
 */
data class ThumbImage(
    val imageBitmap:Bitmap?,
    val imagePath:String? = null,
    val imageData:ByteArray? = null
){
    val thumbImageBitmap:Bitmap
        get() {
            return imageBitmap?.let {
                it
            }?:imageData?.let {
                BitmapFactory.decodeByteArray(it,0,it.size)?:throw NullPointerException("BitmapFactory.decodeByteArray() return null,thumb image is null")
            }?:imagePath?.let {
                BitmapFactory.decodeFile(it)?:throw NullPointerException("BitmapFactory.decodeFile() return null,thumb image is null")
            }?:throw NullPointerException("thumb image is null")
        }
}

/**
 * 注意：musicUrl 和 musicLowBandUrl 不能同时为空
 * todo 参数不太理解,目前没有使用过music分享
 */
data class ShareMusicParams(
    val musicUrl:String?,  //音频网页的 URL 地址	限制长度不超过 10KB
    val musicLowBandUrl:String? = null, //供低带宽环境下使用的音频网页 URL 地址	 限制长度不超过 10KB
    val musicDataUrl:String? = null,    //音频数据的 URL 地址 限制长度不超过 10KB
    val musicLowBandDataUrl:String? = null, //供低带宽环境下使用的音频数据 URL 地址 限制长度不超过 10KB
    val title:String,   //音乐标题  限制长度不超过 512Bytes
    val description:String,  //音乐描述  限制长度不超过 1KB
    val thumbImage: ThumbImage  //音乐缩略图
)

/**
 * videoUrl 和 videoLowBandUrl 不能同时为空
 */
data class ShareVideoPrams(
    val videoUrl:String?,   //视频链接 限制长度不超过 10KB
    val videoLowBandUrl:String?,  //供低带宽的环境下使用的视频链接 限制长度不超过 10KB
    val title:String,   //视频标题  限制长度不超过 512Bytes
    val description:String,  //视频描述  限制长度不超过 1KB
    val thumbImage: ThumbImage  //视频封面缩略图
)

data class ShareWebPageParams(
    val webpageUrl:String,   //html 链接	限制长度不超过 10KB
    val title:String,   //网页标题  限制长度不超过 512Bytes
    val description:String,  //网页描述  限制长度不超过 1KB
    val thumbImage: ThumbImage  //封面缩略图
)

data class ShareMiniProgramParams(
    val webpageUrl:String,   //兼容低版本的网页链接	限制长度不超过 10KB
    val userName:String, //小程序的原始 id
    val path:String, //小程序的 path
    val withShareTicket:Boolean, //是否使用带 shareTicket 的分享 最低客户端版本要求：6.5.13  todo 不是很懂
    val miniprogramType:WeChatMiniProgramType, //小程序的类型
    val title:String,   //小程序消息标题  限制长度不超过 512Bytes
    val description:String,  //小程序消息描述  限制长度不超过 1KB
    val thumbImage: ThumbImage  //小程序消息封面图片，小于128k
)
