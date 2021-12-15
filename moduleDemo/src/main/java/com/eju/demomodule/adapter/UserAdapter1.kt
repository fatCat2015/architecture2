package com.eju.demomodule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eju.baseadapter.WrappedAdapter
import com.eju.demomodule.R
import com.eju.demomodule.databinding.ItemDemoBinding
import com.eju.demomodule.databinding.ItemUser0Binding
import com.eju.demomodule.databinding.ItemUser1Binding
import com.eju.demomodule.entity.User
import timber.log.Timber

class UserAdapter1(private val data:List<User>):RecyclerView.Adapter< RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        val rootView = if(viewType==0)
            ItemUser0Binding.inflate(LayoutInflater.from(parent.context),parent,false).root
        else
            ItemUser1Binding.inflate(LayoutInflater.from(parent.context),parent,false).root
        return  object :RecyclerView.ViewHolder(rootView){

        }
    }

    override fun getItemViewType(position: Int): Int {
        val user =data[position]
        return user.id.toInt()%2
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val tv = holder.itemView.findViewById<TextView>(R.id.tv)
        tv.text = data[position].name
        Timber.tag("BaseAdapter").i("onBindViewHolder:${position}")
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
