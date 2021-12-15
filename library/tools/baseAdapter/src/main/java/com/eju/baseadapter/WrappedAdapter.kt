package com.eju.baseadapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class WrappedAdapter<T>(data:List<T>? = null): BaseAdapter<T>(data){

    private val headers= SparseArrayCompat<Header<*>>()

    private val footers= SparseArrayCompat<Footer<*>>()

    val headCount :Int  get() =  headers.size()

    val footerCount :Int get() = footers.size()


    init {
        registerAdapterDataObserver(object:RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }
        })
    }

    fun <B:ViewBinding> addHeader(view: Header<B>, notify:Boolean = false):Int{
        if(!headers.containsValue(view)){
            val itemViewType = headCount
            headers.put(itemViewType,view)
            if(notify){
                notifyItemInserted(itemViewType)
            }
            return itemViewType
        }
        return -1
    }


    fun removeHeader(view: Header<*>, notify:Boolean = false){
        val index=headers.indexOfValue(view)
        if(index>=0){
            headers.removeAt(index)
            if(notify){
                notifyItemRemoved(index)
            }
        }
    }

    fun <B:ViewBinding> addFooter(view: Footer<B>, notify:Boolean=false):Int{
        if(!footers.containsValue(view)){
            val itemType=footerCount
            footers.put(itemType,view)
            if(notify){
                notifyItemInserted(itemCount-1)
            }
            return itemType
        }
        return -1
    }


    fun removeFooter(view: Footer<*>, notify:Boolean=false){
        val index=footers.indexOfValue(view)
        if(index>=0){
            footers.removeAt(index)
            if(notify){
                notifyItemRemoved(headCount+realItemCount+index)
            }
        }
    }


    final override fun getItemCount(): Int {
        return headCount+super.getItemCount()+footerCount
    }

    val realItemCount :Int get() =  super.getItemCount()


    override fun getItemViewType(position: Int): Int {
        val itemViewType = when{
            position < headCount -> headers.keyAt(position)+ HEADER_ITEM_TYPE_OFFSET   //header itemType 100开始
            position >= headCount+realItemCount -> footers.keyAt(position -headCount-realItemCount)+ FOOTER_ITEM_TYPE_OFFSET  //footer itemType 200开始
            else -> super.getItemViewType(position)   //normal itemType 0-99 超过100种itemType就会有问题 o.o
        }
        return itemViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when {
            viewType < HEADER_ITEM_TYPE_OFFSET -> {  //[0,99]
                super.onCreateViewHolder(parent, viewType)
            }
            viewType < FOOTER_ITEM_TYPE_OFFSET -> {  // [100,199]
                BaseViewHolder(getHeaderView(viewType).getLayoutViewBinding())
            }
            else -> {  //[200,-)
                BaseViewHolder(getFooterView(viewType).getLayoutViewBinding())
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int, payloads: List<Any>) {
        val viewType=getItemViewType(position)
        when {
            viewType < HEADER_ITEM_TYPE_OFFSET -> {  //[0,99]
                super.onBindViewHolder(holder, position, payloads)
            }
            viewType < FOOTER_ITEM_TYPE_OFFSET -> {   // [100,199]
                getHeaderView(viewType)._onBindView(holder.binding)
            }
            else -> {   //[200,-)
                getFooterView(viewType)._onBindView(holder.binding)
            }
        }
    }

    override fun getItem(position: Int): T {
        return super.getItem(position-headCount)
    }

    private fun getHeaderView(viewType:Int): ExtraItem<*> {
        return headers.valueAt(viewType - HEADER_ITEM_TYPE_OFFSET)
    }

    private fun getFooterView(viewType:Int): ExtraItem<*> {
        return footers.valueAt(viewType - FOOTER_ITEM_TYPE_OFFSET)
    }

    companion object{
        private const val HEADER_ITEM_TYPE_OFFSET = 100
        private const val FOOTER_ITEM_TYPE_OFFSET = 200
    }

}


abstract class ExtraItem<B:ViewBinding>{

    abstract fun getLayoutViewBinding():B

    abstract fun onBindView(dataBinding:B)

    internal fun _onBindView(binding:ViewBinding){
        onBindView(binding as B)
    }
}

abstract class Header<B:ViewBinding>:ExtraItem<B>()

abstract class Footer<B:ViewBinding>:ExtraItem<B>()




