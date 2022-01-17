package com.eju.baseadapter.tag

interface ITagItem {
    fun isSelected():Boolean
    fun setSelected(selected:Boolean)
    fun isUnLimit():Boolean = false
}