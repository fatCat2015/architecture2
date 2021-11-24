package com.eju.appbase.base

import androidx.viewbinding.ViewBinding
import com.eju.architecture.core.BaseLazyLoadFragment
import com.eju.architecture.core.IExceptionHandler
import com.eju.architecture.core.IViewBehavior
import timber.log.Timber

abstract class AppBaseLazyLoadFragment<V:ViewBinding>:BaseLazyLoadFragment<V>() {

    private val viewBehaviorImpl: IViewBehavior by lazy {
        ViewBehaviorImpl(this).apply {
            Timber.i("IViewBehavior init")
        }
    }

    private val exceptionHandler: IExceptionHandler by lazy {
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

    override fun finishPage() {
        viewBehaviorImpl.finishPage()
    }

    override fun handleException(throwable: Throwable?) {
        exceptionHandler.handleException(throwable)

    }

}