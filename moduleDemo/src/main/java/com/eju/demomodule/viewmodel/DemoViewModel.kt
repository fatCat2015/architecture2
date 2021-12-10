package com.eju.demomodule.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.demomodule.R
import com.eju.demomodule.entity.Order
import com.eju.demomodule.repository.OrderDetailRepository
import com.eju.tools.application
import com.eju.wechat.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
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

    fun shareBitmap(bitmap:Bitmap){
        launch {
            shareImageToWechat( bitmap,
                WeChatPlatform.SESSION, object : SendReqCallback {
                    override fun onSuccess(result: String) {
                        Timber.tag("handleSendMsgResult").i("share success ${result}")
                    }
                    override fun onFailed(errorMsg: String?) {
                        Timber.tag("handleSendMsgResult").i("share failed ${errorMsg}")
                    }
                })
        }
    }

    fun requestWxLogin(){
        launch {
            sendAuthRequest(object : SendReqCallback {
                override fun onSuccess(result: String) {
                    Timber.tag("handleSendAuthResult").i("sendAuthRequest success ${result}")
                }
                override fun onFailed(errorMsg: String?) {
                    Timber.tag("handleSendAuthResult").i("sendAuthRequest failed ${errorMsg}")
                }
                override fun onCancel() {
                    Timber.tag("handleSendAuthResult").i("sendAuthRequest onCancel ")
                }
            })
        }
    }
}