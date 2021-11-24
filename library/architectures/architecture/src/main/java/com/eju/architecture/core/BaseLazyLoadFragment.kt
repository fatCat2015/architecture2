package com.eju.architecture.core

import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class BaseLazyLoadFragment<B:ViewBinding>:BaseFragment<B>() {

    private var savedInstanceState:Bundle?=null

    private var isLoaded=false

    final override fun observe() {

    }

    final override fun afterViewCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState=savedInstanceState
    }

    final override fun setListeners() {

    }

    override fun onResume() {
        super.onResume()
        if(loadWheneverVisible()){
            lazyLoad()
            isLoaded=true
        }else if(!isLoaded){
            lazyLoad()
            isLoaded=true
        }
    }

    private fun lazyLoad(){
        observeLazy()
        lazyLoad(savedInstanceState)
        setListenersLazy()
    }

    /**
     * 返回true表示可见时即加载 false表示加载一次
     */
    protected open fun loadWheneverVisible()=false

    abstract fun observeLazy()

    abstract fun lazyLoad(savedInstanceState:Bundle?)

    abstract fun setListenersLazy()

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded=false
    }
}