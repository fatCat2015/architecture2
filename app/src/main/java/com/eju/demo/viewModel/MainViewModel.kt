package com.eju.demo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.demo.entity.HelpDetail
import com.eju.demo.repository.MainRepository
import com.eju.retrofit.RetrofitModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository):BaseViewModel() {

    private val _helpDetail = MutableLiveData<HelpDetail>()

    val helpDetail:LiveData<HelpDetail> = _helpDetail

    fun queryHelpDetail(id:String){
        RetrofitModule.addRequestHeader("user_cache","true")
        launch {
            delay(2000)
            _helpDetail.value = mainRepository.queryHelpDetail(id).result
        }
    }


}