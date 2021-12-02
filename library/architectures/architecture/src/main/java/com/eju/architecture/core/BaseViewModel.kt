package com.eju.architecture.core

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.eju.architecture.BuildConfig
import com.eju.tools.application
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel():ViewModel(),DefaultLifecycleObserver, IViewBehavior,IExceptionHandler{

    internal val showLoadingLD : MutableLiveData<CharSequence> by lazy {
        MutableLiveData<CharSequence>()
    }
    internal val hideLoadingLD : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    internal val showToastLD :MutableLiveData<ToastInfo> by lazy {
        MutableLiveData<ToastInfo>()
    }
    internal val showSnackLD :MutableLiveData<SnackInfo> by lazy {
        MutableLiveData<SnackInfo>()
    }
    internal val finishPageLD : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    internal val handleExceptionLD:MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    @MainThread
    final override fun showLoading(msg: CharSequence?) {
        showLoadingLD.value = msg
    }

    @MainThread
    final override fun showLoading(resId: Int) {
        showLoading(application.getString(resId))
    }

    @MainThread
    final override fun hideLoading() {
        hideLoadingLD.value = (hideLoadingLD.value?:0)+1
    }

    @MainThread
    final override fun showToast(msg: CharSequence?, duration: Int) {
        showToastLD.value = showToastLD.value?.apply {
            this.msg = msg
            this.duration =duration
        }?: ToastInfo(msg,null,duration)
    }

    @MainThread
    final override fun showToast(resId: Int?, duration: Int) {
        showToastLD.value = showToastLD.value?.apply {
            this.resId = resId
            this.duration =duration
        }?: ToastInfo(null,resId,duration)
    }

    @MainThread
    final override fun showSnack(msg: CharSequence?, duration: Int, code: Int) {
        showSnackLD.value = showSnackLD.value?.apply {
            this.msg = msg
            this.duration =duration
            this.code = code
        }?: SnackInfo(msg,null,duration,code)
    }

    @MainThread
    final override fun showSnack(resId: Int?, duration: Int, code: Int) {
        showSnackLD.value = showSnackLD.value?.apply {
            this.resId = resId
            this.duration =duration
            this.code = code
        }?: SnackInfo(null,resId,duration,code)
    }

    @MainThread
    final override fun finishPage(){
        finishPageLD.value = (finishPageLD.value?:0)+1
    }

    @MainThread
    final override fun handleException(throwable: Throwable?) {
        handleExceptionLD.value = throwable
    }

    private suspend fun CoroutineScope.wrap(
        showLoading: Boolean,
        loadingMsg: CharSequence?,
        onError: ((Throwable) -> Boolean)?,
        onComplete: (() -> Unit)?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            if (showLoading) {
                showLoading(loadingMsg)
            }
            block()
        } catch (e: CancellationException) {
            //JobCancellationException 手动取消 不作处理
        } catch (e: Throwable) {
            if(BuildConfig.DEBUG){
                e.printStackTrace()
            }
            if (onError?.invoke(e) != true) {
                handleException(e)
                showToast(e.message)
            }
        } finally {
            if (showLoading) {
                hideLoading()
            }
            onComplete?.invoke()
        }
    }

    //launch
    protected fun launch(
        showLoading: Boolean = true,
        loadingMsg: CharSequence? = null,
        onError: ((Throwable) -> Boolean)? = null,
        onComplete: (() -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch {
            wrap(showLoading, loadingMsg, onError, onComplete, block)
        }
    }


    //custom
    protected fun <A,R> combineLiveData(
        liveData0: LiveData<A>,
        showLoading: Boolean = true,
        loadingMsg: String? = "加载中",
        onError: ((Throwable) -> Boolean)? = null,
        onComplete: (() -> Unit)? = null,
        block:suspend CoroutineScope.(A?)->R): LiveData<R> {
        return object: MediatorLiveData<R>(){
            private var lastJob:Job?=null
            init {
                addSource(liveData0) {
                    onEachChanged()
                }
            }
            private fun onEachChanged(){
                lastJob?.cancel()
                lastJob=viewModelScope.launch {
                    wrap(showLoading, loadingMsg, onError, onComplete){
                        value=block(liveData0.value)
                    }
                }
            }
        }
    }

    protected fun <A,B,R> combineLiveData(
        liveData0: LiveData<A>,
        liveData1: LiveData<B>,
        showLoading: Boolean = true,
        loadingMsg: String? = "加载中",
        onError: ((Throwable) -> Boolean)? = null,
        onComplete: (() -> Unit)? = null,
        block:suspend CoroutineScope.(A?,B?)->R): LiveData<R> {
        return object: MediatorLiveData<R>(){
            private var lastJob:Job?=null
            init {
                addSource(liveData0) {
                    onEachChanged()
                }
                addSource(liveData1) {
                    onEachChanged()
                }
            }
            private fun onEachChanged(){
                lastJob?.cancel()
                lastJob=viewModelScope.launch {
                    wrap(showLoading, loadingMsg, onError, onComplete){
                        value=block(liveData0.value,liveData1.value)
                    }
                }
            }
        }
    }

    protected fun <A,B,C,R> combineLiveData(
        liveData0: LiveData<A>,
        liveData1: LiveData<B>,
        liveData2: LiveData<C>,
        showLoading: Boolean = true,
        loadingMsg: String? = "加载中",
        onError: ((Throwable) -> Boolean)? = null,
        onComplete: (() -> Unit)? = null,
        block:suspend CoroutineScope.(A?,B?,C?)->R): LiveData<R> {
        return object: MediatorLiveData<R>(){
            private var lastJob:Job?=null
            init {
                addSource(liveData0) {
                    onEachChanged()
                }
                addSource(liveData1) {
                    onEachChanged()
                }
                addSource(liveData2) {
                    onEachChanged()
                }
            }
            private fun onEachChanged(){
                lastJob?.cancel()
                lastJob=viewModelScope.launch {
                    wrap(showLoading, loadingMsg, onError, onComplete){
                        value=block(liveData0.value,liveData1.value,liveData2.value)
                    }
                }
            }
        }
    }

    protected fun <A,B,C,D,R> combineLiveData(
        liveData0: LiveData<A>,
        liveData1: LiveData<B>,
        liveData2: LiveData<C>,
        liveData3: LiveData<D>,
        showLoading: Boolean = true,
        loadingMsg: String? = "加载中",
        onError: ((Throwable) -> Boolean)? = null,
        onComplete: (() -> Unit)? = null,
        block:suspend CoroutineScope.(A?,B?,C?,D?)->R): LiveData<R> {
        return object: MediatorLiveData<R>(){
            private var lastJob:Job?=null
            init {
                addSource(liveData0) {
                    onEachChanged()
                }
                addSource(liveData1) {
                    onEachChanged()
                }
                addSource(liveData2) {
                    onEachChanged()
                }
                addSource(liveData3) {
                    onEachChanged()
                }
            }
            private fun onEachChanged(){
                lastJob?.cancel()
                lastJob=viewModelScope.launch {
                    wrap(showLoading, loadingMsg, onError, onComplete){
                        value=block(liveData0.value,liveData1.value,liveData2.value,liveData3.value)
                    }
                }
            }
        }
    }


}