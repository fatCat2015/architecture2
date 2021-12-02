package com.eju.start.api

import com.eju.appbase.service.BaseResult
import com.eju.start.api.bean.User
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LoginService {

    @GET("/rescue/detail")
    @Headers("version:1.0.9")
    suspend fun simulateLogin(@Query("rescue_id") mobile:String,@Query("code") code:String): BaseResult<User>
}