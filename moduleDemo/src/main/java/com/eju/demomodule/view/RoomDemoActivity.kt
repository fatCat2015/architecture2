package com.eju.demomodule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.baseViewModels
import com.eju.demomodule.R
import com.eju.demomodule.databinding.ActivityRoomDemoBinding
import com.eju.demomodule.viewmodel.RoomDemoViewModel
import com.eju.tools.doOnClick
import timber.log.Timber

@Route(path = PagePath.DemoModule.RoomDemo)

class RoomDemoActivity : AppBaseActivity<ActivityRoomDemoBinding>() {

    private val viewModel by baseViewModels<RoomDemoViewModel>()

    override fun observe() {
        viewModel.demoList.observe(this){
            Timber.i("observe list:${it}")
        }
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        binding.btList.doOnClick {
            viewModel.queryAllDemos()
        }
        binding.btInsert.doOnClick {
            viewModel.insert()
        }
        binding.btDelete.doOnClick {
            viewModel.delete()
        }
        binding.btUpdate.doOnClick {
            viewModel.update()
        }
    }
}