package com.eju.architecture.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.eju.architecture.R
import com.eju.architecture.core.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

abstract class BaseBottomSheetDialog<B:ViewBinding>:BottomSheetDialogFragment() {

    protected val binding:B by viewBinding()

    private var onDismissCallback:OnDismissCallback?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onDismissCallback = context as? OnDismissCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        setBgTransparent()
        setDialogStyle()
        setData(savedInstanceState)
        setListeners()
    }


    private fun setBgTransparent(){
        var container=view?.parent as View?
        container?.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setDialogStyle(){
        val lp=dialog?.window?.attributes
        lp?.windowAnimations= R.style.bottomSheet_animation
        dialog?.window?.attributes=lp
    }

    protected abstract fun setData(savedInstanceState:Bundle?)

    protected abstract fun setListeners()


    open fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, UUID.randomUUID().toString())
    }

    open fun showAllowingStateLoss(fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction().add(this, UUID.randomUUID().toString()).commitAllowingStateLoss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.onDismiss(dialog)
    }

}