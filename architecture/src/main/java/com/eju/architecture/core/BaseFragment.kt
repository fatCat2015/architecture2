package com.eju.architecture.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.eju.architecture.viewBinding
import com.eju.tools.initializer.NetworkState
import com.eju.tools.initializer.observeNetworkStatus

abstract class BaseFragment<V:ViewBinding>:Fragment(),IViewBehavior,IExceptionHandler {


    protected val binding:V by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        afterViewCreate(savedInstanceState)
        setListeners()
        observeNetworkStatus{ connected, state->
            onNetworkStateChanged(connected,state)
        }
    }

    open fun onNetworkStateChanged(connected:Boolean,newNetworkState: NetworkState){

    }

    abstract fun observe()

    abstract fun afterViewCreate(savedInstanceState: Bundle?)

    abstract fun setListeners()


}