package com.eju.main.viewmodel

import com.eju.architecture.core.BaseViewModel
import com.eju.main.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository):BaseViewModel() {


}