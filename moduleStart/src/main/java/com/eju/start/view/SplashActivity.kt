package com.eju.start.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.aRouter
import com.eju.architecture.core.baseViewModels
import com.eju.start.databinding.ActivitySplashBinding
import com.eju.start.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@Route(path = PagePath.Start.Splash)
@AndroidEntryPoint
class SplashActivity:AppBaseActivity<ActivitySplashBinding>() {

    private val viewModel by baseViewModels<SplashViewModel>()

    override fun showTitle(): Boolean {
        return false
    }

    override fun observe() {
        viewModel.showPrivacyPolicy.observe(this){
            showPrivacyPolicyDialog()
        }
        viewModel.toGuide.observe(this){
            aRouter.build(PagePath.Start.Guide).navigation(this)
        }
        viewModel.toLogin.observe(this){
            aRouter.build(PagePath.Start.Login).navigation(this)
        }
        viewModel.toMain.observe(this){
            aRouter.build(PagePath.Main.Home).navigation(this)
        }
    }

    private fun showPrivacyPolicyDialog(){
        AlertDialog.Builder(this)
            .setTitle("隐私政策说明")
            .setMessage("隐私政策说明隐私政策说明隐私政策说明隐私政策说明隐私政策说明隐私政策说明隐私政策说明隐私政策说明隐私政策说明隐私政策说明")
            .setNegativeButton("取消") { dialog, which ->
                viewModel.onDisagreePrivacyPolicy()
                dialog?.dismiss()
            }
            .setPositiveButton("已知晓并同意") { dialog, which ->
                viewModel.onAgreePrivacyPolicy()
                dialog?.dismiss()
            }
            .setOnDismissListener {
                viewModel.onPrivacyPolicyDialogDismiss()
            }
            .show()
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        viewModel.verifyPrivacyPolicy()
    }
}