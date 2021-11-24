package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.start.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository):BaseViewModel() {

    val toMain = MutableLiveData<Boolean>()

    fun login(mobile:String,code:String){
        launch(loadingMsg = "登录中...") {
            val user=loginRepository.login(mobile,code)
            loginRepository.saveUserInfo(user)
            toMain.value = true
        }
    }

}