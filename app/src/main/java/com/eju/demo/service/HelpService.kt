package com.eju.demo.service

import com.eju.demo.entity.HelpDetail
import com.eju.retrofit.BaseResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.io.Serializable

interface HelpService {
    @GET("/rescue/detail")
    @Headers("version:1.0.9")
    suspend fun getHelpDetail(@Query("rescue_id") id:String): BaseResult<HelpDetail>
}

