package com.eju.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.imageLoader
import coil.memory.MemoryCache
import coil.metadata
import coil.request.ImageRequest

val ImageView.memoryCacheKey: MemoryCache.Key? get() =  this.metadata?.memoryCacheKey

val Fragment.imageLoader:ImageLoader? get() = this.activity?.imageLoader

suspend fun Context.loadBitmapFromUrl(url:String,width:Int = 0 ,height:Int = 0):Bitmap? {
    return imageLoader.execute(ImageRequest.Builder(this)
        .data(url)
        .apply {
            if(width>0&&height>0){
                size(width,height)
            }
        }
        .build()).drawable?.let {
        (it as? BitmapDrawable)?.bitmap
    }
}
