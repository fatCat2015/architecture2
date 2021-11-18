package com.eju.demo.view

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import com.eju.appbase.base.AppBaseActivity
import com.eju.demo.databinding.ActivityMain2Binding
import com.eju.demo.viewModel.MainViewModel
import com.eju.tools.requestPermissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity2 : AppBaseActivity<ActivityMain2Binding>() {


    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun observe() {
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        requestPermissions(Manifest.permission.CAMERA) {
            showToast("granted")
        }
    }
}