package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.start.repository.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(private val splashRepository: SplashRepository):BaseViewModel() {

    val toMain = MutableLiveData<Int>()

    val toLogin = MutableLiveData<Int>()

    fun navigation(){
        launch (showLoading = false){
            val isLogged = splashRepository.verifyIfHasLogged()
            Timber.i("navigation isLogged:${isLogged}")
            when{
                isLogged -> toMain.value = 1
                else -> toLogin.value = 1
            }
        }
    }

}