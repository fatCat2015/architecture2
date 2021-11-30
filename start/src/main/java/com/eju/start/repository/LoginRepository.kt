package com.eju.start.repository

import com.eju.appbase.service.BaseResult
import com.eju.appbase.service.ServiceErrorCode
import com.eju.architecture.core.BaseRepository
import com.eju.start.api.LoginService
import com.eju.start.api.bean.User
import com.eju.appbase.persistence.IS_LOGGED
import com.eju.appbase.persistence.LOGGED_USER_INFO
import com.eju.tools.saveBoolean
import com.eju.tools.saveSerializable
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
        return BaseResult(
            code = ServiceErrorCode.SUCCESS,
            message = "success",
            data = User("id","sck","token${code}",mobile)
        ).result
    }

    suspend fun saveUserInfo(user: User){
        withContext(Dispatchers.IO){
            saveSerializable(LOGGED_USER_INFO,user)
            saveBoolean(IS_LOGGED,true)
        }
    }
}