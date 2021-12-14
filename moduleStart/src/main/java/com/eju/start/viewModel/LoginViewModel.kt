package com.eju.start.viewModel

import androidx.lifecycle.MutableLiveData
import com.eju.appbase.persistence.IS_LOGGED
import com.eju.architecture.core.BaseViewModel
import com.eju.retrofit.RetrofitModule
import com.eju.start.repository.LoginRepository
import com.eju.tools.saveBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository):BaseViewModel() {

    val toMain = MutableLiveData<Boolean>()

    fun login(mobile:String,code:String){
        launch(loadingMsg = "登录中...") {
            val user=loginRepository.login(mobile,code)
            loginRepository.onLogin(user)
            toMain.postValue(true)
        }
    }

}