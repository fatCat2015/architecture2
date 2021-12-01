package com.eju.tools

import android.net.Uri
import android.text.TextUtils
import java.net.URLEncoder
import java.util.*


val Uri.wrapped : Uri get() = Uri.parse(this.toString().replace("#", Uri.encode("#")))

val Uri.realPath:String? get() = this.wrapped.path

/**
 * 解析获取Uri中的query键值对
 */
val Uri.realQueryParameters:Map<String,String> get() = splitQueryParameters(this.wrapped)

private fun splitQueryParameters(rawUri: Uri): Map<String, String> {
    val query = rawUri.encodedQuery ?: return emptyMap()
    val paramMap: MutableMap<String?, String> = LinkedHashMap()
    var start = 0
    do {
        val next = query.indexOf('&', start)
        val end = if (next == -1) query.length else next
        var separator = query.indexOf('=', start)
        if (separator > end || separator == -1) {
            separator = end
        }
        val name = query.substring(start, separator)
        if (!TextUtils.isEmpty(name)) {
            val value = if (separator == end) "" else query.substring(separator + 1, end)
            paramMap[Uri.decode(name)] = Uri.decode(value)
        }

        // Move start to end of name.
        start = end + 1
    } while (start < query.length)
    return Collections.unmodifiableMap(paramMap)
}