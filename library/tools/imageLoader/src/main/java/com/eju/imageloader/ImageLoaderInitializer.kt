package com.eju.imageloader

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.startup.Initializer
import coil.Coil
import coil.decode.*
import coil.fetch.*
import coil.util.DebugLogger

class ImageLoaderInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        initCoil(context)
    }

    private fun initCoil(context: Context){
        Coil.setImageLoader{
            coil.ImageLoader.Builder(context)
                .availableMemoryPercentage(0.25) // Use 25% of the application's available memory.
                .crossfade(true) // Show a short crossfade when loading images from network or disk.
                .componentRegistry {
                    // GIFs
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder(context, enforceMinimumFrameDelay = true))
                    } else {
                        add(GifDecoder(enforceMinimumFrameDelay = true))
                    }

                    // SVGs
                    add(SvgDecoder(context))
                    // Video frames
                    add(VideoFrameFileFetcher(context))
                    add(VideoFrameUriFetcher(context))
                    add(VideoFrameDecoder(context))
                }
                .apply {
                    // Enable logging to the standard Android log if this is a debug build.
                    if (BuildConfig.DEBUG) {
                        logger(DebugLogger(Log.VERBOSE))
                    }
                }
                .build()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf()
    }
}




