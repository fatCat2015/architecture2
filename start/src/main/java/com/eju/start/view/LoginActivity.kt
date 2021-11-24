package com.eju.start.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.architecture.baseViewModels
import com.eju.start.databinding.ActivityLoginBinding
import com.eju.start.viewModel.LoginViewModel
import com.eju.tools.doAfterTextChanged
import com.eju.tools.doOnClick
import com.eju.tools.finishAllActivitiesExceptTop
import com.eju.tools.startCountDown
import dagger.hilt.android.AndroidEntryPoint

@Route(path = PagePath.Start.Login)
@AndroidEntryPoint
class LoginActivity:AppBaseActivity<ActivityLoginBinding>() {

    private val viewModel by baseViewModels<LoginViewModel>()

    private val mobile:String  get() {
        return binding.etMobile.text.toString().trim()
    }

    private val code:String  get() {
        return binding.etCode.text.toString().trim()
    }

    private val codeBtnEnable :Boolean get() {
        return mobile.length == 11
    }

    private val loginBtnEnable :Boolean get() {
        return mobile.length == 11 && code.length ==6
    }

    override fun showTitle(): Boolean {
        return false
    }

    override fun observe() {
        viewModel.toMain.observe(this){
            ARouter.getInstance().build(PagePath.Main.Home).navigation(this)
        }
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        finishAllActivitiesExceptTop()
        binding.etMobile.doAfterTextChanged{
            binding.tvCode.isEnabled = codeBtnEnable
            binding.tvAction.isEnabled = loginBtnEnable
        }
        binding.etCode.doAfterTextChanged {
            binding.tvAction.isEnabled = loginBtnEnable
        }
        binding.tvCode.doOnClick {
            showToast("验证码已发送")
            binding.etCode.requestFocus()

            binding.tvCode.startCountDown(this,10,
                onStart = {
                    isEnabled = false
                    text = "${10}s"
                },onTick = { secondUntilFinished->
                    isEnabled = false
                    text = "${secondUntilFinished}s"
                },onFinish={
                    isEnabled = codeBtnEnable
                    text = "发送验证码"
                })
        }
        binding.tvAction.doOnClick {
            viewModel.login(mobile,code)
        }
    }


}