package com.eju.demomodule.repository

import com.eju.appbase.service.BaseResult
import com.eju.appbase.service.ServiceErrorCode
import com.eju.architecture.core.BaseRepository
import com.eju.demomodule.entity.Order
import javax.inject.Inject

class OrderDetailRepository @Inject constructor():BaseRepository() {

    suspend fun orderDetail(id:String):Order{
        return BaseResult(
            code = ServiceErrorCode.BE_KICKEd_OUT,
            message = "",
            data = Order("1","sck220")
        ).result
    }
}