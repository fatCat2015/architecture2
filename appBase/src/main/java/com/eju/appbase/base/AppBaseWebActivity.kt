package com.eju.appbase.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.eju.appbase.BuildConfig
import com.eju.appbase.R
import com.eju.tools.application
import com.just.agentweb.*

abstract class AppBaseWebActivity<V:ViewBinding>:AppBaseActivity<V>(),WebConfig,WebAction {

    protected lateinit var agentWeb:AgentWeb

    private val agentWebCreator:AgentWebCreator by lazy {
        DefaultAgentCreator(this,this as WebConfig)
    }

    init {
        if(BuildConfig.DEBUG){
            WebView.setWebContentsDebuggingEnabled(true)
            AgentWebConfig.debug()
        }
    }


    override fun observe() {

    }


    override fun afterCreate(savedInstanceState: Bundle?) {
        agentWeb = agentWebCreator.create()
        getWebView().overScrollMode = View.OVER_SCROLL_NEVER
    }


     override fun indicatorColorResId(): Int {
         return R.color.colorPrimary
     }

     override fun indicatorHeightInDp(): Int {
         return 2
     }

     override fun webViewClient(): WebViewClient {
         return WebViewClient()
     }

     override fun webChromeClient(): WebChromeClient {
         return WebChromeClient()
     }

     override fun errorViewLayoutId(): Int {
         return R.layout.layout_web_main_frame_error_view
     }

     override fun refreshBtnViewId(): Int {
         return -1  //-1表示点击整个布局都刷新
     }

     override fun additionalHttpHeaders(): Map<String, String> {
         return emptyMap()
     }

    override fun getWebView(): WebView {
        return agentWeb.webCreator.webView
    }

    override fun refresh() {
        agentWeb.urlLoader.reload()
    }

    override fun currentUrl(): String? {
        return getWebView().url
    }

    override fun clearCache() {
        agentWeb.clearWebCache()
    }

    override fun pause() {
        agentWeb.webLifeCycle.onPause()
    }

    override fun resume() {
        agentWeb.webLifeCycle.onResume()
    }

    override fun destroy() {
        agentWeb.webLifeCycle.onDestroy()
    }

    /**
     * 需要activity中onKeyDown中调用
     */
    override fun handleKeyEvent(keycode: Int, keyEvent: KeyEvent?): Boolean {
        return agentWeb.handleKeyEvent(keycode, keyEvent)
    }

    /**
     * 需要activity中onBackPressed中调用
     */
    override fun handleback(): Boolean {
        return agentWeb.back()
    }

    override fun onPause() {
        pause()
        super.onPause()
    }

    override fun onResume() {
        resume()
        super.onResume()
    }

    override fun onDestroy() {
        destroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(handleKeyEvent(keyCode,event)){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if(!handleback()){
            super.onBackPressed()
        }
    }

 }

interface AgentWebCreator{
    fun create():AgentWeb
}

class DefaultAgentCreator(private val activity:Activity?,
                          private val fragment:Fragment?,
                          private val isFragment: Boolean,
                          private val webConfig: WebConfig):AgentWebCreator{

    constructor(fragment: Fragment,webConfig:WebConfig):this(null,fragment,true,webConfig)

    constructor(activity: Activity,webConfig:WebConfig):this(activity,null,false,webConfig)

    private val context:Context get() = (if(isFragment) fragment?.activity else activity)?: application

    override fun create(): AgentWeb {
        return (if(isFragment) AgentWeb.with(fragment!!) else AgentWeb.with(activity!!))
            .setAgentWebParent(webConfig.webParentView(),ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
            .useDefaultIndicator(ContextCompat.getColor(context,webConfig.indicatorColorResId()),webConfig.indicatorHeightInDp())
            .setWebViewClient(webConfig.webViewClient())
            .setWebChromeClient(webConfig.webChromeClient())
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)  //严格模式:Android 4.2.2 以下会放弃注入对象
            .setMainFrameErrorView(webConfig.errorViewLayoutId(),webConfig.refreshBtnViewId())
            .apply {
                val httpHeaders = webConfig.additionalHttpHeaders()
                if(!httpHeaders.isNullOrEmpty()){
                    additionalHttpHeader(webConfig.getUrl(),httpHeaders)
                }
            }
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用
//            .useMiddlewareWebChrome()
//            .useMiddlewareWebClient()
//            .setPermissionInterceptor { url, permissions, action ->
//                requestPermissions(permissions = permissions,deniedCallback = {},allGrantedCallback = {
//
//                })
//                true
//            }
//           .setAgentWebWebSettings()
//           .setAgentWebUIController()
            .interceptUnkownUrl() //拦截找不到相关页面的Url
            .createAgentWeb()
            .ready()
            .go(webConfig.getUrl())
    }

}

interface WebConfig{

    fun webParentView():ViewGroup

    fun getUrl():String

    fun indicatorColorResId() :Int

    fun indicatorHeightInDp() :Int

    fun webViewClient() : WebViewClient

    fun webChromeClient() : WebChromeClient

    fun errorViewLayoutId():Int

    fun refreshBtnViewId():Int

    fun additionalHttpHeaders():Map<String,String>
}

interface WebAction{

    fun getWebView():WebView

    fun refresh()

    fun currentUrl():String?

    fun clearCache()

    fun pause()

    fun resume()

    fun destroy()

    fun handleKeyEvent(keycode:Int,keyEvent: KeyEvent?):Boolean

    fun handleback(): Boolean
}

