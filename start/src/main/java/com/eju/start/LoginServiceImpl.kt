package com.eju.start

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.persistence.IS_LOGGED
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.service.LoginService
import com.eju.appbase.router.service.ServicePath
import com.eju.retrofit.RetrofitModule
import com.eju.tools.*
import timber.log.Timber


@Route(path = ServicePath.loginService)
class LoginServiceImpl: LoginService {

    private var context:Context? = null

    override fun onLogin(vararg dataToSaved: Pair<String, String>) {
        Timber.i("onLogin ${dataToSaved}")
        saveBoolean(IS_LOGGED,true)
        dataToSaved.forEach {
            saveString(it.first,it.second)
        }
    }

    override fun onLogout(vararg dataKeysToCleared: String) {
        saveBoolean(IS_LOGGED,false)
        removeValueForKeys(*dataKeysToCleared)
    }

    override fun toLogin() {
        ARouter.getInstance().build(PagePath.Start.Login)
//            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) 这样处理会导致黑屏.改为在登陆页面打开之后调用 finishAllActivitiesExceptTop
            .navigation(context)
    }

    override fun addRequestHeaders(vararg headers: Pair<String, String>) {
        RetrofitModule.addRequestHeader(*headers)
    }

    override fun removeRequestHeaders(vararg headKeys: String) {
        RetrofitModule.removeQuestHeaders(*headKeys)
    }

    override fun init(context: Context?) {
        Timber.i("LoginService init ${context}")
        this.context = context
    }

}
