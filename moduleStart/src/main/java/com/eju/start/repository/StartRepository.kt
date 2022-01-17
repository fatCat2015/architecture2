package com.eju.start.repository

import com.eju.architecture.core.BaseRepository
import com.eju.appbase.persistence.GUIDE_INDEX
import com.eju.appbase.persistence.HAS_AGREED_PRIVACY_POLICY
import com.eju.appbase.persistence.IS_LOGGED
import com.eju.appbase.persistence.VERSION_CODE
import com.eju.persistence.persistenceBoolean
import com.eju.persistence.persistenceInt
import com.eju.start.BuildConfig
import com.eju.start.api.bean.SplashScreenAd
import com.eju.tools.*
import kotlinx.coroutines.delay
import javax.inject.Inject

class StartRepository @Inject constructor():BaseRepository() {


    var isAgreedPrivacyPolicy :Boolean by persistenceBoolean(HAS_AGREED_PRIVACY_POLICY)

    var localGuideIndex :Int by persistenceInt(GUIDE_INDEX)

    var isLogged :Boolean by persistenceBoolean(IS_LOGGED)

    var localVersionCode :Int by persistenceInt(VERSION_CODE)

    fun verifyShowGuidePage():Boolean{
        return localGuideIndex != BuildConfig.guideIndex
    }

    fun verifyIfFirstLaunchNewVersion():Boolean = localVersionCode!= versionCode.also {
        localVersionCode = it?:0
    }

    suspend fun querySplashScreenAd():SplashScreenAd{
        delay(1000)
        return SplashScreenAd(true,"https://img2.baidu.com/it/u=111900514,1075415575&fm=253&fmt=auto&app=120&f=JPEG?w=680&h=629")
    }
}

