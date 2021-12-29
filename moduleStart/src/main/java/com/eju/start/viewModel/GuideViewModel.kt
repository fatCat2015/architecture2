package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.start.BuildConfig
import com.eju.start.repository.StartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(private val startRepository: StartRepository):BaseViewModel() {

    val toMain = MutableLiveData<Int>()

    val toLogin = MutableLiveData<Int>()

    fun navigation(){
        launch (showLoading = false){
            val isLogged = startRepository.isLogged
            Timber.i("navigation isLogged:${isLogged}")
            startRepository.localGuideIndex = BuildConfig.guideIndex
            when{
                isLogged -> toMain.postValue(1)
                else -> toLogin.postValue(1)
            }
        }
    }

}