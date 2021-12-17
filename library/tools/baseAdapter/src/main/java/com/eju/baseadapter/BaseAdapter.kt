package com.eju.baseadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import timber.log.Timber

abstract class BaseAdapter<T>(items:List<T>?):RecyclerView.Adapter<BaseViewHolder<*>>() {

    private val data :List<T> = items?: listOf()

    private val itemDelegateHolder = ItemDelegateHolder<T>()

    private var onItemClickListener: OnItemClickListener<T>?=null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>){
        this.onItemClickListener = onItemClickListener
    }

    fun <B:ViewBinding> addItemDelegate(itemDelegate: ItemDelegate<T, B>){
        itemDelegateHolder.addItemDelegate(itemDelegate)
    }

    override fun getItemViewType(position: Int): Int {
        return itemDelegateHolder.getItemViewType(getItem(position),position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return itemDelegateHolder.onCreateViewHolder(parent,viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val itemViewType = getItemViewType(position)
        val item = getItem(position)
        Timber.i("onBindViewHolder ${position}")
        itemDelegateHolder.onBindViewHolder(holder,item,position,itemViewType,onItemClickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int, payloads: List<Any>) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position)
        }else{
            val itemViewType = getItemViewType(position)
            val item = getItem(position)
            itemDelegateHolder.onBindViewHolder(holder,item,position,itemViewType,payloads)
        }
    }

    open fun getItem(position:Int)= data[position]

//    fun notifyDataSetChanged(isRefresh:Boolean,list:List<T>,animate:Boolean = false){
//        if(isRefresh){
//            update(list)
//        }else{
//            add(list,animate)
//        }
//    }
//
//    fun add(list:List<T>,animate:Boolean = false){
//        val startPosition = itemCount
//        data.addAll(list)
//        if(animate){
//            notifyItemRangeInserted(startPosition,list.size)
//        }else{
//            notifyDataSetChanged()
//        }
//
//    }
//
//    fun update(list:List<T>){
//        data.clear()
//        data.addAll(list)
//        notifyDataSetChanged()
//    }
}




