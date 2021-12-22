package com.eju.baseadapter.section

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.eju.baseadapter.BaseViewHolder
import com.eju.baseadapter.ItemDelegate
import com.eju.baseadapter.OnItemClickListener
import com.eju.baseadapter.WrappedAdapter
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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

     private var onSectionItemClickListener :OnItemClickListener<T>? =null

     private var onSectionClickListener :OnItemClickListener<K>? =null

    private val mainHandler :Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val executorService:ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

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

        setOnItemClickListener { viewHolder, item, position ->
            if(item is SectionItem.Item<*,*,*>){
                onSectionItemClickListener?.invoke(viewHolder,item.item as T,position)
            }else if(item is SectionItem.Section<*>){
                onSectionClickListener?.invoke(viewHolder,item.section as K,position)
            }
        }
    }

    open fun setOnSectionItemClickListener(onSectionItemClickListener: OnItemClickListener<T>) {
        this.onSectionItemClickListener = onSectionItemClickListener
    }

    open fun setOnSectionClickListener(onSectionClickListener: OnItemClickListener<K>) {
        this.onSectionClickListener = onSectionClickListener
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

    /**
     * 列表使用S字段进行排序的Comparator
     */
    abstract fun getSectionComparator():Comparator<S>

    private val itemComparator :Comparator<SectionItem.Item<T,S,K>> by lazy {
        object:Comparator<SectionItem.Item<T,S,K>>{

            private val sectionComparator = getSectionComparator()

            override fun compare(o1: SectionItem.Item<T, S,K>?, o2: SectionItem.Item<T, S,K>?): Int {
                return sectionComparator.compare(o1?.sort,o2?.sort)
            }
        }
    }

    /**
     * section分组列表
     */
    private val sectionList :MutableList<K> by lazy {
        mutableListOf()
    }

    /**
     * section分组和其对应的Position
     * key: section
     * value: section对应的position
     */
    private val sectionPositionMap :HashMap<K,Int> by lazy {
        hashMapOf()
    }

    /**
     * 获取position所在的section的分组position
     */
    fun getSectionPositionFromItemPosition(position: Int):Int{
        return when (val item = getItem(position)) {
            is SectionItem.Section<*> -> {
                position
            }
            is SectionItem.Item<*,*,*> -> {
                sectionPositionMap[item.section]?:RecyclerView.NO_POSITION
            }
            else -> {
                RecyclerView.NO_POSITION
            }
        }
    }

    /**
     * 获取position所在的section的分组K
     */
    fun getSectionValueFromItemPosition(position: Int):K{
        return when (val item = getItem(position)) {
            is SectionItem.Section<*> -> {
                item.section as K
            }
            is SectionItem.Item<*,*,*> -> {
                item.section as K
            }
            else -> {
               throw NullPointerException("")
            }
        }
    }

    /**
     * 通过section获取对应的position
     */
    fun getPositionFromSection(section: K) :Int? = sectionPositionMap[section]

    fun isSectionItem(position: Int) :Boolean {
        return sectionPositionMap.values.contains(position)
    }

    fun notifyDataSetChanged(list: List<T> , showSection:Boolean = true , sectionsCallback:(List<K>)->Unit = {}){
        executorService.submit {
            //step1.获取每个item的ceiling值
            val wrappedList = list.map {
                val sortedValue = getSortedValue(it)
                SectionItem.Item(it,sortedValue,getSectionValue(sortedValue))
            }

            //step2.根据ceiling值对列表进行排序
            val sortedList = wrappedList.sortedWith(itemComparator)

            if(!showSection){
                mainHandler.post {
                    notifyDataSetChanged(true,sortedList,false)
                }
                return@submit
            }

            //step3.获取所有不重复的ceiling列表和每个ceiling出现的position
            val sectionPositionList = mutableListOf<Int>()
            val sectionList = mutableListOf<K>()
            sortedList.forEachIndexed { index, item ->
                val ceiling = item.section
                if(!sectionList.contains(ceiling)){
                    sectionList.add(ceiling)
                    sectionPositionList.add(index)
                }
            }
            Timber.i("sectionPositionList-->${sectionPositionList}")
            Timber.i("sectionList-->${sectionList}")

            //step4.组装item和ceiling成列表数据
            val adapterList :MutableList<SectionItem> = ArrayList(sortedList)
            sectionPositionList.reverse() //从后向前添加ceiling
            sectionList.reverse()
            sectionPositionList.forEachIndexed { index, position ->
                val ceiling = SectionItem.Section(sectionList[index])
                adapterList.add(position,ceiling)
            }

            //step5.获取分组列表和分组对应的position
            this.sectionList.clear()
            this.sectionPositionMap.clear()
            adapterList.forEachIndexed { index, sectionItem ->
                if(sectionItem is SectionItem.Section<*>){
                    val section = sectionItem.section as K
                    this.sectionList.add(section)
                    this.sectionPositionMap[section] = index
                }
            }
            Timber.i("sectionPositionMap-->${sectionPositionMap}")
            Timber.i("sectionList-->${this.sectionList}")
            mainHandler.post {
                sectionsCallback.invoke(this.sectionList.toList())  //toList(),生成一个新的List,避免数据被修改
            }

            //step6.notify
            mainHandler.post {
                notifyDataSetChanged(true,adapterList,false)
            }
        }
    }

}