package com.eju.retrofit.download

import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.NullPointerException
import javax.inject.Inject

/**
 * 无进度下载大文件
 */
class DownloadProxy @Inject constructor(private val downloadService: DownloadService) {

    suspend fun downLoad(url:String,outputDir:File,fileName:String) :File  {
        val responseBody = downloadService.download(url)
        //step1.创建输出文件
        val contentType = responseBody.contentType()?.toString()
        Timber.i("contentType:${contentType}")
        val extension = if(contentType.isNullOrEmpty()) null else MimeTypeMap.getSingleton().getExtensionFromMimeType(contentType)
        Timber.i("extension:${extension}")
        val outputFile = File(outputDir,if(extension.isNullOrEmpty()) fileName else "${fileName}.${extension}")
        if(outputFile.exists()){
            outputFile.delete()
        }
        Timber.i("outputFile:${outputFile}")

        //step2.输入流写入文件(此时的文件已经全部读取完成,在这里处理的进度并不是下载进度)
        val inputStream = responseBody.byteStream()
        val outputStream = FileOutputStream(outputFile)
        val bufferSize = 1024*8
        val buffer = ByteArray(bufferSize)
        val bufferedInputStream = BufferedInputStream(inputStream,bufferSize)
        var readLength = 0
        while (bufferedInputStream.read(buffer,0,bufferSize).also {
                readLength = it
            }!=-1){
            outputStream.write(buffer,0,readLength)
        }
        bufferedInputStream.close()
        outputStream.flush()
        outputStream.close()
        inputStream.close()

        //step3.success
        Timber.i("downLoad success-->outputFile:${outputFile}")
        return outputFile
    }

}