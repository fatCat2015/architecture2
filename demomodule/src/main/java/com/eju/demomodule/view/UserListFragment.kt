package com.eju.demomodule.view

import android.os.Bundle
import com.eju.appbase.base.AppBaseLazyLoadFragment
import com.eju.appbase.base.AppBaseLazyLoadPagingFragment
import com.eju.architecture.baseViewModels
import com.eju.demomodule.adapter.DemoAdapter
import com.eju.demomodule.databinding.FragmentCoilBinding
import com.eju.demomodule.databinding.FragmentUserListBinding
import com.eju.demomodule.viewmodel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserListFragment:AppBaseLazyLoadPagingFragment<FragmentUserListBinding>() {


    private val viewModel: UserListViewModel by baseViewModels()


    private val adapter:DemoAdapter  by lazy {
        DemoAdapter(viewModel.list).apply {
            Timber.i("adapter :${this}")
        }
    }

    override fun observeLazy() {

    }

    override fun lazyLoad(savedInstanceState: Bundle?) {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }.setOnLoadMoreListener {
            viewModel.loadMore()
        }
        binding.rv.adapter=adapter
        binding.refreshLayout.autoRefresh()
    }

    override fun setListenersLazy() {
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

    override fun showEmptyView(showEmpty: Boolean) {
    }
}