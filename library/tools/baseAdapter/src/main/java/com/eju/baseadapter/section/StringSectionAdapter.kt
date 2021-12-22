package com.eju.baseadapter.section

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eju.baseadapter.BaseViewHolder
import java.util.*
import kotlin.Comparator

/**
 * 使用T的某个String字段进行排序
 */
abstract class StringSectionAdapter<T,SB: ViewBinding,IB: ViewBinding>:SectionAdapter<T,String,String,SB,IB>() {

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