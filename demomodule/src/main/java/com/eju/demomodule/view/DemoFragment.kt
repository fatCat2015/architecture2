package com.eju.demomodule.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseLazyLoadFragment
import com.eju.appbase.entity.User
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.aRouter
import com.eju.architecture.baseViewModels
import com.eju.demomodule.databinding.FragmentDemoBinding
import com.eju.demomodule.viewmodel.DemoViewModel
import com.eju.tools.doOnClick
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@Route(path = PagePath.DemoModule.DemoFragment)
class DemoFragment:AppBaseLazyLoadFragment<FragmentDemoBinding>() {


    private val viewModel by baseViewModels<DemoViewModel>()

    override fun observeLazy() {
        viewModel.orderDetail.observe(this){
            Timber.i("order:${it}")
        }
    }

    override fun lazyLoad(savedInstanceState: Bundle?) {
    }

    override fun setListenersLazy() {
        binding.btWeb.doOnClick {
            aRouter.build(PagePath.DemoModule.WebPage)
                .withString("url","https://www.baidu.com")
                .navigation()
        }
        binding.btArouter.doOnClick {
            aRouter.build(PagePath.DemoModule.ARouterDemo)
                .withString("number","9527")
                .withSerializable("user",User("10","sck"))
                .withSerializable("users", arrayListOf(User("10","sck")))
                .navigation()
        }

        binding.btApi.doOnClick {
            viewModel.queryOrderDetail("89")
        }
    }
}