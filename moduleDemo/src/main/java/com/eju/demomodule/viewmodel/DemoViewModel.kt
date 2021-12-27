package com.eju.demomodule.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.demomodule.entity.Order
import com.eju.demomodule.repository.OrderDetailRepository
import com.eju.retrofit.download.DownloadProxy
import com.eju.tools.cacheDirPath
import com.eju.wechat.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.io.File
import java.net.URL
import java.net.URLConnection
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(private val orderDetailRepository: OrderDetailRepository):BaseViewModel() {


    @Inject lateinit var downloadProxy:DownloadProxy

    private val _orderDetail = MutableLiveData<Order>()
    val orderDetail:LiveData<Order> = _orderDetail

    fun queryOrderDetail(id:String){
        launch {
            orderDetailRepository.orderDetail(id)
        }
    }

    fun download(){
        launch(loadingMsg = "下载中") {
            Timber.tag("DownloadProxy").i("${downloadProxy} ${Thread.currentThread().id}")
//            downloadProxy.downLoad("https://img0.baidu.com/it/u=1051577226,2771334401&fm=26&fmt=auto",
            val outputFile = downloadProxy.downLoad("https://img.jiandanhome.com/yft_sync/2112/25/1640436250484.mp4",
                File(cacheDirPath),"${System.currentTimeMillis()}")
            Timber.tag("DownloadProxy").i("outputFile :${outputFile}")
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