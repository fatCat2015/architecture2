package com.eju.appbase.base

import android.app.Activity
import com.eju.appbase.databinding.LayoutCommonTitleBinding
import com.eju.architecture.core.ITitleView


open class CommonTitleView(private val activity: Activity): ITitleView<LayoutCommonTitleBinding> {

    override val bindingCreator: () -> LayoutCommonTitleBinding
        get() = {
            LayoutCommonTitleBinding.inflate(activity.layoutInflater)
        }

    override val binding: LayoutCommonTitleBinding by lazy {
        bindingCreator()
    }

    override fun onBindView(binding: LayoutCommonTitleBinding) {
        binding.ibAppTitleLeft.setOnClickListener { activity.onBackPressed() }
        binding.tvAppTitle.text = activity.title
    }

}