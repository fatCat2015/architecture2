package com.eju.tools.widget

import android.view.*
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import timber.log.Timber
import java.util.*


class ShowOneDialogHandler(){

    private val dialogList :MutableList<WrappedDialogFragment> by lazy {
        mutableListOf()
    }

    private inner class WrappedDialogFragment(
        private val fragmentManager: FragmentManager,
        private val dialogFragment: DialogFragment,
        private val isAllowingStateLoss:Boolean,
        private val tag:String?
    ):DefaultLifecycleObserver{

        init {
            dialogFragment.lifecycle.addObserver(this)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            dialogList.remove(this)
            Timber.i("dialog destroyed --> ${dialogFragment} ")
            showNext()
        }

        fun show() {
            Timber.i("show dialog --> ${dialogFragment}")
            if(isAllowingStateLoss){
                fragmentManager.beginTransaction().add(dialogFragment, tag).commitAllowingStateLoss()
            }else{
                dialogFragment.show(fragmentManager,tag)
            }
        }
    }

    @MainThread
    fun show(fragmentManager: FragmentManager,dialogFragment: DialogFragment,isAllowingStateLoss:Boolean = true,tag:String?=null){
        dialogList.add(WrappedDialogFragment(fragmentManager,dialogFragment,isAllowingStateLoss,tag))
        if(dialogList.size == 1){
            showNext()
        }
    }

    private fun showNext(){
        dialogList.firstOrNull()?.show()
    }



}