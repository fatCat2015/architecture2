package com.eju.demomodule.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import coil.load
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.alibaba.android.arouter.facade.annotation.Route
import com.eju.appbase.base.AppBaseActivity
import com.eju.appbase.router.RouterPath
import com.eju.demomodule.databinding.ActivityBigImageBinding
import com.eju.tools.intentExtra
import timber.log.Timber

@Route(path = RouterPath.DemoModule.ImageDetail)
class BigImageActivity : AppBaseActivity<ActivityBigImageBinding>() {


    private val memoryCacheKey:MemoryCache.Key? by intentExtra("memoryCacheKey")

    override fun observe() {
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        binding.iv.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fclubimg.club.vmall.com%2Fdata%2Fattachment%2Fforum%2F202003%2F16%2F191956racqkqb94feaarvm.jpg&refer=http%3A%2F%2Fclubimg.club.vmall.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630139971&t=1cd7153a49c4a3300161b1fbaaafd37a"){
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            crossfade(2000)
            memoryCacheKey?.let {
                Timber.i("memoryCacheKey:${memoryCacheKey}")
                placeholderMemoryCacheKey(it)
            }?:placeholder(ColorDrawable(Color.RED))

        }
    }

    override fun setListeners() {
    }
}