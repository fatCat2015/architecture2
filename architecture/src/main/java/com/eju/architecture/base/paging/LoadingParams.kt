package com.eju.architecture.base.paging

data class LoadingParams<K>(
    val initialKey:K,
    val loadSize:Int,
){
    var key:K = initialKey
}
