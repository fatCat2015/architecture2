package com.eju.tools.initializer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.drawable.toDrawable
import coil.Coil
import coil.bitmap.BitmapPool
import coil.decode.*
import coil.fetch.*
import coil.memory.MemoryCache
import coil.metadata
import coil.request.videoFrameMicros
import coil.request.videoFrameOption
import coil.size.OriginalSize
import coil.size.PixelSize
import coil.size.Size
import coil.util.DebugLogger
import com.eju.tools.BuildConfig
import timber.log.Timber
import kotlin.math.roundToInt

class CoilInitializer:SimpleInitializer<Unit>() {
    override fun create(context: Context) {
        Timber.i("Initializer init ${this}")
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
}

val ImageView.memoryCacheKey: MemoryCache.Key? get() =  this.metadata?.memoryCacheKey


/**
 * 展示网络视频时使用: imageView.load(""){ xxx fetcher(HttpVideoFrameFetcher(context)) }
 */
class HttpVideoFrameFetcher(context: Context): Fetcher<Uri> {

    private val delegate = VideoFrameDecoderDelegate(context)

    override suspend fun fetch(
        pool: BitmapPool,
        data: Uri,
        size: Size,
        options: Options
    ): FetchResult {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(data.toString(), hashMapOf())
            val (drawable, isSampled) = delegate.decode(pool, retriever, size, options)
            return DrawableResult(
                drawable = drawable,
                isSampled = isSampled,
                dataSource = DataSource.DISK
            )
        } finally {
            retriever.release()
        }
    }

    override fun key(data: Uri): String? {
        return data.toString()
    }

    /**
     * copy from VideoFrameDecoderDelegate in Coil
     */
    private class VideoFrameDecoderDelegate(private val context: Context) {

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        fun decode(
            pool: BitmapPool,
            retriever: MediaMetadataRetriever,
            size: Size,
            options: Options
        ): DecodeResult {
            val option = options.parameters.videoFrameOption() ?: MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            val frameMicros = options.parameters.videoFrameMicros() ?: 0L

            // Resolve the dimensions to decode the video frame at accounting
            // for the source's aspect ratio and the target's size.
            var srcWidth = 0
            var srcHeight = 0
            val destSize = when (size) {
                is PixelSize -> {
                    val rotation = if (Build.VERSION.SDK_INT >= 17) retriever.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION
                    )?.toIntOrNull() ?: 0 else 0
                    if (rotation == 90 || rotation == 270) {
                        srcWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
                        srcHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
                    } else {
                        srcWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
                        srcHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
                    }

                    if (srcWidth > 0 && srcHeight > 0) {
                        val rawScale = DecodeUtils.computeSizeMultiplier(
                            srcWidth = srcWidth,
                            srcHeight = srcHeight,
                            dstWidth = size.width,
                            dstHeight = size.height,
                            scale = options.scale
                        )
                        val scale = if (options.allowInexactSize) rawScale.coerceAtMost(1.0) else rawScale
                        val width = (scale * srcWidth).roundToInt()
                        val height = (scale * srcHeight).roundToInt()
                        PixelSize(width, height)
                    } else {
                        // We were unable to decode the video's dimensions.
                        // Fall back to decoding the video frame at the original size.
                        // We'll scale the resulting bitmap after decoding if necessary.
                        OriginalSize
                    }
                }
                is OriginalSize -> OriginalSize
            }

            val rawBitmap: Bitmap? = if (Build.VERSION.SDK_INT >= 27 && destSize is PixelSize) {
                retriever.getScaledFrameAtTime(frameMicros, option, destSize.width, destSize.height)
            } else {
                retriever.getFrameAtTime(frameMicros, option)?.also {
                    srcWidth = it.width
                    srcHeight = it.height
                }
            }

            // If you encounter this exception make sure your video is encoded in a supported codec.
            // https://developer.android.com/guide/topics/media/media-formats#video-formats
            checkNotNull(rawBitmap) { "Failed to decode frame at $frameMicros microseconds." }

            val bitmap = normalizeBitmap(pool, rawBitmap, destSize, options)

            val isSampled = if (srcWidth > 0 && srcHeight > 0) {
                DecodeUtils.computeSizeMultiplier(srcWidth, srcHeight, bitmap.width, bitmap.height, options.scale) < 1.0
            } else {
                // We were unable to determine the original size of the video. Assume it is sampled.
                true
            }

            return DecodeResult(
                drawable = bitmap.toDrawable(context.resources),
                isSampled = isSampled
            )
        }

        /** Return [inBitmap] or a copy of [inBitmap] that is valid for the input [options] and [size]. */
        private fun normalizeBitmap(
            pool: BitmapPool,
            inBitmap: Bitmap,
            size: Size,
            options: Options
        ): Bitmap {
            // Fast path: if the input bitmap is valid, return it.
            if (isConfigValid(inBitmap, options) && isSizeValid(inBitmap, options, size)) {
                return inBitmap
            }

            // Slow path: re-render the bitmap with the correct size + config.
            val scale: Float
            val dstWidth: Int
            val dstHeight: Int
            when (size) {
                is PixelSize -> {
                    scale = DecodeUtils.computeSizeMultiplier(
                        srcWidth = inBitmap.width,
                        srcHeight = inBitmap.height,
                        dstWidth = size.width,
                        dstHeight = size.height,
                        scale = options.scale
                    ).toFloat()
                    dstWidth = (scale * inBitmap.width).roundToInt()
                    dstHeight = (scale * inBitmap.height).roundToInt()
                }
                is OriginalSize -> {
                    scale = 1f
                    dstWidth = inBitmap.width
                    dstHeight = inBitmap.height
                }
            }
            val safeConfig = when {
                Build.VERSION.SDK_INT >= 26 && options.config == Bitmap.Config.HARDWARE -> Bitmap.Config.ARGB_8888
                else -> options.config
            }

            val outBitmap = pool.get(dstWidth, dstHeight, safeConfig)
            outBitmap.applyCanvas {
                scale(scale, scale)
                drawBitmap(inBitmap, 0f, 0f, paint)
            }
            pool.put(inBitmap)

            return outBitmap
        }

        private fun isConfigValid(bitmap: Bitmap, options: Options): Boolean {
            return Build.VERSION.SDK_INT < 26 || bitmap.config != Bitmap.Config.HARDWARE || options.config == Bitmap.Config.HARDWARE
        }

        private fun isSizeValid(bitmap: Bitmap, options: Options, size: Size): Boolean {
            return options.allowInexactSize || size is OriginalSize ||
                    size == DecodeUtils.computePixelSize(bitmap.width, bitmap.height, size, options.scale)
        }
    }

}