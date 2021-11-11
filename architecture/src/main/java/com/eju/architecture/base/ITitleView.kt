package com.eju.architecture.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface ITitleView<B:ViewBinding> {

    val binding:B

    fun onBindView(binding:B)

}
