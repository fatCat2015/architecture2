package com.eju.demomodule.titles

import android.app.Activity
import com.eju.architecture.core.ITitleView
import com.eju.demomodule.databinding.LayoutWebTitleBinding
import com.eju.tools.doOnClick
import timber.log.Timber

class WebTitle(private val activity: Activity):ITitleView<LayoutWebTitleBinding> {

    override val bindingCreator: () -> LayoutWebTitleBinding
        get() = {
            LayoutWebTitleBinding.inflate(activity.layoutInflater)
        }

    override val binding: LayoutWebTitleBinding by lazy {
        bindingCreator().also {
            Timber.i("WebTitle init viewBinding ${it}")
        }
    }

    override fun onBindView(binding: LayoutWebTitleBinding) {
        binding.ibClose.doOnClick {
            activity.finish()
        }
        binding.ibBack.doOnClick {
            activity.onBackPressed()
        }
        binding.tvTitle.text = activity.title
    }
}