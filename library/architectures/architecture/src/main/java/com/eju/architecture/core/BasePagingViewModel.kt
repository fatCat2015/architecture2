package com.eju.architecture.core

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job

abstract class BasePagingViewModel<V,D: PagingData<V>,K: LoadingParams<V, D>>: BaseViewModel(), IPagingBehavior {

    private val _list:MutableList<V> by lazy {
        mutableListOf()
    }

    val list:List<V> = _list

    private val loadingParams:K by lazy {
        loadingParamsFactory.invoke()
    }

    abstract val loadingParamsFactory:()->K

    internal val finishRefreshLD : MutableLiveData<Int> by lazy {
        MainSaveLiveData<Int>()
    }
    internal val finishLoadMoreLD : MutableLiveData<Int> by lazy {
        MainSaveLiveData<Int>()
    }
    internal val setEnableLoadMoreLD : MutableLiveData<Boolean> by lazy {
        MainSaveLiveData<Boolean>()
    }
    internal val showEmptyViewLD : MutableLiveData<Boolean> by lazy {
        MainSaveLiveData<Boolean>()
    }

    internal val notifyDataSetChangedLD : MutableLiveData<Int> by lazy {
        MainSaveLiveData<Int>()
    }

    internal val notifyItemChangedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MainSaveLiveData<ListNotifyInfo>()
    }

    internal val notifyItemInsertedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MainSaveLiveData<ListNotifyInfo>()
    }

    internal val notifyItemRemovedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MainSaveLiveData<ListNotifyInfo>()
    }

    internal val notifyItemMovedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MainSaveLiveData<ListNotifyInfo>()
    }

    final override fun finishRefresh() {
        finishRefreshLD.value = (finishRefreshLD.value?:0)+1
    }

    final override fun finishLoadMore() {
        finishLoadMoreLD.value = (finishLoadMoreLD.value?:0)+1
    }

    final override fun setEnableLoadMore(enabled: Boolean) {
        setEnableLoadMoreLD.value = enabled
    }

    final override fun showEmptyView(showEmpty: Boolean) {
        showEmptyViewLD.value = showEmpty
    }

    final override fun notifyDataSetChanged() {
        notifyDataSetChangedLD.value = (notifyDataSetChangedLD.value?:0)+1
    }

    final override fun notifyItemChanged(position: Int, itemCount: Int, payload: Any?) {
        notifyItemChangedLD.value =  notifyItemChangedLD.value?.apply {
            this.position=position
            this.itemCount=itemCount
            this.payload=payload
        }?:ListNotifyInfo(position,itemCount,payload)
    }

    final override fun notifyItemInserted(position: Int, itemCount: Int) {
        notifyItemInsertedLD.value =  notifyItemInsertedLD.value?.apply {
            this.position=position
            this.itemCount=itemCount
        }?:ListNotifyInfo(position,itemCount,null)
    }

    final override fun notifyItemRemoved(position: Int, itemCount: Int) {
        notifyItemRemovedLD.value =  notifyItemRemovedLD.value?.apply {
            this.position=position
            this.itemCount=itemCount
        }?:ListNotifyInfo(position,itemCount,null)
    }

    final override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMovedLD.value =  notifyItemMovedLD.value?.apply {
            this.fromPosition = fromPosition
            this.toPosition = toPosition
        }?:ListNotifyInfo(0,0,null).apply {
            this.fromPosition = fromPosition
            this.toPosition = toPosition
        }
    }

    open fun refresh():Job{
        loadingParams.reset()
        return loadPagedList(true)
    }

    open fun loadMore():Job{
        return loadPagedList(false)
    }

    private fun loadPagedList(isRefresh:Boolean):Job{
        return launch(showLoading= false,
            loadingMsg=null,
            onComplete={
                if(isRefresh){
                    finishRefresh()
                }else{
                    finishLoadMore()
                }
            },
        ) {
            val pagedData = load(loadingParams)
            val pagedList = pagedData.getPagedList()?: emptyList()
            if(isRefresh){
                _list.clear()
            }
            _list.addAll(pagedList)
            showEmptyView(verifyShowEmptyView(_list,loadingParams,pagedData))
            setEnableLoadMore(verifyEnableLoadMore(_list,loadingParams,pagedData))
            loadingParams.setNext(_list,pagedData)
            notifyDataSetChanged()
        }
    }

    /**
     * @param list ???????????????????????????????????????
     * @param loadingParams ??????????????????
     * @param pagedData ????????????????????????
     */
    protected abstract fun verifyShowEmptyView(list:List<V>,loadingParams: K,pagedData:D):Boolean

    /**
     * @param list ???????????????????????????????????????
     * @param loadingParams ??????????????????
     * @param pagedData ????????????????????????
     */
    protected abstract fun verifyEnableLoadMore(list:List<V>,loadingParams: K,pagedData:D):Boolean

    protected abstract suspend fun load(loadingParams: K):D



}


/**
 * ????????????
 * V:????????????
 * D:????????????
 */
interface LoadingParams<V,D: PagingData<V>>{
    /**
     * ???????????????????????????
     */
    fun reset()

    /**
     * ?????????????????????????????????????????????
     * @param list ??????????????????
     * @param data ?????????????????????????????????
     */
    fun setNext(list:List<V>,data:D)
}

/**
 * ????????????,V?????????????????????
 */
interface PagingData<V>{
    fun getPagedList():List<V>?
}



