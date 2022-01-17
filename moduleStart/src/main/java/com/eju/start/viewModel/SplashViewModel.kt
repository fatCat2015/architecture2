package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.appbase.persistence.HAS_AGREED_PRIVACY_POLICY
import com.eju.architecture.core.BaseViewModel
import com.eju.start.api.bean.SplashScreenAd
import com.eju.start.repository.StartRepository
import com.eju.tools.finishAllActivities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val startRepository: StartRepository):BaseViewModel() {

    val splashScreenAd = MutableLiveData<SplashScreenAd>()

    val showPrivacyPolicy = MutableLiveData<Int>()

    val toMain = MutableLiveData<Int>()

    val toLogin = MutableLiveData<Int>()

    val toGuide = MutableLiveData<Int>()


    private fun querySplashScreenAd(){
        launch (showLoading = false){
            splashScreenAd.postValue(startRepository.querySplashScreenAd().also {
                Timber.i("querySplashScreenAd-->${it}")
            })
        }
    }

    fun verifyPrivacyPolicy(){
        launch (showLoading = false){
            val isAgreedPrivacyPolicy = startRepository.isAgreedPrivacyPolicy
            Timber.i("hasAgreedPrivacyPolicy:${isAgreedPrivacyPolicy}")
            if(isAgreedPrivacyPolicy){
                querySplashScreenAd()
                delay(delay_time)
                countdownToPage()
            }else{
                showPrivacyPolicy.postValue(1)
            }

        }
    }

    private fun countdownToPage() {
        val showGuide = startRepository.verifyShowGuidePage()
        val isLogged = startRepository.isLogged
        val firstLaunchNewVersion = startRepository.verifyIfFirstLaunchNewVersion()
        Timber.i("countdownToPage showGuide:${showGuide};isLogged:${isLogged};firstLaunchNewVersion:${firstLaunchNewVersion}")
        when {
            showGuide -> toGuide.postValue(1)
            isLogged -> toMain.postValue(1)
            else -> toLogin.postValue(1)
        }
    }

    fun onDisagreePrivacyPolicy(){
        startRepository.isAgreedPrivacyPolicy = false
    }

    fun onAgreePrivacyPolicy(){
        startRepository.isAgreedPrivacyPolicy = true
    }

    fun onPrivacyPolicyDialogDismiss(){
        if(startRepository.isAgreedPrivacyPolicy){
            verifyPrivacyPolicy()
        }else{
            finishAllActivities()
        }
    }


    companion object{
        const val delay_time = 3000L
    }


}