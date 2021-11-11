package com.eju.appbase.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.eju.appbase.R
import com.eju.architecture.base.IPagingBehavior
import com.scwang.smart.refresh.layout.SmartRefreshLayout

abstract class AppBasePagingActivity<V:ViewBinding>:AppBaseActivity<V>(),IPagingBehavior {

    open val refreshLayoutId get() = R.id.refreshLayout

    protected val refreshLayout:SmartRefreshLayout? by lazy {
        binding.root.findViewById(refreshLayoutId)
    }

    abstract val adapterProducer:()->RecyclerView.Adapter<*>

    protected val adapter:RecyclerView.Adapter<*> by lazy {
        adapterProducer()
    }

    override fun finishRefresh() {
        refreshLayout?.finishRefresh()
    }

    override fun finishLoadMore() {
        refreshLayout?.finishLoadMore()
    }

    override fun setEnableLoadMore(enabled: Boolean) {
        refreshLayout?.setEnableAutoLoadMore(enabled)
    }

    override fun showEmptyView(showEmpty: Boolean) {
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun notifyItemChanged(position: Int, itemCount: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position,itemCount,payload)
    }

    override fun notifyItemInserted(position: Int, itemCount: Int) {
        adapter.notifyItemRangeInserted(position,itemCount)
    }

    override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition,toPosition)
    }

    override fun notifyItemRemoved(position: Int, itemCount: Int) {
        adapter.notifyItemRangeRemoved(position,itemCount)
    }



}