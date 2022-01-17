package com.eju.main.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.core.baseViewModels
import com.eju.main.databinding.ActivityMainBinding
import com.eju.main.viewmodel.MainViewModel
import com.eju.tools.finishAllActivitiesExceptTop
import com.eju.tools.pressBackTwiceToExitApp
import dagger.hilt.android.AndroidEntryPoint

@Route(path = PagePath.Main.Home)
@AndroidEntryPoint
class MainActivity : AppBaseActivity<ActivityMainBinding>() {

    private val viewModel by baseViewModels<MainViewModel>()

    override fun observe() {

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        finishAllActivitiesExceptTop()
        pressBackTwiceToExitApp {
            showToast("再按一次退出应用")
        }
    }
}