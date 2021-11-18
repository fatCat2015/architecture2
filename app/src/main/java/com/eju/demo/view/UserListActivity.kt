package com.eju.demo.view

import android.os.Bundle
import com.eju.appbase.base.AppBasePagingActivity
import com.eju.architecture.baseViewModels
import com.eju.demo.adapter.UserAdapter
import com.eju.demo.databinding.ActiivtyListBinding
import com.eju.demo.viewModel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserListActivity:AppBasePagingActivity<ActiivtyListBinding>() {


    private val viewModel:UserListViewModel by baseViewModels()


    private val adapter:UserAdapter  by lazy {
        UserAdapter(viewModel.list).apply {
            Timber.i("adapter :${this}")
        }
    }


    override fun afterCreate(savedInstanceState: Bundle?) {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }.setOnLoadMoreListener {
            viewModel.loadMore()
        }
        binding.rv.adapter=adapter
        binding.refreshLayout.autoRefresh()
    }

    override fun observe() {

    }

    override fun finishRefresh() {
        binding.refreshLayout.finishRefresh()
    }

    override fun finishLoadMore() {
        binding.refreshLayout.finishLoadMore()
    }

    override fun setEnableLoadMore(enabled: Boolean) {
        binding.refreshLayout.setEnableLoadMore(enabled)
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }




}