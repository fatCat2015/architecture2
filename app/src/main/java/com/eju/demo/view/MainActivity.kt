package com.eju.demo.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.eju.appbase.base.AppBaseActivity
import com.eju.architecture.baseViewModels
import com.eju.demo.databinding.ActivityMainBinding
import com.eju.demo.viewModel.MainViewModel
import com.eju.tools.doOnClick
import com.eju.tools.requestPermissions
import com.eju.tools.saveSerializable
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
            startActivity(Intent(this, UserListActivity::class.java))
        }

        binding.tv2.doOnClick {
            startActivity(Intent(this, MomentListActivity::class.java))
        }

        binding.tv3.setOnClickListener {
        }

        binding.tv4.doOnClick {
            requestPermissions(Manifest.permission.CAMERA) {
                showToast("granted")
            }
        }

        binding.tv5.doOnClick {
        }
    }





}
