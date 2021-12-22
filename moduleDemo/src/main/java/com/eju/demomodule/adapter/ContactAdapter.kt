package com.eju.demomodule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.eju.baseadapter.BaseViewHolder
import com.eju.baseadapter.section.StringSectionAdapter
import com.eju.demomodule.databinding.ItemContactBinding
import com.eju.demomodule.databinding.ItemContactSectionBinding
import com.eju.demomodule.entity.Contact
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import java.lang.StringBuilder
import java.util.*

class ContactAdapter:StringSectionAdapter<Contact,ItemContactSectionBinding,ItemContactBinding,>() {

    override fun getLayoutViewBinding(parent: ViewGroup): ItemContactBinding {
        return ItemContactBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun getSectionViewBinding(parent: ViewGroup): ItemContactSectionBinding {
        return ItemContactSectionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bindData(
        viewHolder: BaseViewHolder<ItemContactBinding>,
        item: Contact,
        position: Int
    ) {
        viewHolder.binding.let {
            it.tvName.text = item.name
            it.tvMobile.text = item.mobile
        }
    }

    override fun bindSection(
        viewHolder: BaseViewHolder<ItemContactSectionBinding>,
        section: String,
        position: Int
    ) {
        viewHolder.binding.tvLetter.text = section
    }

    override fun getSectionValue(sortedValue: String): String {
        return sortedValue[0].toString().uppercase(Locale.getDefault())
    }

    override fun getSortedValue(item: Contact): String {
        return str2Pinyin(item.name)
    }



    private val hanyuPinyinOutputFormat:HanyuPinyinOutputFormat by lazy {
        HanyuPinyinOutputFormat().apply {
            caseType = HanyuPinyinCaseType.LOWERCASE
            toneType = HanyuPinyinToneType.WITHOUT_TONE
        }
    }

    private fun str2Pinyin(str:String):String{
        if(str.isEmpty()){
            return ""
        }
        val strCharArray = str.toCharArray()
        val strBuilder = StringBuilder()
        strCharArray.forEach { char->
            strBuilder.append(PinyinHelper.toHanyuPinyinStringArray(char,hanyuPinyinOutputFormat)?.let {
                if(it.isEmpty()){
                    char.toString()
                }else{
                    it.first()
                }
            }?:char.toString())

        }
        return strBuilder.toString()
    }
}