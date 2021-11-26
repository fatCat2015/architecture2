package com.eju.appbase.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding

open class AppBaseWebActivity<B:ViewBinding>:AppBaseActivity<B>() {
    override fun observe() {
        TODO("Not yet implemented")
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }
}