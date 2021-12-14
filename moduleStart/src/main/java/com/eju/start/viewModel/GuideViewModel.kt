package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.start.repository.StartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(private val splashRepository: StartRepository):BaseViewModel() {

    val toMain = MutableLiveData<Int>()

    val toLogin = MutableLiveData<Int>()

    fun navigation(){
        launch (showLoading = false){
            val isLogged = splashRepository.verifyIfHasLogged()
            Timber.i("navigation isLogged:${isLogged}")
            when{
                isLogged -> toMain.postValue(1)
                else -> toLogin.postValue(1)
            }
        }
    }

}