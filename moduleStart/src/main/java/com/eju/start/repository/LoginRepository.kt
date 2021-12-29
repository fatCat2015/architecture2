package com.eju.start.repository

import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.persistence.LOGGED_MOBILE
import com.eju.appbase.persistence.USER_ID
import com.eju.appbase.persistence.USER_TOKEN
import com.eju.architecture.core.BaseRepository
import com.eju.start.api.LoginService
import com.eju.appbase.router.service.LoginService as ARouterLoginService
import com.eju.start.api.bean.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginService: LoginService):BaseRepository() {

    init {
        Timber.i("LoginRepository init loginService:${loginService}")
    }

    suspend fun login(mobile:String,code:String): User {
        delay(1500)
        return loginService.simulateLogin("58",code).result.apply {
            this.userId = "963"
            this.userToken = "token12345678910111213"
        }
    }

    suspend fun onLogin(user: User){
        withContext(Dispatchers.IO){
            ARouter.getInstance().navigation(ARouterLoginService::class.java).let { loginService->
                loginService.onLogin(
                    USER_ID to user.userId,
                    USER_TOKEN to user.userToken,
                    LOGGED_MOBILE to user.mobile,
                )
                loginService.addRequestHeaders(
                    USER_ID to user.userId,
                    USER_TOKEN to user.userToken,)
            }

        }
    }

}