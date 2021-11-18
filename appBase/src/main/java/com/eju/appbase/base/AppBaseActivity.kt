package com.eju.appbase.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.eju.appbase.R
import com.eju.architecture.base.IViewBehavior
import com.eju.architecture.base.BaseActivity
import com.eju.architecture.base.IExceptionHandler
import com.eju.architecture.base.ITitleView
import com.gyf.immersionbar.ktx.immersionBar
import timber.log.Timber

abstract class AppBaseActivity<B:ViewBinding>:BaseActivity<B>() {

    private val viewBehaviorImpl: IViewBehavior by lazy {
        ViewBehaviorImpl(this).apply {
            Timber.i("IViewBehavior init")
        }
    }

    private val exceptionHandler:IExceptionHandler by lazy {
        ExceptionHandlerImpl(this).apply {
            Timber.i("IExceptionHandler init")
        }
    }

    override fun showLoading(msg: CharSequence?) {
        viewBehaviorImpl.showLoading(msg)
    }

    override fun showLoading(resId: Int) {
        viewBehaviorImpl.showLoading(resId)
    }

    override fun hideLoading() {
        viewBehaviorImpl.hideLoading()
    }

    override fun showToast(msg: CharSequence?, duration: Int) {
        viewBehaviorImpl.showToast(msg,duration)
    }

    override fun showToast(resId: Int?, duration: Int) {
        viewBehaviorImpl.showToast(resId,duration)
    }

    override fun showSnack(msg: CharSequence?, duration: Int, code: Int) {
        viewBehaviorImpl.showSnack(msg,duration,code)
    }

    override fun showSnack(resId: Int?, duration: Int, code: Int) {
        viewBehaviorImpl.showSnack(resId,duration,code)
    }

    override fun handleException(throwable: Throwable?) {
        exceptionHandler.handleException(throwable)

    }

    override fun <V : ViewBinding> titleView(): ITitleView<V>? {
        return CommonTitleView(this) as ITitleView<V>
    }

    override fun setImmersionBar() {
        super.setImmersionBar()
        immersionBar {
            findViewById<View>(R.id.app_title)?.let{
                titleBar(it)
            }
        }
    }



}