package com.eju.appbase.base

import com.eju.appbase.service.PagedList
import com.eju.architecture.core.BasePagingViewModel
import com.eju.architecture.core.LoadingParams


abstract class BasePagePagingViewModel<T>:
    BasePagingViewModel<T, PagedList<T>, PageLoadingParams<T>>(){

    protected open val initialPage:Int = 1

    protected open val pageSize:Int = 10

    override val loadingParamsFactory: () -> PageLoadingParams<T>
        get() = {
            PageLoadingParams(initialPage,pageSize)
        }

    override fun verifyShowEmptyView(
        list: List<T>,
        loadingParams: PageLoadingParams<T>,
        pagedData: PagedList<T>
    ): Boolean {
        return list.isEmpty()
    }

    override fun verifyEnableLoadMore(
        list: List<T>,
        loadingParams: PageLoadingParams<T>,
        pagedData: PagedList<T>
    ): Boolean {
        return pagedData.total_count?.let { total_count->
            list.size < total_count
        }?:pagedData.total_page?.let { total_page->
            loadingParams.page < total_page
        }?:pagedData.getPagedList()?.let { pagedList ->
            pagedList.size >= loadingParams.pageSize
        }?:false
    }

}

class PageLoadingParams<T>(
    private val initialPage:Int,
    val pageSize:Int,
): LoadingParams<T, PagedList<T>> {

    var page:Int = initialPage

    override fun reset() {
        this.page = initialPage
    }
    override fun setNext(list:List<T>,data: PagedList<T>) {
        this.page+=1
    }

}