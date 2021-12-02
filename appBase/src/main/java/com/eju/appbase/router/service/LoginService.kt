package com.eju.appbase.router.service

import com.alibaba.android.arouter.facade.template.IProvider
import java.io.Serializable

interface LoginService:IProvider {

    /**
     * 登录后保存登录信息
     */
    fun onLogin(vararg dataToSaved:Pair<String,String>)

    /**
     * 登出后清楚登录信息
     */
    fun onLogout(vararg dataKeysToCleared:String)

    /**
     * 去登录页面
     */
    fun toLogin()

    /**
     * 添加公共请求头
     */
    fun addRequestHeaders(vararg heads:Pair<String,String>)

    /**
     * 删除公共请求头
     */
    fun removeRequestHeaders(vararg headKeys:String)
}

