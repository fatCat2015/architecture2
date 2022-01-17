package com.eju.demomodule.entity

import com.eju.baseadapter.tag.ITagItem

data class Tag(
     val id:String,
     val name:String
):ITagItem {

    private var selectedFlag = false

    override fun isSelected(): Boolean {
        return selectedFlag
    }

    override fun setSelected(selected: Boolean) {
        this.selectedFlag = selected
    }

    override fun isUnLimit(): Boolean {
        return id == "id0"
    }
}