package com.eju.demo.entity

import com.eju.architecture.core.PagingData

data class Moment(
    val id:String,
    val content:String
)

class MomentPagingData: PagingData<Moment> {

    var momentList :List<Moment> ? =null
    var momentNextParams :MomentNext? =null

    override fun getPagedList(): List<Moment>? {
        return momentList
    }
}

data class MomentNext(
    val nextAnchorId :String?,
    val nextAnchorId1 :String?,
)