package com.eju.aop.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.eju.aop.annotation.CheckLogin
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import timber.log.Timber

object LoginCheckProxy : ILoginCheck {

    const val DEFAULT_DELAY_TIME=300

    private var loginCheck: ILoginCheck? = null

    var currentProceedingJoinPoint: ProceedingJoinPoint?=null

    private val mainHandler=Handler(Looper.getMainLooper())

    /**
     *
     * @param loginPageClz  登录页面Class
     * @param loginCheck    主要用于判断是否登录
     */
    @Synchronized
    fun initLoginCheck(loginCheck: ILoginCheck) {
        this.loginCheck = loginCheck
    }

    override fun isLoggedIn(): Boolean {
        return loginCheck?.isLoggedIn()?:false
    }

    override fun startLoginActivity() {
        loginCheck?.startLoginActivity()
    }


    fun onLoginSuccess(){
        currentProceedingJoinPoint?.let {
            var delayTime= getDelayTime(it)
            mainHandler.postDelayed({
                currentProceedingJoinPoint?.proceed()
                currentProceedingJoinPoint=null
            },delayTime.toLong())
        }

    }


    private fun getDelayTime(joinPoint: JoinPoint): Int {
        val methodSignature =
            joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val annotation = method.getAnnotation(CheckLogin::class.java)
        return annotation?.value?:DEFAULT_DELAY_TIME
    }

}

