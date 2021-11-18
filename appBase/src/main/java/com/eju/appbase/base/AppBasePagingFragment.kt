package com.eju.appbase.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.eju.appbase.R
import com.eju.architecture.core.*
import com.gyf.immersionbar.ktx.immersionBar
import timber.log.Timber

abstract class AppBasePagingFragment<V:ViewBinding>:AppBaseFragment<V>(), IPagingBehavior {

    override fun notifyItemChanged(position: Int, itemCount: Int, payload: Any?) {
    }

    override fun notifyItemInserted(position: Int, itemCount: Int) {
    }

    override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun notifyItemRemoved(position: Int, itemCount: Int) {
    }

}