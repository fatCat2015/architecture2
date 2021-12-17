package com.eju.baseadapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import timber.log.Timber
import java.lang.IllegalArgumentException

internal class ItemDelegateHolder<T>{

    /**
     * key对应的是itemViewType
     */
    private val itemDelegateMap= SparseArrayCompat<ItemDelegate<T, *>>()

    /**
     * 添加新的ItemDelegate
     */
    fun addItemDelegate(itemDelegate: ItemDelegate<T, *>){
        itemDelegateMap.put(itemDelegateMap.size(),itemDelegate)
    }

    /**
     * 获取item对应使用的itemViewType
     */
    fun getItemViewType(item:T,position: Int):Int{
        val size=itemDelegateMap.size()
        for(index in 0 until size){
            val itemViewType=itemDelegateMap.keyAt(index)
            val itemDelegate=itemDelegateMap.valueAt(index)
            if(itemDelegate.apply(item,position)){
                return itemViewType
            }
        }
        throw IllegalStateException("each item has to specify a item type ")
    }

    /**
     * 通过itemViewType获取对应的ItemDelegate
     */
    private fun getItemDelegateByItemViewType(itemViewType:Int): ItemDelegate<T, *> {
        val itemDelegate=itemDelegateMap[itemViewType]
        return itemDelegate?.let { it } ?:throw IllegalArgumentException("illegal itemViewType: $itemViewType")
    }


    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemDelegate=getItemDelegateByItemViewType(viewType)
        return BaseViewHolder(itemDelegate.getLayoutViewBinding(parent))
    }

    fun onBindViewHolder(holder: BaseViewHolder<*>, item:T, position:Int, viewType:Int, onItemClickListener: OnItemClickListener<T>?){
        val itemDelegate=getItemDelegateByItemViewType(viewType)
        itemDelegate._bindData(holder,item,position)
        if(itemDelegate.clickAble()){
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(holder,item,holder.adapterPosition) //使用holder.adapterPosition 是因为使用notifyItemInserted()等更新数据之后,position不会改变
            }
        }else{
            holder.itemView.setOnClickListener(null)
        }
    }

    fun onBindViewHolder(holder: BaseViewHolder<*>, item:T, position:Int, viewType:Int, payloads: List<Any>){
        val itemDelegate=getItemDelegateByItemViewType(viewType)
        itemDelegate._bindData(holder,item,position,payloads)
    }





}