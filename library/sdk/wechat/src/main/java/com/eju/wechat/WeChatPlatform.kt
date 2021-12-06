package com.eju.wechat

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject

enum class WeChatPlatform (val scene:Int){
    /**
     * 微信好友
     */
    SESSION(SendMessageToWX.Req.WXSceneSession),

    /**
     * 微信朋友圈
     */
    TIMELINE(SendMessageToWX.Req.WXSceneTimeline),

    /**
     * 微信收藏
     */
    FAVORITE(SendMessageToWX.Req.WXSceneFavorite),
}

enum class WeChatMiniProgramType (val type:Int){
    /**
     * 测试版
     */
    TEST(WXMiniProgramObject.MINIPROGRAM_TYPE_TEST),

    /**
     * 预览版
     */
    PREVIEW(WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW),

    /**
     * 正式版
     */
    RELEASE(WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE),

}