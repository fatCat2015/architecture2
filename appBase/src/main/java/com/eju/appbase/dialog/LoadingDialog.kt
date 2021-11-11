package com.eju.appbase.dialog

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.eju.appbase.R
import com.eju.appbase.databinding.DialogLoadingBinding
import com.eju.architecture.dialog.BaseDialogFragment
import com.eju.architecture.global.dp

class LoadingDialog:BaseDialogFragment<DialogLoadingBinding>() {

    companion object{
        fun newInstance(loadingText:String?):LoadingDialog{
            return LoadingDialog().apply {
                arguments=Bundle().apply {
                    putString("loadingText",loadingText)
                }
            }
        }
    }


    override fun setData(savedInstanceState: Bundle?) {
        var content=arguments?.getString("loadingText")
        binding.clLoading.background= GradientDrawable().apply {
            activity?.let { activity->
                setColor(ContextCompat.getColor(activity, R.color.colorTranslucent7))
                cornerRadius=8.dp
            }
        }
        content=if(content.isNullOrEmpty()) activity?.getString(R.string.loading) else content
        binding.textView.isGone= content.isNullOrEmpty()
        binding.textView.text=content
    }

    override fun setListeners() {

    }

    override fun dimEnabled(): Boolean {
        return false
    }
}