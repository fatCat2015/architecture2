package com.eju.appbase.router

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.BuildConfig
import com.eju.appbase.R
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.databinding.ActivityPageNotFoundBinding
import com.eju.tools.intentExtra

@Route(path = PagePath.Other.NotFound)
class PageNotFoundActivity:AppBaseActivity<ActivityPageNotFoundBinding>() {

    private val targetPath by intentExtra<String>("path")

    override fun observe() {

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        if(BuildConfig.DEBUG){
            binding.tvError.text = "${getString(R.string.page_not_found)}\ntargetPath:${targetPath}"
        }
    }
}