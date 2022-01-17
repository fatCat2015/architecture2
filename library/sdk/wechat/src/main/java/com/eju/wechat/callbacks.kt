package com.eju.wechat


interface SendReqCallback{
    fun onSuccess(result:String)
    fun onFailed(errorMsg:String?)
    fun onCancel(){

    }
}

internal val callBackMap by lazy {
    mutableMapOf<String,SendReqCallback>()
}
