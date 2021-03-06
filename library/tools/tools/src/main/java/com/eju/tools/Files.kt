package com.eju.tools

import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.*

internal val fileProviderAuthorities :String get() = "${packageName}.fileProvider"

fun File.toUri():Uri{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            application.applicationContext,
            fileProviderAuthorities,
            this
        )
    } else {
        Uri.fromFile(this)
    }
}

fun String.toFile():File = File(this)

inline fun File.print(crossinline block: PrintWriter.() -> Unit) =
    PrintWriter(BufferedWriter(FileWriter(this))).apply(block).close()


val externalCacheDirPath: String?
    get() = application.externalCacheDir?.absolutePath

val internalCacheDirPath: String
    get() = application.cacheDir.absolutePath

/**
 * Checks if a volume containing external storage is available for read and write.
 */
inline val isExternalStorageWritable: Boolean
    get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

inline val isExternalStorageRemovable: Boolean
    get() = Environment.isExternalStorageRemovable()

val cacheDirPath: String
    get() = if (isExternalStorageWritable || !isExternalStorageRemovable)
        externalCacheDirPath.orEmpty()
    else
        internalCacheDirPath


fun File.toByteArray():ByteArray{
    var buffer: ByteArray? = null
    try {
        val fis = FileInputStream(this)
        val bos = ByteArrayOutputStream()
        val b = ByteArray(1024)
        var n: Int
        while (fis.read(b).also { n = it } != -1) {
            bos.write(b, 0, n)
        }
        fis.close()
        bos.close()
        buffer = bos.toByteArray()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return buffer!!
}