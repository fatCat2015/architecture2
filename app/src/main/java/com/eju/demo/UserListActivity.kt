package com.eju.demo

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.base.AppBasePagingActivity
import com.eju.architecture.base.IPagingBehavior
import com.eju.architecture.baseViewModels
import com.eju.demo.adapter.UserAdapter
import com.eju.demo.databinding.ActiivtyListBinding
import com.eju.demo.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserListActivity:AppBasePagingActivity<ActiivtyListBinding>() {

    override val refreshLayoutId: Int
        get() = R.id.refreshLayout



    override val adapterProducer: () -> RecyclerView.Adapter<*>
        get() = {
            UserAdapter(viewModel.list).apply {
                Timber.i("adapter :${this}")
            }
        }

    private val viewModel:UserViewModel by baseViewModels()


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




}