package com.eju.baseadapter

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class SimpleAdapter<T,B:ViewBinding>(data:List<T>?=null): WrappedAdapter<T>(data) {

    init {
        addItemDelegate(object : ItemDelegate<T, B>(){

            override fun getLayoutViewBinding(parent: ViewGroup): B {
                return  this@SimpleAdapter.getLayoutViewBinding(parent)
            }

            override fun apply(item: T,position: Int): Boolean {
                return true
            }

            override fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int) {
                this@SimpleAdapter.bindData(viewHolder,item,position)
            }

            override fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int, payloads: List<Any>) {
                this@SimpleAdapter.bindData(viewHolder,item,position,payloads)
            }

            override fun clickAble(): Boolean {
                return this@SimpleAdapter.clickAble()
            }
        })
    }


    abstract fun getLayoutViewBinding(parent: ViewGroup): B

    abstract fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int)

    open fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int, payloads: List<Any>){

    }

    open fun clickAble(): Boolean{
        return true
    }


}

