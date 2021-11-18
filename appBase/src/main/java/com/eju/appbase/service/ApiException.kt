package com.eju.appbase.service

import java.lang.RuntimeException

open class ApiException(val code:String?,val msg:String?): RuntimeException(msg) {

}

object ServiceErrorCode{
    const val NULL_CODE = "-1"
    const val SUCCESS = "SYS000"
    const val BE_KICKEd_OUT = "10000"
    const val TOKEN_OUT_OF_DATE = "10001"
}