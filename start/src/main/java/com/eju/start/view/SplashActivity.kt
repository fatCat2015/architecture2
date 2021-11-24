package com.eju.start.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.baseViewModels
import com.eju.start.databinding.ActivitySplashBinding
import com.eju.start.viewModel.SplashViewModel
import com.eju.tools.finishAllActivities
import dagger.hilt.android.AndroidEntryPoint

@Route(path = PagePath.Start.Splash)
@AndroidEntryPoint
class SplashActivity:AppBaseActivity<ActivitySplashBinding>() {

    private val viewModel by baseViewModels<SplashViewModel>()

    override fun showTitle(): Boolean {
        return false
    }

    override fun observe() {
        viewModel.toGuide.observe(this){
            ARouter.getInstance().build(PagePath.Start.Guide).navigation(this)
        }
        viewModel.toLogin.observe(this){
            ARouter.getInstance().build(PagePath.Start.Login).navigation(this)
        }
        viewModel.toMain.observe(this){
            ARouter.getInstance().build(PagePath.Main.Home).navigation(this)
        }
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        viewModel.countdownToPage()
    }
}