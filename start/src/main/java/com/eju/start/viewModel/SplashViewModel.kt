package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.start.repository.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val splashRepository: SplashRepository):BaseViewModel() {

    val toMain = MutableLiveData<Int>()

    val toLogin = MutableLiveData<Int>()

    val toGuide = MutableLiveData<Int>()


    fun countdownToPage(){
        launch (showLoading = false){
            delay(delay_time)
            val showGuide = splashRepository.verifyShowGuidePage()
            val isLogged = splashRepository.verifyIfHasLogged()
            val firstLaunchNewVersion =  splashRepository.verifyIfFirstLaunchNewVersion()
            Timber.i("countdownToPage showGuide:${showGuide};isLogged:${isLogged};firstLaunchNewVersion:${firstLaunchNewVersion}")
            when{
                showGuide -> toGuide.value = 1
                isLogged -> toMain.value = 1
                else -> toLogin.value = 1
            }
        }
    }



    companion object{
        const val delay_time = 1000L
    }


}