package com.eju.demo

import android.content.Intent
import android.os.Bundle
import com.eju.appbase.base.AppBaseActivity
import com.eju.architecture.baseViewModels
import com.eju.architecture.global.*
import com.eju.demo.databinding.ActivityMainBinding
import com.eju.demo.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppBaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by baseViewModels()


    override fun observe() {
    }

    override fun afterCreate(savedInstanceState: Bundle?) {

        viewModel.helpDetail.observe(this){
            saveSerializable("12212",it)
        }

        binding.tv.doOnClick {
            viewModel.queryHelpDetail("58")
        }

        binding.tv1.doOnClick {
            startActivity(Intent(this,UserListActivity::class.java))
        }

    }

}
