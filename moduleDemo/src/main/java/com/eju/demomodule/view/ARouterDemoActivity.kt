package com.eju.demomodule.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.entity.User
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.aRouter
import com.eju.appbase.router.rawUri
import com.eju.demomodule.databinding.ActivityArouterDemoBinding
import com.eju.tools.doOnClick
import com.eju.tools.intentExtra
import timber.log.Timber

@Route(path = PagePath.DemoModule.ARouterDemo)
class ARouterDemoActivity:AppBaseActivity<ActivityArouterDemoBinding>() {


    private val number:String? by intentExtra<String>("number")



    override fun observe() {

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        binding.btDegrade.doOnClick {
            aRouter.build(PagePath.Main.Home).navigation(this)
        }
    }
}