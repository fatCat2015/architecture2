package com.eju.appbase.router

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter

class PageRouterActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let { uri->
            ARouter.getInstance().build(uri).navigation()
            finish()
        }?:finish()
    }
}