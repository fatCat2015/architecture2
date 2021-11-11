package com.eju.appbase.base

import android.app.Activity
import com.eju.appbase.databinding.LayoutCommonTitleBinding
import com.eju.architecture.base.ITitleView


open class CommonTitleView(private val activity: Activity): ITitleView<LayoutCommonTitleBinding> {

    override val binding: LayoutCommonTitleBinding
        get() = LayoutCommonTitleBinding.inflate(activity.layoutInflater)


    override fun onBindView(binding: LayoutCommonTitleBinding) {
        binding.ibAppTitleLeft.setOnClickListener { activity.onBackPressed() }
        binding.tvAppTitle.text = activity.title
    }

}