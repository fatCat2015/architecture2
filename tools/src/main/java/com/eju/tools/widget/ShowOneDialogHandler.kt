package com.eju.tools.widget

import android.view.*
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import java.util.*


object ShowOneDialogHandler{

    private val dialogMap:MutableMap<FragmentManager,LinkedList<DialogFragment>> by lazy {
        mutableMapOf()
    }

    @MainThread
    fun show(fragmentActivity: FragmentActivity,dialog: DialogFragment){
        show(fragmentActivity,fragmentActivity.supportFragmentManager,dialog)
    }

    @MainThread
    fun show(fragment: Fragment,dialog: DialogFragment){
        show(fragment,fragment.childFragmentManager,dialog)
    }

    private fun show(lifecycleOwner: LifecycleOwner,fragmentManager: FragmentManager,dialog: DialogFragment){
        val dialogList= dialogMap[fragmentManager]?:LinkedList<DialogFragment>().also {
            lifecycleOwner.lifecycle.addObserver(object:DefaultLifecycleObserver{
                override fun onDestroy(owner: LifecycleOwner) {
                    dialogMap.remove(fragmentManager)?.clear()
                }
            })
            dialogMap[fragmentManager]=it
        }
        dialogList.addFirst(dialog)
        dialog.lifecycle.addObserver(dialogLifecycleObserver)
        if(dialogList.size==1){
            showDialogAllowingStateLoss(fragmentManager,dialog)
        }
    }

    private val dialogLifecycleObserver=object:DefaultLifecycleObserver{
        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            if(owner is DialogFragment){
                findEntryFromDialog(owner)?.let { entry->
                    val fragmentManager=entry.key
                    val dialogList=entry.value
                    dialogList.remove(owner)
                    if(dialogList.isNotEmpty()){
                        val nextDialog=dialogList.last()
                        showDialogAllowingStateLoss(fragmentManager,nextDialog)
                    }
                }
            }
        }
    }

    private fun findEntryFromDialog(dialog: DialogFragment): Map.Entry<FragmentManager,LinkedList<DialogFragment>>?{
        var entry:Map.Entry<FragmentManager,LinkedList<DialogFragment>>?=null
        dialogMap.forEach {
            if(it.value.contains(dialog)){
                entry=it
                return@forEach
            }
        }
        return entry
    }


    private fun showDialogAllowingStateLoss(fragmentManager: FragmentManager,dialogFragment: DialogFragment) {
        fragmentManager.beginTransaction().add(dialogFragment, javaClass.simpleName).commitAllowingStateLoss()
    }



}