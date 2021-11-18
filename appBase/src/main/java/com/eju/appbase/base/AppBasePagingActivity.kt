package com.eju.appbase.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.eju.appbase.R
import com.eju.architecture.base.IPagingBehavior
import com.scwang.smart.refresh.layout.SmartRefreshLayout

abstract class AppBasePagingActivity<V:ViewBinding>:AppBaseActivity<V>(),IPagingBehavior {


    override fun showEmptyView(showEmpty: Boolean) {
    }


    override fun notifyItemChanged(position: Int, itemCount: Int, payload: Any?) {
    }

    override fun notifyItemInserted(position: Int, itemCount: Int) {
    }

    override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun notifyItemRemoved(position: Int, itemCount: Int) {
    }



}