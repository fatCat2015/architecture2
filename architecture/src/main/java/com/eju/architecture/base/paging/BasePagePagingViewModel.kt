package com.eju.architecture.base.paging

import android.app.Application
import com.eju.retrofit.PagedList
import timber.log.Timber

abstract class BasePagePagingViewModel<V>:BasePagingViewModel<Int,V>() {

    open val initialPage:Int = 1

    open val pageSize:Int = 20

    override val loadingParamsProducer: () -> LoadingParams<Int>
        get() = {
            LoadingParams(initialPage,pageSize)
        }

    override fun resetKey() {
        loadingParams.key = initialPage
    }

    override fun setNextPage(pagedList: PagedList<V>) {
        loadingParams.key +=1
    }

    override fun verifyEnableLoadMore(pagedList: PagedList<V>,list:List<V>): Boolean {
        val currentPage = loadingParams.key
        val loadSize = loadingParams.loadSize
        return when{
            pagedList.total_count!=null -> list.size < pagedList.total_count!!
            pagedList.total_page!=null -> currentPage < pagedList.total_page!!
            else -> (pagedList.list?.size?:0) >= loadSize
        }
    }

    override fun verifyShowEmptyView(list: List<V>): Boolean {
        return list.isEmpty()
    }

}