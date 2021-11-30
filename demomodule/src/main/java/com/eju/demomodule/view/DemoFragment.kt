package com.eju.demomodule.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseLazyLoadFragment
import com.eju.appbase.router.PagePath
import com.eju.demomodule.databinding.FragmentDemoBinding
import com.eju.tools.doOnClick

@Route(path = PagePath.DemoModule.DemoFragment)
class DemoFragment:AppBaseLazyLoadFragment<FragmentDemoBinding>() {


    override fun observeLazy() {
    }

    override fun lazyLoad(savedInstanceState: Bundle?) {
    }

    override fun setListenersLazy() {
        binding.btWeb.doOnClick {
            ARouter.getInstance().build(PagePath.DemoModule.WebPage)
                .withString("url","https://www.baidu.com")
                .navigation()
        }
    }
}