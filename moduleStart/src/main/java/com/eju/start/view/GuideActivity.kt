package com.eju.start.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.persistence.GUIDE_INDEX
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.aRouter
import com.eju.architecture.baseViewModels
import com.eju.start.BuildConfig
import com.eju.start.databinding.ActivityGuideBinding
import com.eju.start.databinding.ActivityLoginBinding
import com.eju.start.viewModel.GuideViewModel
import com.eju.start.viewModel.LoginViewModel
import com.eju.tools.doOnClick
import com.eju.tools.finishAllActivitiesExceptTop
import com.eju.tools.saveInt
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
            saveInt(GUIDE_INDEX, BuildConfig.guideIndex)
            viewModel.navigation()
        }
    }
}