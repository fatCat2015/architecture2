package com.eju.baseadapter.section

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eju.baseadapter.BaseViewHolder
import com.eju.baseadapter.ItemDelegate
import com.eju.baseadapter.WrappedAdapter
import timber.log.Timber

/**
 * 泛型说明
 * T:  item数据类型
 * S:  item排序数据类型
 * K:  展示section的数据类型
 * SB: 分组viewBinding
 * IB: item viewBinding
 *
 */
abstract class SectionAdapter<T,S,K,SB:ViewBinding,IB:ViewBinding>:WrappedAdapter<SectionItem>() {

    init {

        addItemDelegate(object:ItemDelegate<SectionItem,SB>(){
            override fun getLayoutViewBinding(parent: ViewGroup): SB {
                return  this@SectionAdapter.getSectionViewBinding(parent)
            }

            override fun apply(item: SectionItem, position: Int): Boolean {
                return item is SectionItem.Section<*>
            }

            override fun bindData(viewHolder: BaseViewHolder<SB>, item: SectionItem, position: Int) {
                this@SectionAdapter.bindSection(viewHolder,(item as SectionItem.Section<*>).section as K,position)
            }

            override fun bindData(viewHolder: BaseViewHolder<SB>, item: SectionItem, position: Int, payloads: List<Any>) {
            }

            override fun clickAble(): Boolean {
                return this@SectionAdapter.sectionClickable()
            }
        })


        addItemDelegate(object:ItemDelegate<SectionItem,IB>(){

            override fun getLayoutViewBinding(parent: ViewGroup): IB {
                return  this@SectionAdapter.getLayoutViewBinding(parent)
            }

            override fun apply(item: SectionItem, position: Int): Boolean {
                return item is SectionItem.Item<*,*,*>
            }

            override fun bindData(viewHolder: BaseViewHolder<IB>, item: SectionItem, position: Int) {
                this@SectionAdapter.bindData(viewHolder,(item as SectionItem.Item<*,*,*>).item as T,position)
            }

            override fun bindData(viewHolder: BaseViewHolder<IB>, item: SectionItem, position: Int, payloads: List<Any>) {
                this@SectionAdapter.bindData(viewHolder,(item as SectionItem.Item<*,*,*>).item as T,position,payloads)
            }

            override fun clickAble(): Boolean {
                return this@SectionAdapter.clickAble()
            }
        })

    }


    abstract fun getLayoutViewBinding(parent: ViewGroup): IB

    abstract fun getSectionViewBinding(parent: ViewGroup): SB

    abstract fun bindData(viewHolder: BaseViewHolder<IB>, item: T, position: Int)

    open fun bindData(viewHolder: BaseViewHolder<IB>, item: T, position: Int, payloads: List<Any>){

    }

    abstract fun bindSection(viewHolder: BaseViewHolder<SB>, section: K, position: Int)


    open fun clickAble(): Boolean{
        return true
    }

    open fun sectionClickable():Boolean = true

    /**
     * 用于排序的字段
     */
    abstract fun getSortedValue(item:T):S

    /**
     * section展示的字段
     */
    abstract fun getSectionValue(sortedValue:S):K

    abstract fun getSectionComparator():Comparator<S>

    private val itemComparator :Comparator<SectionItem.Item<T,S,K>> by lazy {
        object:Comparator<SectionItem.Item<T,S,K>>{

            private val sectionComparator = getSectionComparator()

            override fun compare(o1: SectionItem.Item<T, S,K>?, o2: SectionItem.Item<T, S,K>?): Int {
                return sectionComparator.compare(o1?.sort,o2?.sort)
            }
        }
    }

    private val sectionList :MutableList<K> by lazy {
        mutableListOf()
    }


    fun notifyDataSetChanged(list: List<T>){
        val start = System.currentTimeMillis()
        //step1.获取每个item的ceiling值
        val wrappedList = list.map {
            val sortedValue = getSortedValue(it)
            SectionItem.Item(it,sortedValue,getSectionValue(sortedValue))
        }

        //step2.根据ceiling值对列表进行排序
        val sortedList = wrappedList.sortedWith(itemComparator)

        //step3.获取所有不重复的ceiling列表和每个ceiling出现的position
        sectionList.clear()
        val sectionPositionList = mutableListOf<Int>()
        sortedList.forEachIndexed { index, item ->
            val ceiling = item.section
            if(!sectionList.contains(ceiling)){
                sectionList.add(ceiling)
                sectionPositionList.add(index)
            }
        }

        Timber.i("${sectionPositionList}")
        Timber.i("${sectionList}")
        //step4.组装item和ceiling成列表数据
        val adapterList :MutableList<SectionItem> = ArrayList(sortedList)
        sectionPositionList.reverse() //从后向前添加ceiling
        sectionPositionList.forEachIndexed { index, position ->
            val ceiling = SectionItem.Section(sectionList[sectionList.size-1-index])
            adapterList.add(position,ceiling)
        }

        Timber.i("${System.currentTimeMillis()-start}")
        //step5.notify

        notifyDataSetChanged(true,adapterList,false)
    }

}