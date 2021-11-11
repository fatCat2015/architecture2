package com.eju.architecture.base.paging

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import com.eju.architecture.base.BaseViewModel
import com.eju.architecture.base.IPagingBehavior
import com.eju.architecture.base.ListNotifyInfo
import com.eju.retrofit.PagedList

abstract class BasePagingViewModel<K,V>(): BaseViewModel(), IPagingBehavior {

    internal val finishRefreshLD : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    internal val finishLoadMoreLD : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    internal val setEnableLoadMoreLD : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    internal val showEmptyViewLD : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    internal val notifyDataSetChangedLD : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    internal val notifyItemChangedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MutableLiveData<ListNotifyInfo>()
    }

    internal val notifyItemInsertedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MutableLiveData<ListNotifyInfo>()
    }

    internal val notifyItemRemovedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MutableLiveData<ListNotifyInfo>()
    }

    internal val notifyItemMovedLD : MutableLiveData<ListNotifyInfo> by lazy {
        MutableLiveData<ListNotifyInfo>()
    }

    protected val loadingParams: LoadingParams<K>  by lazy {
        loadingParamsProducer()
    }

    protected abstract val loadingParamsProducer:()->LoadingParams<K>

    private val _list:MutableList<V> by lazy {
        mutableListOf()
    }

    val list:List<V> get() = _list

    @MainThread
    override fun finishRefresh() {
        finishRefreshLD.value = (finishRefreshLD.value?:0)+1
    }

    @MainThread
    override fun finishLoadMore() {
        finishLoadMoreLD.value = (finishLoadMoreLD.value?:0)+1
    }

    @MainThread
    override fun setEnableLoadMore(enabled: Boolean) {
        setEnableLoadMoreLD.value = enabled
    }

    @MainThread
    override fun showEmptyView(showEmpty: Boolean) {
        showEmptyViewLD.value = showEmpty
    }

    @MainThread
    override fun notifyDataSetChanged() {
        notifyDataSetChangedLD.value = (notifyDataSetChangedLD.value?:0)+1
    }

    @MainThread
    override fun notifyItemChanged(position: Int, itemCount: Int, payload: Any?) {
        notifyItemChangedLD.value =  notifyItemChangedLD.value?.apply {
            this.position=position
            this.itemCount=itemCount
            this.payload=payload
        }?:ListNotifyInfo(position,itemCount,payload)
    }

    @MainThread
    override fun notifyItemInserted(position: Int, itemCount: Int) {
        notifyItemInsertedLD.value =  notifyItemInsertedLD.value?.apply {
            this.position=position
            this.itemCount=itemCount
        }?:ListNotifyInfo(position,itemCount,null)
    }

    @MainThread
    override fun notifyItemRemoved(position: Int, itemCount: Int) {
        notifyItemRemovedLD.value =  notifyItemRemovedLD.value?.apply {
            this.position=position
            this.itemCount=itemCount
        }?:ListNotifyInfo(position,itemCount,null)
    }

    @MainThread
    override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMovedLD.value =  notifyItemMovedLD.value?.apply {
            this.fromPosition = fromPosition
            this.toPosition = toPosition
        }?:ListNotifyInfo(0,0,null).apply {
            this.fromPosition = fromPosition
            this.toPosition = toPosition
        }
    }

    fun refresh(){
        resetKey()
        loadPagedData(true)
    }

    fun loadMore(){
        loadPagedData(false)
    }

    protected abstract fun resetKey()

    protected abstract fun setNextPage(pagedList: PagedList<V>)

    protected abstract fun verifyEnableLoadMore(pagedList: PagedList<V>,list:List<V>):Boolean

    protected abstract fun verifyShowEmptyView(list:List<V>):Boolean

    private fun loadPagedData(isRefresh:Boolean){
        launch(
            showLoading = false,
            loadingMsg = "",
            onError = {
                false
            },
            onComplete = {
                if(isRefresh){
                    finishRefresh()
                }else{
                    finishLoadMore()
                }
            }
        ){
            val pagedList=load(loadingParams)
            val positionStart = _list.size
            val itemCount = pagedList.list?.size?:0
            if(isRefresh){
                _list.clear()
            }
            pagedList.list?.let {
                _list.addAll(it)
            }
            showEmptyView(verifyShowEmptyView(_list))
            setEnableLoadMore(verifyEnableLoadMore(pagedList,_list))
            setNextPage(pagedList)
            if(!isRefresh&&itemCount>0){
                notifyItemInserted(positionStart,itemCount)
            }else{
                notifyDataSetChanged()
            }
        }

    }

    protected abstract suspend fun load(loadingParams: LoadingParams<K>):PagedList<V>



}


