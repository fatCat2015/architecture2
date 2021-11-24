package com.eju.aop.login

import android.app.Activity
import android.content.Context

interface ILoginCheck {
    /**
     * 判断登录状态
     * @param context
     * @return
     */
    fun isLoggedIn(): Boolean

    fun startLoginActivity()

}