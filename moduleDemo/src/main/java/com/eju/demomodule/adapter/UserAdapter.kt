package com.eju.demomodule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.eju.baseadapter.BaseViewHolder
import com.eju.baseadapter.ItemDelegate
import com.eju.baseadapter.WrappedAdapter
import com.eju.demomodule.databinding.ItemUser0Binding
import com.eju.demomodule.databinding.ItemUser1Binding
import com.eju.demomodule.entity.User

class UserAdapter(data:List<User>):WrappedAdapter<User>(data) {

    init {

        addItemDelegate(object:ItemDelegate<User,ItemUser0Binding>(){
            override fun getLayoutViewBinding(parent: ViewGroup): ItemUser0Binding {
                return ItemUser0Binding.inflate(LayoutInflater.from(parent.context),parent,false)
            }
            override fun apply(item: User,position:Int): Boolean {
                return item.id%2 ==0
            }
            override fun bindData(
                viewHolder: BaseViewHolder<ItemUser0Binding>,
                item: User,
                position: Int
            ) {
                viewHolder.binding.tv.text = item.name
                viewHolder.binding.tvId.text = "id:${item.id}"
            }

            override fun bindData(
                viewHolder: BaseViewHolder<ItemUser0Binding>,
                item: User,
                position: Int,
                payloads: List<Any>
            ) {
                viewHolder.binding.tv.text = item.name
            }

        })

        addItemDelegate(object:ItemDelegate<User, ItemUser1Binding>(){
            override fun getLayoutViewBinding(parent: ViewGroup): ItemUser1Binding {
                return ItemUser1Binding.inflate(LayoutInflater.from(parent.context),parent,false)
            }
            override fun apply(item: User,position:Int): Boolean {
                return item.id%2 !=0
            }
            override fun bindData(
                viewHolder: BaseViewHolder<ItemUser1Binding>,
                item: User,
                position: Int
            ) {
                viewHolder.binding.tv.text = item.name
                viewHolder.binding.tvId.text = "id:${item.id}"
            }

            override fun bindData(
                viewHolder: BaseViewHolder<ItemUser1Binding>,
                item: User,
                position: Int,
                payloads: List<Any>
            ) {
                viewHolder.binding.tv.text = item.name
            }
        })
    }
}