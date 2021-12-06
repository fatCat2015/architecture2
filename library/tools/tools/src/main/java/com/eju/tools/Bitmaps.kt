package com.eju.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sqrt

fun Bitmap.toByteArray(format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality:Int = 100):ByteArray{
    return ByteArrayOutputStream().use {
        compress(format, quality, it)
        it.flush()
        it.toByteArray()
    }
}

fun Bitmap.saveLocally(file: File,format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality:Int = 100):String{
    return BufferedOutputStream(FileOutputStream(file)).use {
        compress(format, quality, it)
        it.flush()
        file.absolutePath
    }
}

/**
 * 指定压缩后的质量最大值
 * 压缩前后大小差距过大时,压缩后的大小很可能大于maxSizeInKB
 * 压缩前后大小差距过大时,压缩后的图片质量会较差
 * 压缩前后大小差距过大时,推荐使用下面的compressImageFile方法,同时指定size和resolution
 */
suspend fun compressImageFile(imageFile:File, maxSizeInKB:Long):File{
    return compressImageFile(imageFile){
        size(maxSizeInKB*1024)
    }

}

suspend fun compressImageFile(imageFile:File, block: Compression.() -> Unit = { default() }):File{
    return Compressor.compress(application, imageFile){
        block()
    }.apply {
        Timber.i("compressImage complete # originalFile:${imageFile} compressedFile:${this} ")
    }

}




fun zoomImage(bitmap: Bitmap, newWidth: Double, newHeight: Double): Bitmap {
    // 获取这个图片的宽和高
    val width = bitmap.width.toFloat()
    val height = bitmap.height.toFloat()
    // 创建操作图片用的matrix对象
    val matrix = Matrix()
    // 计算宽高缩放率
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // 缩放图片动作
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(
        bitmap, 0, 0, width.toInt(),
        height.toInt(), matrix, true
    )
}

