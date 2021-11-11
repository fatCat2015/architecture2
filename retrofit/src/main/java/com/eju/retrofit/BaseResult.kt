package com.eju.retrofit

import com.google.gson.annotations.SerializedName

@Suppress("UNCHECKED_CAST")
data class BaseResult<T>(
    @SerializedName("err_code")
    val code:String?,
    @SerializedName("err_msg")
    val message:String?,
    internal val data:T?
){
    private fun isSuccess()=code=="SYS000"

    val result :T get() {
        return code?.let {
            if(isSuccess()){
                data?.let {
                    it
                }?: "" as T
            }else{
                throw ApiException(code,message)
            }
        }?:throw ApiException(ApiException.codeIsNull,"code is null")
    }
}

data class PagedList<T>(
    val page:Int?,
    val total_page:Int?,
    val total_count:Int?,
    val list:List<T>?
)