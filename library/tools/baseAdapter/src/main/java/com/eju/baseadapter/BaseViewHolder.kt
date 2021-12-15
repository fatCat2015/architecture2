package com.eju.baseadapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<B:ViewBinding>(val binding:B): RecyclerView.ViewHolder(binding.root)