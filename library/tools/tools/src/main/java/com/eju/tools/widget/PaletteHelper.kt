package com.eju.tools.widget

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object PaletteHelper {

    suspend fun getColorFromBitmap(bitmap: Bitmap,defaultColor:Int):Int{
        return suspendCancellableCoroutine { continuation ->
            val task=Palette.from(bitmap).generate { palette->
                palette?.let {
                    continuation.resume(_getColor(palette,defaultColor))
                }?:continuation.resume(defaultColor)
            }
            continuation.invokeOnCancellation {
                task.cancel(true)
            }
        }
    }

    private fun _getColor(palette: Palette,defaultColor:Int):Int{
        var color = palette.getLightVibrantColor(defaultColor)
        var index = 0
        while (color == defaultColor) {
            when (index) {
                0 -> color = palette.getDarkVibrantColor(defaultColor)
                1 -> color = palette.getVibrantColor(defaultColor)
                2 -> color = palette.getLightMutedColor(defaultColor)
                3 -> color = palette.getDarkMutedColor(defaultColor)
                4 -> color = palette.getMutedColor(defaultColor)
            }
            index++
            if (index >= 5) {
                break
            }
        }
        return color
    }


}