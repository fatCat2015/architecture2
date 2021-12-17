package com.eju.baseadapter

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import timber.log.Timber

abstract class WrappedAdapter<T>(data:List<T>? = null): BaseAdapter<T>(data){

    private val itemSections:List<ItemSection> by lazy {
        listOf(
            ItemSection(startItemType = 1_000_000), //headers
            ItemSection(startItemType = 2_000_000), //emptyView
            NormalItemSection{                      //normal list items ,item type 范围: [0,1_000_000)
                realItemCount
            },
            ItemSection(startItemType = 3_000_000), //footers
            ItemSection(startItemType = 4_000_000), //noMoreData
        )
    }

    private var emptyHeader :ExtraItem<*>? = null

    private var noMoreDataFooter :ExtraItem<*>? = null

    var hasMoreData :Boolean = true

    init {
        registerAdapterDataObserver(object:RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                if(realItemCount==0){
                    addEmptyHeader()
                    removeNoMoreDataFooter()
                }else{
                    removeEmptyHeader()
                    if(hasMoreData){
                        removeNoMoreDataFooter()
                    }else{
                        addNoMoreDataFooter()
                    }
                }
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


    @Synchronized
    private fun <B:ViewBinding> addItem(itemSectionIndex:Int,item: ExtraItem<B>, notify:Boolean = false){
        if(itemSections[itemSectionIndex].addItem(item) && notify){
            val notifyPosition = itemSections.subList(0,itemSectionIndex+1).fold(0){ count,item->
                count+item.itemCount
            }
            notifyItemInserted(notifyPosition-1)
        }
    }

    @Synchronized
    private fun <B:ViewBinding> removeItem(itemSectionIndex:Int,item: ExtraItem<B>, notify:Boolean = false){
        val itemIndex = itemSections[itemSectionIndex].removeItem(item)
        if(itemIndex >= 0 && notify){
            val preItemCount = itemSections.subList(0,itemSectionIndex).fold(0){ count,item->
                count+item.itemCount
            }
            notifyItemRemoved(preItemCount+itemIndex)
        }
    }

    fun <B:ViewBinding> setEmptyView(emptyView:ExtraItem<B>){
        this.emptyHeader = emptyView
    }

    fun <B:ViewBinding> setEmptyView(bindBlock:(B)->Unit = {},bindingCreator:(ViewGroup)->B){
        this.emptyHeader = object:ExtraItem<B>(){
            override fun getLayoutViewBinding(parent: ViewGroup): B {
                return bindingCreator.invoke(parent)
            }
            override fun onBindView(dataBinding: B) {
                bindBlock.invoke(dataBinding)
            }
        }
    }

    private fun addEmptyHeader(){
        this.emptyHeader?.let {
            addItem(1,it,false)
        }
    }

    private fun removeEmptyHeader(){
        this.emptyHeader?.let {
            removeItem(1,it,false)
        }
    }

    fun <B:ViewBinding> setNoMoreDataView(emptyView:ExtraItem<B>){
        this.noMoreDataFooter = emptyView
    }

    fun <B:ViewBinding> setNoMoreDataView(bindBlock:(B)->Unit = {},bindingCreator:(ViewGroup)->B){
        this.noMoreDataFooter = object:ExtraItem<B>(){
            override fun getLayoutViewBinding(parent: ViewGroup): B {
                return bindingCreator.invoke(parent)
            }
            override fun onBindView(dataBinding: B) {
                bindBlock.invoke(dataBinding)
            }
        }
    }

    private fun addNoMoreDataFooter(){
        this.noMoreDataFooter?.let {
            addItem(4,it,false)
        }
    }

    private fun removeNoMoreDataFooter(){
        this.noMoreDataFooter?.let {
            removeItem(4,it,false)
        }
    }

    fun <B:ViewBinding> addHeader(header: ExtraItem<B>, notify:Boolean = false){
        addItem(0,header,notify)
    }

    fun removeHeader(header: ExtraItem<*>, notify:Boolean = false){
        removeItem(0,header,notify)
    }

    fun <B:ViewBinding> addFooter(footer: ExtraItem<B>, notify:Boolean=false){
        addItem(3,footer,notify)
    }

    fun removeFooter(footer: ExtraItem<*>, notify:Boolean=false){
        removeItem(3,footer,notify)
    }

    final override fun getItemCount(): Int {
        return itemSections.fold(0){count,itemSection->
            count+itemSection.itemCount
        }
    }

    fun isNormalItem(position:Int) :Boolean = getItemViewType(position) in 0 until 1_000_000

    private val realItemCount :Int get() =  super.getItemCount()

    override fun getItemViewType(position: Int): Int {
        var positions = mutableListOf<List<Int>>()
        var start = 0
        itemSections.forEach {
            positions.add(List(it.itemCount){
                it+start
            })
            start += it.itemCount
        }
        return positions.find { it.contains(position) }?.let {
            val itemSection = itemSections[positions.indexOf(it)]
            if(itemSection is NormalItemSection){
                super.getItemViewType(position)
            }else{
                itemSection.getItemType(position-it.first())
            }
        }?:super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return itemSections.find { it.containsItemType(viewType) }?.getItem(viewType)?.let {
            BaseViewHolder(it.getLayoutViewBinding(parent))
        }?: super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int, payloads: List<Any>) {
        val viewType=getItemViewType(position)
         itemSections.find { it.containsItemType(viewType) }?.getItem(viewType)?.let {
             it._onBindView(holder.binding)
        }?: super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItem(position: Int): T {
        return super.getItem(position-(itemSections[0].itemCount)-(itemSections[1].itemCount))
    }

}


abstract class ExtraItem<B:ViewBinding>{

    abstract fun getLayoutViewBinding(parent: ViewGroup):B

    abstract fun onBindView(dataBinding:B)

    internal fun _onBindView(binding:ViewBinding){
        onBindView(binding as B)
    }
}


internal open class ItemSection(
    private val startItemType:Int,
    private var nextItemType:Int = startItemType,
    private val items:SparseArrayCompat<ExtraItem<*>> = SparseArrayCompat()
){
    open val itemCount :Int get() = items.size()

    fun <B:ViewBinding> addItem(item:ExtraItem<B>):Boolean{
        return if(!items.containsValue(item)){
            items.put(nextItemType,item)
            nextItemType++
            true
        }else{
            false
        }
    }

    fun <B:ViewBinding> removeItem(item:ExtraItem<B>):Int{
        val index=items.indexOfValue(item)
        return if(index>=0){
            items.removeAt(index)
            index
        }else{
            -1
        }
    }

    fun getItemType(index: Int) = items.keyAt(index)

    fun containsItemType(itemType:Int) :Boolean {
        return items.containsKey(itemType)
    }

    fun getItem(itemType: Int):ExtraItem<*>? = items.get(itemType)
}

internal class NormalItemSection(private val itemCountGetter:()->Int):ItemSection(
    startItemType = 0
){
    override val itemCount: Int
        get() = itemCountGetter.invoke()

}



