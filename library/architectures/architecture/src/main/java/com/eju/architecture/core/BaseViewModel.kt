package com.eju.architecture.core

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.eju.architecture.BuildConfig
import com.eju.tools.application
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel():ViewModel(),DefaultLifecycleObserver, IViewBehavior,IExceptionHandler{

    internal val showLoadingLD : MutableLiveData<CharSequence> by lazy {
        MainSaveLiveData<CharSequence>()
    }
    internal val hideLoadingLD : MutableLiveData<Int> by lazy {
        MainSaveLiveData<Int>()
    }
    internal val showToastLD :MutableLiveData<ToastInfo> by lazy {
        MainSaveLiveData<ToastInfo>()
    }
    internal val showSnackLD :MutableLiveData<SnackInfo> by lazy {
        MainSaveLiveData<SnackInfo>()
    }
    internal val finishPageLD : MutableLiveData<Int> by lazy {
        MainSaveLiveData<Int>()
    }
    internal val handleExceptionLD:MutableLiveData<Throwable> by lazy {
        MainSaveLiveData<Throwable>()
    }

    final override fun showLoading(msg: CharSequence?) {
        showLoadingLD.value = msg
    }

    final override fun showLoading(resId: Int) {
        showLoading(application.getString(resId))
    }

    final override fun hideLoading() {
        hideLoadingLD.value = (hideLoadingLD.value?:0)+1
    }

    final override fun showToast(msg: CharSequence?, duration: Int) {
        showToastLD.value = showToastLD.value?.apply {
            this.msg = msg
            this.duration =duration
        }?: ToastInfo(msg,null,duration)
    }

    final override fun showToast(resId: Int?, duration: Int) {
        showToastLD.value = showToastLD.value?.apply {
            this.resId = resId
            this.duration =duration
        }?: ToastInfo(null,resId,duration)
    }

    final override fun showSnack(msg: CharSequence?, duration: Int, code: Int) {
        showSnackLD.value = showSnackLD.value?.apply {
            this.msg = msg
            this.duration =duration
            this.code = code
        }?: SnackInfo(msg,null,duration,code)
    }

    final override fun showSnack(resId: Int?, duration: Int, code: Int) {
        showSnackLD.value = showSnackLD.value?.apply {
            this.resId = resId
            this.duration =duration
            this.code = code
        }?: SnackInfo(null,resId,duration,code)
    }

    final override fun finishPage(){
        finishPageLD.value = (finishPageLD.value?:0)+1
    }

    final override fun handleException(throwable: Throwable?) {
        handleExceptionLD.value = throwable
    }

    private suspend fun CoroutineScope.wrap(
        showLoading: Boolean,
        loadingMsg: CharSequence?,
        onError: ((Throwable) -> Boolean)?,
        onComplete: (() -> Unit)?,
        block: suspend CoroutineScope.() -> Unit,
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
    protected fun  launch(
        context:CoroutineContext = viewModelScope.coroutineContext+Dispatchers.IO,
        showLoading: Boolean = true,
        loadingMsg: CharSequence? = null,
        onError: ((Throwable) -> Boolean)? = null,
        onComplete: (() -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return viewModelScope.launch(context) {
            wrap(showLoading, loadingMsg, onError, onComplete ,block)
        }
    }


    //custom
    protected fun <A,R> combineLiveData(
        context:CoroutineContext = viewModelScope.coroutineContext+Dispatchers.IO,
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
                lastJob = launch(context,showLoading,loadingMsg,onError,onComplete){
                    postValue(block(liveData0.value))
                }
            }
        }
    }

    protected fun <A,B,R> combineLiveData(
        context:CoroutineContext = viewModelScope.coroutineContext+Dispatchers.IO,
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
                lastJob = launch(context,showLoading,loadingMsg,onError,onComplete){
                    postValue(block(liveData0.value,liveData1.value))
                }
            }
        }
    }

    protected fun <A,B,C,R> combineLiveData(
        context:CoroutineContext = viewModelScope.coroutineContext+Dispatchers.IO,
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
                lastJob = launch(context,showLoading,loadingMsg,onError,onComplete){
                    postValue(block(liveData0.value,liveData1.value,liveData2.value))
                }
            }
        }
    }

    protected fun <A,B,C,D,R> combineLiveData(
        context:CoroutineContext = viewModelScope.coroutineContext+Dispatchers.IO,
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
                lastJob = launch(context,showLoading,loadingMsg,onError,onComplete){
                    postValue(block(liveData0.value,liveData1.value,liveData2.value,liveData3.value))
                }
            }
        }
    }


}