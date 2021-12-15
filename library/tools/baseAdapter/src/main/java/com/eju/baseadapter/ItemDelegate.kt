package com.eju.baseadapter

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * 每一个ItemDelegate代表一种布局类型
 */
abstract class ItemDelegate<T,B:ViewBinding> {

    /**
     * 布局itemView对应的ViewBinding
     */
    abstract fun getLayoutViewBinding(parent: ViewGroup):B

    /**
     * @return true 表示 数据item使用该布局
     */
    abstract fun apply(item:T,position:Int):Boolean

    /**
     * @return true 表示 该布局可以进行点击
     */
    open fun clickAble():Boolean{
        return true
    }

    /**
     * 布局和数据进行绑定
     */
    internal fun _bindData(viewHolder: BaseViewHolder<*>, item: T, position:Int){
        bindData(viewHolder as BaseViewHolder<B>,item,position)
    }

    abstract fun bindData(viewHolder: BaseViewHolder<B>, item: T, position:Int)

    /**
     * 布局和数据进行绑定 一般用于局部刷新
     */
    internal fun _bindData(viewHolder: BaseViewHolder<*>, item: T, position:Int, payloads: List<Any>){
        bindData(viewHolder as BaseViewHolder<B>,item,position,payloads)
    }

    open fun bindData(viewHolder: BaseViewHolder<B>, item: T, position:Int, payloads: List<Any>){

    }
}


