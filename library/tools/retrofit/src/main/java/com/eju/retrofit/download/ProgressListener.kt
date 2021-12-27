package com.eju.retrofit.download

@Deprecated("unuse")
interface ProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}