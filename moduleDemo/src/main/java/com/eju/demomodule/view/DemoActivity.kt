package com.eju.demomodule.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.newFragment
import com.eju.demomodule.databinding.ActivityDemoBinding
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
            newFragment<DemoFragment>(PagePath.DemoModule.DemoFragment),
            newFragment<CoilFragment>(PagePath.DemoModule.CoilFragment),
            newFragment<UserListFragment>(PagePath.DemoModule.UserList),
            newFragment<MomentListFragment>(PagePath.DemoModule.MomentList)
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