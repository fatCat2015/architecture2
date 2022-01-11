package com.eju.baseadapter.tag

import androidx.annotation.IntRange
import androidx.viewbinding.ViewBinding
import com.eju.baseadapter.SimpleAdapter
import java.util.*

abstract class TagAdapter<T:ITagItem,B:ViewBinding>(
    list:List<T>,
    @IntRange(from = 1, to = Int.MAX_VALUE.toLong())
    maxSelectCount:Int,
    enableUnselect:Boolean,
    unselectOldestWhenExceed:Boolean = false
):SimpleAdapter<T,B>(list) {

    constructor( list:List<T>):this(list,1,false,true)

    var actionWhenExceedMaxCount:((Int)->Unit)? = null

    var onSelectedChangedCallback:((List<T>,Int)->Unit)? = null

    val selectedCount :Int get() {
        return getData().fold(0){count,item->
            if(item.isSelected()) count+1 else count
        }
    }

    val selectedTags:List<T> get() {
        return getData().filter { it.isSelected() }
    }

    private val selectedTagLinkedList :LinkedList<T> by lazy {
        LinkedList()
    }

    init {

        selectedTagLinkedList.addAll(list.filter { it.isSelected() })

        setOnItemClickListener { _, item, position ->
            if(item.isSelected()){
                if(enableUnselect){
                    item.setSelected(false)
                    notifyItemChanged(position)
                    selectedTagLinkedList.remove(item)
                    onSelectedChangedCallback?.invoke(selectedTags,selectedCount)
                }
            }else{
                if(selectedCount>=maxSelectCount){
                    if(unselectOldestWhenExceed){
                        selectedTagLinkedList.removeFirst().setSelected(false)
                        item.setSelected(true)
                        notifyDataSetChanged()
                        selectedTagLinkedList.addLast(item)
                        onSelectedChangedCallback?.invoke(selectedTags,selectedCount)
                    }else{
                        actionWhenExceedMaxCount?.invoke(maxSelectCount)
                    }
                }else{
                    item.setSelected(true)
                    notifyItemChanged(position)
                    selectedTagLinkedList.addLast(item)
                    onSelectedChangedCallback?.invoke(selectedTags,selectedCount)
                }
            }
        }
    }


}