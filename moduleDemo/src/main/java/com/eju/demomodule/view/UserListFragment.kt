package com.eju.demomodule.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseLazyLoadFragment
import com.eju.appbase.base.AppBaseLazyLoadPagingFragment
import com.eju.appbase.router.PagePath
import com.eju.architecture.baseViewModels
import com.eju.baseadapter.Footer
import com.eju.baseadapter.Header
import com.eju.demomodule.adapter.DemoAdapter
import com.eju.demomodule.adapter.UserAdapter
import com.eju.demomodule.adapter.UserAdapter1
import com.eju.demomodule.databinding.*
import com.eju.demomodule.entity.User
import com.eju.demomodule.viewmodel.UserListViewModel
import com.eju.tools.doOnClick
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@Route(path = PagePath.DemoModule.UserList)
@AndroidEntryPoint
class UserListFragment:AppBaseLazyLoadPagingFragment<FragmentUserListBinding>() {


    private val viewModel: UserListViewModel by baseViewModels()


    private val adapter: UserAdapter by lazy {
        UserAdapter(viewModel.list).apply {
            Timber.i("adapter :${this}")
        }.also {
            it.setOnItemClickListener { viewHolder, item, position ->
                showToast("${item.name} # ${position} # ${it.getItem(position).name}")
            }
        }.also {
            it.addHeader(header0)
            it.addFooter(footer0)
        }
    }

    private val header0 = object:Header<UserHeader0Binding>(){
        override fun getLayoutViewBinding(): UserHeader0Binding {
            return UserHeader0Binding.inflate(layoutInflater)
        }

        override fun onBindView(dataBinding: UserHeader0Binding) {
            dataBinding.tv.doOnClick {
                showToast(dataBinding.tv.text)
            }
        }
    }

    private val header1 = object:Header<UserHeader1Binding>(){
        override fun getLayoutViewBinding(): UserHeader1Binding {
            return UserHeader1Binding.inflate(layoutInflater)
        }

        override fun onBindView(dataBinding: UserHeader1Binding) {
            dataBinding.tv.doOnClick {
                showToast(dataBinding.tv.text)
            }
        }
    }

    private val footer0 = object:Footer<UserFooter0Binding>(){
        override fun getLayoutViewBinding(): UserFooter0Binding {
            return UserFooter0Binding.inflate(layoutInflater)
        }

        override fun onBindView(dataBinding: UserFooter0Binding) {
            dataBinding.tv.doOnClick {
                showToast(dataBinding.tv.text)
            }
        }
    }

    private val footer1 = object:Footer<UserFooter1Binding>(){
        override fun getLayoutViewBinding(): UserFooter1Binding {
            return UserFooter1Binding.inflate(layoutInflater)
        }

        override fun onBindView(dataBinding: UserFooter1Binding) {
            dataBinding.tv.doOnClick {
                showToast(dataBinding.tv.text)
            }
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
        (binding.rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rv.adapter=adapter
        binding.rv.addItemDecoration(object:RecyclerView.ItemDecoration(){

            private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.RED
            }
            private val dividerHeight = 30

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if(position!=RecyclerView.NO_POSITION){
                    if(adapter.isNormalItem(position)){
                        outRect.bottom = dividerHeight
                    }

                }

            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                val childCount = parent.childCount
                for (index in 0 until childCount) {
                    val childView = parent.getChildAt(index)
                    val position = parent.getChildAdapterPosition(childView)
                    if(position!=RecyclerView.NO_POSITION){
                        if(adapter.isNormalItem(position)){
                            c.drawRect(0F,childView.bottom.toFloat(),childView.width.toFloat(),
                                (childView.bottom+dividerHeight).toFloat(),paint)
                        }
                    }
                }

            }
        })
        binding.refreshLayout.autoRefresh()
    }

    override fun setListenersLazy() {
        binding.btAddHeader.doOnClick {
            adapter.addHeader(header1,true)
        }
        binding.btRemoveHeader.doOnClick {
            adapter.removeHeader(header1,true)
        }
        binding.btAddFooter.doOnClick {
            adapter.addFooter(footer1,true)
        }
        binding.btRemoveFooter.doOnClick {
            adapter.removeFooter(footer1,true)
        }
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