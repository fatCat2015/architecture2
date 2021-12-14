package com.eju.appbase.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.R
import com.eju.appbase.persistence.LOGGED_MOBILE
import com.eju.appbase.persistence.USER_ID
import com.eju.appbase.persistence.USER_TOKEN
import com.eju.appbase.router.service.LoginService
import com.eju.appbase.service.ApiException
import com.eju.appbase.service.ServiceErrorCode
import com.eju.architecture.core.IExceptionHandler
import com.eju.tools.application
import com.eju.tools.showToast

class ExceptionHandlerImpl(
    private val activity: FragmentActivity?,
    private val fragment: Fragment?,
    private val isFragment: Boolean

):IExceptionHandler {

    constructor(activity: FragmentActivity):this(activity,null,false)

    constructor(fragment: Fragment):this(null,fragment,true)

    private val context: Context? by lazy {
        if(isFragment) fragment?.activity else activity
    }

    private val fragmentManager: FragmentManager? by lazy {
        if(isFragment) fragment?.childFragmentManager else activity?.supportFragmentManager
    }

    private val lifecycleOwner: LifecycleOwner? by lazy {
        if(isFragment) fragment else activity
    }

    override fun handleException(throwable: Throwable?) {
        if(throwable is ApiException){
            val apiMsg:String? = throwable?.message
            when(throwable.code){
                ServiceErrorCode.BE_KICKEd_OUT ->{
                    showToast(if(apiMsg.isNullOrEmpty()) context?.getString(R.string.be_kicked_out) else apiMsg)
                    onForcedLogout()
                }
                ServiceErrorCode.TOKEN_OUT_OF_DATE ->{
                    showToast(if(apiMsg.isNullOrEmpty()) context?.getString(R.string.token_out_of_date) else apiMsg)
                    onForcedLogout()
                }
                else->{
                    showToastLifecycleAware(throwable?.message)
                }
            }
        }else{
            showToastLifecycleAware(throwable?.message)
        }
    }

    private fun onForcedLogout(){
        ARouter.getInstance().navigation(LoginService::class.java)?.let { loginService ->
            loginService.onLogout(USER_ID, USER_TOKEN, LOGGED_MOBILE)
            loginService.removeRequestHeaders(USER_ID, USER_TOKEN)
            loginService.toLogin()
        }
    }

    private fun showToast(msg:CharSequence?){
        application.showToast(msg,Toast.LENGTH_LONG)
    }

    private fun showToastLifecycleAware(msg:CharSequence?){
        lifecycleOwner?.let {
            showToast(context,it,msg, Toast.LENGTH_SHORT)
        }
    }

    companion object{

    }
}