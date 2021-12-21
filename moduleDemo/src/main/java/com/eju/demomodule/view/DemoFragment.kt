package com.eju.demomodule.view

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseLazyLoadFragment
import com.eju.appbase.entity.User
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.aRouter
import com.eju.architecture.baseViewModels
import com.eju.demomodule.R
import com.eju.demomodule.databinding.FragmentDemoBinding
import com.eju.demomodule.viewmodel.DemoViewModel
import com.eju.permissions.requestPermissions
import com.eju.tools.*
import com.eju.wechat.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
@Route(path = PagePath.DemoModule.DemoFragment)
class DemoFragment:AppBaseLazyLoadFragment<FragmentDemoBinding>() {


    private val viewModel by baseViewModels<DemoViewModel>()

    override fun observeLazy() {
        viewModel.orderDetail.observe(this){
            Timber.i("order:${it}")
        }
    }

    override fun lazyLoad(savedInstanceState: Bundle?) {
    }

    override fun setListenersLazy() {
        binding.btWeb.doOnClick {
            aRouter.build(PagePath.DemoModule.WebPage)
                .withString("url","https://www.baidu.com")
                .navigation()
        }
        binding.btArouter.doOnClick {
            aRouter.build(PagePath.DemoModule.ARouterDemo)
                .withString("number","9527")
                .withSerializable("user",User("10","sck"))
                .withSerializable("users", arrayListOf(User("10","sck")))
                .navigation()
        }



        binding.btApi.doOnClick {
            viewModel.queryOrderDetail("89")
        }

        binding.btRequestPermissions.doOnClick {
            requestPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,deniedCallback = {
                         showToast("denied ${it}")
            },allGrantedCallback = {
                showToast("all granted")
            })

        }

        binding.btShare.doOnClick {
            viewModel.shareBitmap(BitmapFactory.decodeResource(
                resources,
                R.drawable.ic_big
            ))
        }

        binding.btCompressImage.doOnClick {
            val small =  BitmapFactory.decodeResource(resources,R.drawable.ic_demo,BitmapFactory.Options()).saveLocally(
                File(cacheDirPath,"small.jpg")
            )
            val big =  BitmapFactory.decodeResource(resources,R.drawable.ic_big,BitmapFactory.Options()).saveLocally(
                File(cacheDirPath,"big.jpg")
            )
            lifecycleScope.launch {
                compressImageFile(File(small),32)
                compressImageFile(File(big),1024)
            }
        }


        binding.btRoomDemo.doOnClick {
            aRouter.build(PagePath.DemoModule.RoomDemo).navigation()
        }

        binding.btContact.doOnClick {
            aRouter.build(PagePath.DemoModule.Contact).navigation()
        }

    }
}