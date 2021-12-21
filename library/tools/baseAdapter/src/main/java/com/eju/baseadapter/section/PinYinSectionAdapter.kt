package com.eju.baseadapter.section

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eju.baseadapter.BaseViewHolder
import java.util.*
import kotlin.Comparator

/**
 * 使用T的某个字段的拼音进行排序
 * section展示分组的大些首字母
 */
abstract class PinYinSectionAdapter<T,SB: ViewBinding,IB: ViewBinding>:SectionAdapter<T,String,String,SB,IB>() {

    override fun getSortedValue(item: T): String {
        return getSortedValuePinYin(item)
    }

    abstract fun getSortedValuePinYin(item: T):String

    override fun getSectionValue(sortedValue: String): String {
        return sortedValue.uppercase(Locale.getDefault())[0].toString()
    }

    override fun getSectionComparator(): Comparator<String> {
        return Comparator { o1, o2 ->
            if(o1==null || o2==null){
                0
            }else{
                o1.compareTo(o2)
            }
        }
    }
}