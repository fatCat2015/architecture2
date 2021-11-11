package com.eju.architecture.global

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresPermission
import java.lang.Exception
import java.lang.NullPointerException

fun <V> Activity.intentExtra(key:String) : Lazy<V?> = lazy {
    intent.extras?.get(key) as? V
}

fun <V> Activity.intentExtra(key:String,default:V) : Lazy<V> = lazy {
    (intent.extras?.get(key) as? V)?:default
}

fun <V> Activity.safeIntentExtra(key:String) : Lazy<V> = lazy {
    checkNotNull(intent.extras?.get(key) as? V){
        "No intent value for key \"$key\""
    }
}

internal fun startSystemActivity(intent: Intent):Boolean {
    return try {
        startActivity(intent)
        true
    }catch (e:Exception){
        false
    }
}

fun dial(phoneNumber:String) = startSystemActivity(Intent(Intent.ACTION_DIAL,Uri.parse("tel:${Uri.encode(phoneNumber)}")))

@RequiresPermission(Manifest.permission.CALL_PHONE)
fun makeCall(phoneNumber:String) = startSystemActivity(Intent(Intent.ACTION_CALL,Uri.parse("tel:${Uri.encode(phoneNumber)}")))

fun sendSMS(phoneNumber: String,content:String) = startSystemActivity(
    Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:${Uri.encode(phoneNumber)}")).apply {
        putExtra("sms_body",content)
    })

fun browse(url:String) = startSystemActivity(Intent(Intent.ACTION_VIEW,Uri.parse(url)))

fun email(email: String, subject: String = "", text: String = "") = startSystemActivity(
    Intent(Intent.ACTION_SENDTO,Uri.parse("mailto")).apply {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        if (subject.isNotEmpty()) putExtra(Intent.EXTRA_SUBJECT, subject)
        if (text.isNotEmpty()) putExtra(Intent.EXTRA_TEXT, text)
    })

fun installApk(apkFileUri:Uri) = startSystemActivity(
    Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(apkFileUri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }
)

fun createPackageInstallSettingsIntent() = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,Uri.parse("package:${packageName}"))

