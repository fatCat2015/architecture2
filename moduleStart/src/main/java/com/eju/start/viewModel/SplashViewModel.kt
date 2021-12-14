package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.appbase.persistence.HAS_AGREED_PRIVACY_POLICY
import com.eju.architecture.core.BaseViewModel
import com.eju.start.repository.StartRepository
import com.eju.tools.finishAllActivities
import com.eju.tools.saveBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val startRepository: StartRepository):BaseViewModel() {

    val showPrivacyPolicy = MutableLiveData<Int>()

    val toMain = MutableLiveData<Int>()

    val toLogin = MutableLiveData<Int>()

    val toGuide = MutableLiveData<Int>()


    fun countdownToPage(){
        launch (showLoading = false){
            val hasAgreedPrivacyPolicy = startRepository.hasAgreedPrivacyPolicy()
            Timber.i("hasAgreedPrivacyPolicy:${hasAgreedPrivacyPolicy}")
            if(hasAgreedPrivacyPolicy){
                val startTime = System.currentTimeMillis()
                startRepository.init()
                val initTime = System.currentTimeMillis() -startTime
                Timber.i("init complete initTime: ${initTime}")
                val remainedTime = delay_time - initTime
                delay(if(remainedTime<=0) 0 else remainedTime)
                val showGuide = startRepository.verifyShowGuidePage()
                val isLogged = startRepository.verifyIfHasLogged()
                val firstLaunchNewVersion =  startRepository.verifyIfFirstLaunchNewVersion()
                Timber.i("countdownToPage showGuide:${showGuide};isLogged:${isLogged};firstLaunchNewVersion:${firstLaunchNewVersion}")
                when{
                    showGuide -> toGuide.postValue(1)
                    isLogged -> toMain.postValue(1)
                    else -> toLogin.postValue(1)
                }
            }else{
                showPrivacyPolicy.postValue(1)
            }

        }
    }

    fun onDisagreePrivacyPolicy(){
        startRepository.setAgreedPrivacyPolicy(false)
    }

    fun onAgreePrivacyPolicy(){
        startRepository.setAgreedPrivacyPolicy(true)
    }

    fun onPrivacyPolicyDialogDismiss(){
        if(startRepository.hasAgreedPrivacyPolicy()){
            countdownToPage()
        }else{
            finishAllActivities()
        }
    }


    companion object{
        const val delay_time = 1000L
    }


}