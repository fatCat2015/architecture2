package com.eju.start.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.aRouter
import com.eju.architecture.baseViewModels
import com.eju.start.databinding.ActivityGuideBinding
import com.eju.start.viewModel.GuideViewModel
import com.eju.tools.doOnClick
import com.eju.tools.finishAllActivitiesExceptTop
import dagger.hilt.android.AndroidEntryPoint

@Route(path = PagePath.Start.Guide)
@AndroidEntryPoint
class GuideActivity:AppBaseActivity<ActivityGuideBinding>() {

    private val viewModel by baseViewModels<GuideViewModel>()

    override fun showTitle(): Boolean {
        return false
    }

    override fun observe() {
        viewModel.toLogin.observe(this){
            aRouter.build(PagePath.Start.Login).navigation(this)
        }
        viewModel.toMain.observe(this){
            aRouter.build(PagePath.Main.Home).navigation(this)
        }
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        finishAllActivitiesExceptTop()
        binding.tvAction.doOnClick {
            viewModel.navigation()
        }
    }
}