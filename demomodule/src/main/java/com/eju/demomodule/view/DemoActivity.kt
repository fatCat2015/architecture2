package com.eju.demomodule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.demomodule.databinding.ActivityDemoBinding
import com.eju.demomodule.view.CoilFragment
import com.eju.demomodule.view.MomentListFragment
import com.eju.demomodule.view.UserListFragment
import com.eju.tools.setUpWithViewPager2
import com.eju.tools.widget.SimpleFragmentAdapter2
import dagger.hilt.android.AndroidEntryPoint

@Route(path=PagePath.DemoModule.Demo)
@AndroidEntryPoint
class DemoActivity : AppBaseActivity<ActivityDemoBinding>() {

    override fun observe() {

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        val fragments = listOf(
            ARouter.getInstance().build(PagePath.DemoModule.DemoFragment).navigation() as Fragment,
            ARouter.getInstance().build(PagePath.DemoModule.CoilFragment).navigation() as Fragment,
            ARouter.getInstance().build(PagePath.DemoModule.UserList).navigation() as Fragment,
            ARouter.getInstance().build(PagePath.DemoModule.MomentList).navigation() as Fragment,
        )
        binding.viewPager2.adapter = SimpleFragmentAdapter2(fragments,this)
        binding.viewPager2.offscreenPageLimit = fragments.size-1
        binding.tabLayout.setUpWithViewPager2(binding.viewPager2, listOf(
            "demo",
            "coil",
            "list",
            "自定义分页"
        ))
    }
}