package com.eju.retrofit

import java.lang.RuntimeException

open class ApiException(val code:String?,val msg:String?): RuntimeException(msg) {
    companion object{
        const val codeIsNull = "-1"
        const val kickedOut = "10000"
    }

}