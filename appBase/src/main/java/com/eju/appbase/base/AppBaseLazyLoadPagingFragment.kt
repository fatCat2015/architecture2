package com.eju.appbase.base

import androidx.viewbinding.ViewBinding
import com.eju.architecture.core.*

abstract class AppBaseLazyLoadPagingFragment<V:ViewBinding>:AppBaseLazyLoadFragment<V>(), IPagingBehavior {

    override fun notifyItemChanged(position: Int, itemCount: Int, payload: Any?) {
    }

    override fun notifyItemInserted(position: Int, itemCount: Int) {
    }

    override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun notifyItemRemoved(position: Int, itemCount: Int) {
    }

}