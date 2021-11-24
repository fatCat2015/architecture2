package com.eju.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException

class HttpErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val response = chain.proceed(chain.request())
            if(response!=null&&response.isSuccessful){
                return response
            }
            throw HttpException(response.code,response.message)
        } catch (e: IOException) {
            e.printStackTrace()
            throw convertException(e)
        }
    }

    private fun convertException(e:Exception):IOException{
        val application=RetrofitInitializer.application
        var msg :String =
            when (e) {
                is HttpException -> {
                    if(e.code.toString().startsWith("4")){   //4xx
                        application.getString(R.string.HttpException4xx)
                    }else{
                        application.getString(R.string.HttpException)  //3xx 5xx
                    }
                }
                is SocketTimeoutException -> {
                    application.getString(R.string.SocketTimeoutException)
                }
                is ConnectException -> {
                    application.getString(R.string.ConnectException)
                }
                else -> {
                    application.getString(R.string.UnknownHostException)
                }
            }
        return IOException(msg)
    }

    private data class HttpException(
        val code:Int,
        val msg:String?
    ):IOException(msg)
}



