package com.eju.architecture.core

import androidx.viewbinding.ViewBinding

interface ITitleView<B:ViewBinding> {

    val bindingCreator:()->B

    val binding:B

    fun onBindView(binding:B)

}
