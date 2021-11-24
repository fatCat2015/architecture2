package com.eju.start.repository

import com.eju.appbase.service.BaseResult
import com.eju.appbase.service.ServiceErrorCode
import com.eju.architecture.core.BaseRepository
import com.eju.appbase.api.LoginService
import com.eju.appbase.api.bean.User
import com.eju.appbase.persistence.GUIDE_INDEX
import com.eju.appbase.persistence.IS_LOGGED
import com.eju.appbase.persistence.VERSION_CODE
import com.eju.start.BuildConfig
import com.eju.tools.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SplashRepository @Inject constructor():BaseRepository() {

    suspend fun verifyShowGuidePage():Boolean{
        return withContext(Dispatchers.IO){
            val localGuideIndex= fetchInt(GUIDE_INDEX,0)
            localGuideIndex != BuildConfig.guideIndex
        }
    }

    suspend fun verifyIfHasLogged():Boolean = withContext(Dispatchers.IO){
        fetchBoolean(IS_LOGGED,false)
    }

    suspend fun verifyIfFirstLaunchNewVersion():Boolean = withContext(Dispatchers.IO){
        fetchInt(VERSION_CODE,0)!= versionCode.also {
            saveInt(VERSION_CODE, it?:0)
        }
    }
}