package com.eju.demomodule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eju.baseadapter.BaseViewHolder
import com.eju.baseadapter.SimpleAdapter
import com.eju.demomodule.databinding.ItemDemoBinding

class DemoAdapter(private val list:List<Any>) :SimpleAdapter<Any,ItemDemoBinding>(list){

    override fun getLayoutViewBinding(parent: ViewGroup): ItemDemoBinding {
        return ItemDemoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bindData(viewHolder: BaseViewHolder<ItemDemoBinding>, item: Any, position: Int) {
        viewHolder.binding.tv.text = item.toString()
    }

}
