package com.eju.start.repository

import com.eju.analysis.AnalysisInitializer
import com.eju.architecture.core.BaseRepository
import com.eju.appbase.persistence.GUIDE_INDEX
import com.eju.appbase.persistence.HAS_AGREED_PRIVACY_POLICY
import com.eju.appbase.persistence.IS_LOGGED
import com.eju.appbase.persistence.VERSION_CODE
import com.eju.persistence.persistenceBoolean
import com.eju.persistence.persistenceInt
import com.eju.start.BuildConfig
import com.eju.startup.AppInitializer
import com.eju.tools.*
import com.eju.tools.initializer.NetworkInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StartRepository @Inject constructor():BaseRepository() {


    var isAgreedPrivacyPolicy :Boolean by persistenceBoolean(HAS_AGREED_PRIVACY_POLICY)

    var localGuideIndex :Int by persistenceInt(GUIDE_INDEX)

    var isLogged :Boolean by persistenceBoolean(IS_LOGGED)

    var localVersionCode :Int by persistenceInt(VERSION_CODE)

    /**
     * 进行一些必须在同意隐私政策之后才能进行的初始化
     */
    suspend fun init(){
        AppInitializer.initializeComponentSuspend(AnalysisInitializer::class.java)
    }


    fun verifyShowGuidePage():Boolean{
        return localGuideIndex != BuildConfig.guideIndex
    }


    fun verifyIfFirstLaunchNewVersion():Boolean = localVersionCode!= versionCode.also {
        localVersionCode = it?:0
    }
}

