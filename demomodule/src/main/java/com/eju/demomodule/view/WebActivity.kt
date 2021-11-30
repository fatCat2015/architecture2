package com.eju.demomodule.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.base.AppBaseWebActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.core.ITitleView
import com.eju.demomodule.R
import com.eju.demomodule.databinding.ActivityWebBinding
import com.eju.demomodule.titles.WebTitle
import com.eju.tools.safeIntentExtra
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import timber.log.Timber

@Route(path = PagePath.DemoModule.WebPage)
class WebActivity:AppBaseWebActivity<ActivityWebBinding>() {


    private val webUrl by safeIntentExtra<String>("url")

    private lateinit var titleView:WebTitle

    override fun observe() {

    }

    override fun <V : ViewBinding> titleView(): ITitleView<V>? {
        return WebTitle(this).also { titleView = it } as ITitleView<V>
    }

    override fun webParentView(): ViewGroup {
        return binding.flFragmentContainer
    }

    override fun getUrl(): String {
        return webUrl
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        super.afterCreate(savedInstanceState)
        //todo anything
    }

    override fun webViewClient(): WebViewClient {
        return object :WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Timber.i("onPageStarted ")
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Timber.i("onPageFinished ")
            }
        }
    }

    override fun webChromeClient(): WebChromeClient {
        return object:WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                titleView.binding.tvTitle.text = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Timber.i("onProgressChanged ${newProgress} ")
            }
        }
    }


}