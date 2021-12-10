package com.eju.demomodule.view

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import coil.transform.RoundedCornersTransformation
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.eju.appbase.base.AppBaseLazyLoadFragment
import com.eju.appbase.router.PagePath
import com.eju.appbase.R
import com.eju.demomodule.databinding.FragmentCoilBinding
import com.eju.tools.doOnClick
import com.eju.tools.initializer.HttpVideoFrameFetcher
import com.eju.tools.initializer.memoryCacheKey

@Route(path = PagePath.DemoModule.CoilFragment)
class CoilFragment:AppBaseLazyLoadFragment<FragmentCoilBinding>() {

    override fun observeLazy() {
    }

    override fun lazyLoad(savedInstanceState: Bundle?) {

//        binding.iv0.load("https://img0.baidu.com/it/u=1242053365,2901037121&fm=26&fmt=auto&gp=0.jpg")
        binding.iv0.load("https://img.jiandanhome.com/2112/09/008b49a858d211eca9d100163e0052f0.webp")

        binding.iv1.scaleType= ImageView.ScaleType.CENTER_CROP
        binding.iv1.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F07%2F04%2F17577a32ae4b66a.jpg%21r650&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630118969&t=66f83137bfeadb4565fc3a75edb0d0a6"){
            //测试关闭缓存
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-成功图
            crossfade(2000)
            //占位图
            placeholder(ColorDrawable(Color.RED))
        }

        binding.iv2.scaleType= ImageView.ScaleType.CENTER_CROP
        binding.iv2.load(""){
            //测试关闭缓存
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-错误图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(ColorDrawable(Color.RED))
            //错误图
            error(ColorDrawable(Color.BLUE))
        }

        binding.iv3.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20190530%2F11%2F1559186561-CrujemOyHp.jpg&refer=http%3A%2F%2Fimage.biaobaiju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630119214&t=8b0a1894d99e3efcbb10e7aae1bab347"){
            //测试关闭缓存
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-成功图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(R.mipmap.ic_launcher)
            //圆形展示
            transformations(CircleCropTransformation())

        }


        binding.iv4.load("https://pics4.baidu.com/feed/d000baa1cd11728bd4d98501effc13c8c3fd2c27.jpeg?token=73c8c9c6a7f8bee46641914a57098d98"){
            //测试关闭缓存
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-成功图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(R.mipmap.ic_launcher)
            //圆角展示
            transformations(RoundedCornersTransformation(20F,30F,40F,50F))

        }

        binding.iv5.load("https://pics4.baidu.com/feed/d000baa1cd11728bd4d98501effc13c8c3fd2c27.jpeg?token=73c8c9c6a7f8bee46641914a57098d98"){
            //测试关闭缓存
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-成功图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(R.mipmap.ic_launcher)
            //BlurTransformation展示 图片毛玻璃
            transformations(
                BlurTransformation(requireActivity(),radius = 15F),
                RoundedCornersTransformation(20F,30F,40F,50F)
            )

        }


        binding.iv6.load("https://pics4.baidu.com/feed/d000baa1cd11728bd4d98501effc13c8c3fd2c27.jpeg?token=73c8c9c6a7f8bee46641914a57098d98"){
            //测试关闭缓存
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-成功图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(R.mipmap.ic_launcher)
            //GrayscaleTransformation展示
            transformations(
                GrayscaleTransformation(),
                RoundedCornersTransformation(20F,30F,40F,50F)
            )
        }

        //gif加载
//        binding.iv7.scaleType=ImageView.ScaleType.CENTER_CROP
        binding.iv7.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201502%2F26%2F20150226125945_jZvyB.thumb.1000_0.gif&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630120525&t=356a1a954f667b1125f7a50471568d6b"){
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-效果图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(ColorDrawable(Color.RED))
        }

        //展示网络视频
        binding.iv8.load("https://img.jiandanhome.com/yft_sync/2107/10/1625910353835.mp4"){
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            //过渡展示 占位图-错误图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(ColorDrawable(Color.RED))
            videoFrameMillis(18000)
            fetcher(HttpVideoFrameFetcher(requireActivity()))
        }


        //自定义target
        activity?.imageLoader?.enqueue(
            ImageRequest.Builder(requireActivity())
            .data("https://pics4.baidu.com/feed/d000baa1cd11728bd4d98501effc13c8c3fd2c27.jpeg?token=73c8c9c6a7f8bee46641914a57098d98")
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.DISABLED)
            .placeholder(ColorDrawable(Color.GREEN))
            .size(300,300)
            .error(ColorDrawable(Color.RED))
            .target(
                onStart = {
                    Log.i("sck220", "onStart: ${it}")
                },
                onSuccess = {
                    Log.i("sck220", "onSuccess: ${it}")
                    if(it is BitmapDrawable){
                        Log.i("sck220", "onSuccess: ${it.bitmap.width} ${it.bitmap.height}")
                    }
                },
                onError = {
                    Log.i("sck220", "onError: ${it}")
                }
            )
            .build())

        //展示大图 从模糊到高清
        binding.iv9.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fclubimg.club.vmall.com%2Fdata%2Fattachment%2Fforum%2F202003%2F16%2F191956racqkqb94feaarvm.jpg&refer=http%3A%2F%2Fclubimg.club.vmall.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630139971&t=1cd7153a49c4a3300161b1fbaaafd37a"){
            //过渡展示 占位图-成功图
//            crossfade(true)
            crossfade(2000)
            //占位图
            placeholder(ColorDrawable(Color.RED))
        }
        binding.iv9.doOnClick {
            binding.iv9.memoryCacheKey?.let { memoryCacheKey->
                ARouter.getInstance().build(PagePath.DemoModule.ImageDetail)
                    .withParcelable("memoryCacheKey",memoryCacheKey)
                    .withTransition(0,0)
                    .navigation()
            }

        }
    }

    override fun setListenersLazy() {
    }
}