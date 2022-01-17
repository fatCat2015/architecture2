package com.eju.demomodule.view

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseLazyLoadPagingFragment
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.service.LoginService
import com.eju.architecture.core.baseViewModels
import com.eju.demomodule.adapter.DemoAdapter
import com.eju.demomodule.adapter.LogItemDecoration
import com.eju.demomodule.databinding.FragmentMomentListBinding
import com.eju.demomodule.viewmodel.MomentListViewModel
import com.eju.tools.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@Route(path = PagePath.DemoModule.MomentList)
@AndroidEntryPoint
class MomentListFragment: AppBaseLazyLoadPagingFragment<FragmentMomentListBinding>() {

    private val viewModel: MomentListViewModel by baseViewModels()


    private val adapter: DemoAdapter by lazy {
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
        binding.rv.addItemDecoration(LogItemDecoration())
        binding.rv.addItemDecoration(DividerItemDecoration(dividerSize = 1, dividerColor = Color.RED))
//        binding.rv.addItemDecoration(DividerItemDecoration(dividerWidth = 30, dividerHeight = 45, showDividers = DividerItemDecoration.SHOW_BEGINNING or DividerItemDecoration.SHOW_END))
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
//        binding.refreshLayout.setEnableLoadMore(enabled)
        binding.refreshLayout.setEnableLoadMore(false)
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun showEmptyView(showEmpty: Boolean) {
    }
}