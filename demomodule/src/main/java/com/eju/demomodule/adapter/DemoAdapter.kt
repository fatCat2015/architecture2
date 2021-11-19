package com.eju.demomodule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eju.demomodule.databinding.ItemDemoBinding

class DemoAdapter(private val list:List<Any>) :RecyclerView.Adapter<DemoViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        return DemoViewHolder(ItemDemoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        holder.bindng.tv.text = list[position].toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class DemoViewHolder(val bindng:ItemDemoBinding):RecyclerView.ViewHolder(bindng.root)