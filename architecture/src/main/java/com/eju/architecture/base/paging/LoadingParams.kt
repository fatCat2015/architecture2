package com.eju.architecture.base.paging

/**
 * 分页参数
 * V:数据类型
 * D:分页数据
 */
interface LoadingParams<V,D:PagingData<V>>{
    /**
     * 刷新前重置刷新参数
     */
    fun reset()

    /**
     * 请求成功后设置下一页的请求参数
     * @param list 当前列表数据
     * @param data 最后一个请求的分页数据
     */
    fun setNext(list:List<V>,data:D)
}

/**
 * 分页数据,V表示数据的类型
 */
interface PagingData<V>{
    fun getPagedList():List<V>?
}
