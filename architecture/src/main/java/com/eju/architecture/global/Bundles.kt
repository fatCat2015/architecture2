package com.eju.architecture.global

import android.app.Activity
import androidx.fragment.app.Fragment


fun <V> Fragment.bundleExtra(key:String) : Lazy<V?> = lazy {
    arguments?.get(key) as? V
}

fun <V> Fragment.bundleExtra(key:String, default:V) : Lazy<V> = lazy {
    (arguments?.get(key) as? V)?:default
}

fun <V> Fragment.safeBundleExtra(key:String) : Lazy<V> = lazy {
    checkNotNull(arguments?.get(key) as? V){
        "No intent value for key \"$key\""
    }
}