package com.eju.demomodule.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.newFragment
import com.eju.demomodule.databinding.ActivityDemoBinding
import com.eju.tools.UnreadMessageHandler
import com.eju.tools.moveTaskToBackWhenPressBack
import com.eju.tools.setUpWithViewPager2
import com.eju.tools.widget.SimpleFragmentAdapter2
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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