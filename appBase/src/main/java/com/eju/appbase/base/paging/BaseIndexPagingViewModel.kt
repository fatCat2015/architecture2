package com.eju.appbase.base.paging

import com.eju.appbase.service.PagedList
import com.eju.architecture.base.paging.BasePagingViewModel
import com.eju.architecture.base.paging.LoadingParams
import timber.log.Timber


abstract class BaseIndexPagingViewModel<T>:BasePagingViewModel<T,PagedList<T>,IndexLoadingParams<T>>(){

    protected open val initialIndex:Int = 0

    protected open val pageSize:Int = 10

    override val loadingParamsFactory: () -> IndexLoadingParams<T>
        get() = {
            IndexLoadingParams(initialIndex,pageSize)
        }

    override fun verifyShowEmptyView(
        list: List<T>,
        loadingParams: IndexLoadingParams<T>,
        pagedData: PagedList<T>
    ): Boolean {
        return list.isEmpty()
    }

    override fun verifyEnableLoadMore(
        list: List<T>,
        loadingParams: IndexLoadingParams<T>,
        pagedData: PagedList<T>
    ): Boolean {
        return pagedData.total_count?.let { total_count->
            list.size < total_count
        }?:pagedData.getPagedList()?.let { pagedList ->
            pagedList.size >= loadingParams.pageSize
        }?:false
    }

}

class IndexLoadingParams<T>(
    private val initialIndex:Int,
    val pageSize:Int,
):LoadingParams<T,PagedList<T>>{

    var startIndex:Int = initialIndex

    override fun reset() {
        this.startIndex = initialIndex
    }
    override fun setNext(list:List<T>,data: PagedList<T>) {
        this.startIndex = list.size
    }

}