package com.eju.architecture.core

import android.widget.Toast
import androidx.annotation.StringRes
import com.eju.tools.SnackDuration
import com.eju.tools.ToastDuration
import com.google.android.material.snackbar.BaseTransientBottomBar


interface IViewBehavior {
    fun showLoading(msg:CharSequence? = null)
    fun showLoading(@StringRes resId: Int)
    fun hideLoading()
    fun showToast(msg:CharSequence?,@ToastDuration duration:Int = Toast.LENGTH_SHORT)
    fun showToast(@StringRes resId:Int?,@ToastDuration duration:Int = Toast.LENGTH_SHORT)
    fun showSnack(msg:CharSequence?, @SnackDuration duration:Int = BaseTransientBottomBar.LENGTH_SHORT, code:Int = 0)
    fun showSnack(@StringRes resId:Int?, @SnackDuration duration:Int = BaseTransientBottomBar.LENGTH_SHORT, code:Int = 0)
    fun finishPage()
}

interface IPagingBehavior{

    fun finishRefresh()

    fun finishLoadMore()

    fun setEnableLoadMore(enabled:Boolean)

    fun showEmptyView(showEmpty:Boolean)

    fun notifyDataSetChanged()

    fun notifyItemChanged(position:Int,itemCount:Int = 1,payload:Any? = null)

    fun notifyItemInserted(position:Int,itemCount:Int = 1)

    fun notifyItemRemoved(position:Int,itemCount:Int = 1)

    fun notifyItemMoved(fromPosition:Int,toPosition:Int )

}



internal data class ToastInfo(
    var msg:CharSequence?,
    var resId:Int?,
    @ToastDuration
    var duration:Int
)

internal data class SnackInfo(
    var msg:CharSequence?,
    var resId:Int?,
    @SnackDuration
    var duration:Int,
    var code :Int
)

internal data class ListNotifyInfo(
    var position:Int,
    var itemCount:Int,
    var payload: Any?
){
    var fromPosition:Int = 1
    var toPosition:Int = 1
}



