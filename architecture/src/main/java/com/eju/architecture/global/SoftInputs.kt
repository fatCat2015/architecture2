package com.eju.architecture.global

import android.content.Context
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 当前focusView是EditText并且触摸地不是EditText,返回true 表示此时应该关闭软键盘
 */
fun isShouldHideSoftInput(focusView: View?, event: MotionEvent): Boolean {
    if (focusView is EditText) {
        val positions = IntArray(2)
        focusView.getLocationInWindow(positions)
        val left = positions[0]
        val top = positions[1]
        val right = left + focusView.width
        val bottom = top + focusView.height
        return !(event.rawX.toInt() in left..right && event.rawY.toInt() in top..bottom)
    }
    return false
}

/**
 * 关闭软键盘
 */
fun hideSoftInput(token: IBinder?, context: Context?) {
    if (token != null&&context!=null) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

/**
 * 打开软键盘
 */
fun showSoftInput(context: Context?) {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
    imm?.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}
