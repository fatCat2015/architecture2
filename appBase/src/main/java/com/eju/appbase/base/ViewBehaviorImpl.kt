package com.eju.appbase.base

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.eju.appbase.dialog.LoadingDialog
import com.eju.architecture.core.IViewBehavior
import com.eju.tools.showToast
import com.google.android.material.snackbar.Snackbar


class ViewBehaviorImpl(
    private val activity: FragmentActivity?,
    private val fragment:Fragment?,
    private val isFragment: Boolean
): IViewBehavior {


    constructor(activity: FragmentActivity):this(activity,null,false)

    constructor(fragment: Fragment):this(null,fragment,true)

    private val context: Context? by lazy {
        if(isFragment) fragment?.activity else activity
    }
    private val activityContentView:View? by lazy {
        (if(isFragment) fragment?.activity else activity)?.findViewById(android.R.id.content)
    }

    private val fragmentManager: FragmentManager? by lazy {
        if(isFragment) fragment?.childFragmentManager else activity?.supportFragmentManager
    }

    private val lifecycleOwner:LifecycleOwner? by lazy {
        if(isFragment) fragment else activity
    }

    private var loadingDialog:LoadingDialog? =null

    override fun showLoading(msg: CharSequence?) {
        fragmentManager?.let {
            hideLoading()
            LoadingDialog.newInstance(msg?.toString()).also { loadingDialog = it }
                .showAllowingStateLoss(it)
        }

    }

    override fun showLoading(resId: Int) {
        showLoading( context?.getString(resId) )
    }

    override fun hideLoading() {
        loadingDialog?.dismissAllowingStateLoss()
    }

    override fun showToast(msg: CharSequence?,duration: Int) {
        lifecycleOwner?.let {
            showToast(context,it,msg,duration)
        }
    }

    override fun showToast(resId: Int?, duration: Int) {
        resId?.let {
            showToast(context?.getString(it),duration)
        }
    }

    override fun showSnack(msg: CharSequence?, duration:Int,code:Int) {
        msg?.let { msg->
            activityContentView?.let { view->
                Snackbar.make(view,msg,duration).show()
            }
        }
    }

    override fun showSnack(resId: Int?, duration: Int, code: Int) {
        resId?.let { resId->
            activityContentView?.let { view->
                Snackbar.make(view,resId,duration).show()
            }

        }
    }

    override fun finishPage() {
        if(isFragment){
            fragment?.activity?.finish()
        }else{
            activity?.finish()
        }
    }

}