package com.eju.demo.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eju.architecture.base.BaseViewModel
import com.eju.demo.entity.HelpDetail
import com.eju.demo.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository):BaseViewModel() {

    private val _helpDetail = MutableLiveData<HelpDetail>()

    val helpDetail:LiveData<HelpDetail> = _helpDetail

    fun queryHelpDetail(id:String){
        launch {
            delay(2000)
            _helpDetail.value = mainRepository.queryHelpDetail(id).result
        }
    }


}