package com.eju.demomodule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.demomodule.R
import com.eju.demomodule.databinding.ActivityRoomDemoBinding

@Route(path = PagePath.DemoModule.RoomDemo)
class RoomDemoActivity : AppBaseActivity<ActivityRoomDemoBinding>() {
    override fun observe() {
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
    }
}