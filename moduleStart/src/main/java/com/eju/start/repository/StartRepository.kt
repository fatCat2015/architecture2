package com.eju.start.repository

import com.eju.analysis.AnalysisInitializer
import com.eju.architecture.core.BaseRepository
import com.eju.appbase.persistence.GUIDE_INDEX
import com.eju.appbase.persistence.HAS_AGREED_PRIVACY_POLICY
import com.eju.appbase.persistence.IS_LOGGED
import com.eju.appbase.persistence.VERSION_CODE
import com.eju.start.BuildConfig
import com.eju.startup.AppInitializer
import com.eju.tools.*
import com.eju.tools.initializer.NetworkInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StartRepository @Inject constructor():BaseRepository() {

    fun hasAgreedPrivacyPolicy():Boolean = fetchBoolean(HAS_AGREED_PRIVACY_POLICY,false)

    /**
     * 进行一些必须在同意隐私政策之后才能进行的初始化
     */
    suspend fun init(){
        AppInitializer.initializeComponentSuspend(AnalysisInitializer::class.java)
    }

    fun setAgreedPrivacyPolicy(agreed:Boolean){
        saveBoolean(HAS_AGREED_PRIVACY_POLICY,agreed)
    }

    fun verifyShowGuidePage():Boolean{
        val localGuideIndex= fetchInt(GUIDE_INDEX,0)
        return localGuideIndex != BuildConfig.guideIndex
    }

    fun verifyIfHasLogged():Boolean = fetchBoolean(IS_LOGGED,false)

    fun verifyIfFirstLaunchNewVersion():Boolean = fetchInt(VERSION_CODE,0)!= versionCode.also {
        saveInt(VERSION_CODE, it?:0)
    }
}

