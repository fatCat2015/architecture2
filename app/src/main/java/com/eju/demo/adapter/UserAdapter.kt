package com.eju.demo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eju.demo.databinding.ItemUserBinding
import com.eju.demo.entity.User

class UserAdapter(private val userList:List<User>) :RecyclerView.Adapter<UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindng.tv.text = userList[position].name
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

class UserViewHolder(val bindng:ItemUserBinding):RecyclerView.ViewHolder(bindng.root)