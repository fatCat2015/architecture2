package com.eju.demomodule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.demomodule.entity.Order
import com.eju.demomodule.repository.OrderDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(private val orderDetailRepository: OrderDetailRepository):BaseViewModel() {

    private val _orderDetail = MutableLiveData<Order>()
    val orderDetail:LiveData<Order> = _orderDetail

    fun queryOrderDetail(id:String){
        launch {
            orderDetailRepository.orderDetail(id)
        }
    }
}