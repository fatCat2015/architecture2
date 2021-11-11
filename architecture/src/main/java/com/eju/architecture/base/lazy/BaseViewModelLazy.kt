package com.eju.architecture.base.lazy

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.eju.architecture.base.BaseViewModel
import com.eju.architecture.base.IPagingBehavior
import com.eju.architecture.base.IViewBehavior
import com.eju.architecture.base.paging.BasePagePagingViewModel
import kotlin.reflect.KClass


class BaseViewModelLazy<VM : BaseViewModel> (
    private val viewModelClass: KClass<VM>,
    private val viewBehavior: IViewBehavior,
    private val pagingViewBehavior: IPagingBehavior?,
    private val lifecycleOwner: LifecycleOwner,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {

    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val store = storeProducer()
                val factory = factoryProducer()
                ViewModelProvider(store, factory).get(viewModelClass.java).also {
                    lifecycleOwner.lifecycle.addObserver(it)
                    observeViewLiveData(it)
                    observePagingViewLiveData(it)
                    cached = it
                }
            } else {
                viewModel
            }
        }

    private fun observeViewLiveData(viewModel:BaseViewModel){
        viewModel.showLoadingLD.observe(lifecycleOwner){
            viewBehavior.showLoading(it)
        }
        viewModel.hideLoadingLD.observe(lifecycleOwner){
            viewBehavior.hideLoading()
        }
        viewModel.showToastLD.observe(lifecycleOwner){
            if(it.msg.isNullOrEmpty()){
                viewBehavior.showToast(it.resId,it.duration)
            }else{
                viewBehavior.showToast(it.msg,it.duration)
            }
        }
        viewModel.showSnackLD.observe(lifecycleOwner){
            if(it.msg.isNullOrEmpty()){
                viewBehavior.showSnack(it.resId,it.duration,it.code)
            }else{
                viewBehavior.showSnack(it.msg,it.duration,it.code)
            }

        }
    }

    private fun observePagingViewLiveData(viewModel:BaseViewModel){
        (viewModel as? BasePagePagingViewModel<*>)?.let { pagingViewModel->
            pagingViewBehavior?.let { pagingViewBehavior->
                pagingViewModel.finishRefreshLD.observe(lifecycleOwner){
                    pagingViewBehavior.finishRefresh()
                }
                pagingViewModel.finishLoadMoreLD.observe(lifecycleOwner){
                    pagingViewBehavior.finishLoadMore()
                }
                pagingViewModel.setEnableLoadMoreLD.observe(lifecycleOwner){
                    pagingViewBehavior.setEnableLoadMore(it)
                }
                pagingViewModel.showEmptyViewLD.observe(lifecycleOwner){
                    pagingViewBehavior.showEmptyView(it)
                }
                pagingViewModel.notifyDataSetChangedLD.observe(lifecycleOwner){
                    pagingViewBehavior.notifyDataSetChanged()
                }
                pagingViewModel.notifyItemChangedLD.observe(lifecycleOwner){
                    pagingViewBehavior.notifyItemChanged(it.position,it.itemCount,it.payload)
                }
                pagingViewModel.notifyItemInsertedLD.observe(lifecycleOwner){
                    pagingViewBehavior.notifyItemInserted(it.position,it.itemCount)
                }
                pagingViewModel.notifyItemRemovedLD.observe(lifecycleOwner){
                    pagingViewBehavior.notifyItemRemoved(it.position,it.itemCount)
                }
                pagingViewModel.notifyItemMovedLD.observe(lifecycleOwner){
                    pagingViewBehavior.notifyItemMoved(it.fromPosition,it.toPosition)
                }
            }
        }

    }

    override fun isInitialized(): Boolean = cached != null
}