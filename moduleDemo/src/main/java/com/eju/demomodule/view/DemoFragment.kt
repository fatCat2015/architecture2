package com.eju.demomodule.view

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.view.postDelayed
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseLazyLoadFragment
import com.eju.appbase.dialog.LoadingDialog
import com.eju.appbase.entity.User
import com.eju.appbase.router.PagePath
import com.eju.appbase.router.aRouter
import com.eju.architecture.baseViewModels
import com.eju.demomodule.R
import com.eju.demomodule.databinding.CustomToastBinding
import com.eju.demomodule.databinding.FragmentDemoBinding
import com.eju.demomodule.demo.Consumer
import com.eju.demomodule.viewmodel.DemoViewModel
import com.eju.liveeventbus.observeEvent
import com.eju.liveeventbus.observeEventSticky
import com.eju.liveeventbus.postEvent
import com.eju.permissions.requestPermissions
import com.eju.tools.*
import com.eju.tools.widget.ShowOneDialogHandler
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

    private val showOneDialogHandler:ShowOneDialogHandler by lazy { ShowOneDialogHandler() }

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


        observeEvent<String>("aaa"){
            Timber.i("onChanged 111 :${it}")
        }

        observeEvent<String>("aaa"){
            Timber.i("onChanged 222 :${it}")
        }



        binding.btEventBus.doOnClick {
            binding.btEventBus.postDelayed(2000){
                val event = "${eventIndex++}"
                Timber.i("post:${event}")
                postEvent("aaa",event)
            }
        }

        binding.btObserve.doOnClick {
            observeEventSticky<String>("aaa"){
                Timber.i("onChanged 333 :${it}")
            }
        }

        binding.btDownload.doOnClick {
//            viewModel.download()
            Timber.tag("DemoFragment").i("11:${eventIndex}")
            Log.i("DemoFragment","22:${eventIndex}")
//            showToast("${eventIndex++}")


            val consumer = Consumer()
            consumer.str
            consumer.str = "11"
        }

        binding.btDialog.doOnClick {
            showOneDialogHandler.show(childFragmentManager,LoadingDialog.newInstance("222"))
            it.postDelayed(1000){
                showOneDialogHandler.show(childFragmentManager,LoadingDialog.newInstance("111"))
            }
            it.postDelayed(2000){
                showOneDialogHandler.show(childFragmentManager,LoadingDialog.newInstance("444"))
            }
            it.postDelayed(3333){
                showOneDialogHandler.show(childFragmentManager,LoadingDialog.newInstance("333"))
            }
        }

    }





    private var eventIndex = 0
}