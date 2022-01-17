package com.eju.architecture.core.lazy

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.eju.architecture.core.getActualParameterizedType
import timber.log.Timber
import java.lang.Exception
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewBindingLazy<B:ViewBinding>(private val  viewClass:Class<*>,private val layoutInflaterProducer:()->LayoutInflater):ReadWriteProperty<Any,B> {

    private var _viewBinding:B? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): B {
        return _viewBinding?:viewClass.getActualParameterizedType<ViewBinding>()?.let {
            try {
                val a = System.currentTimeMillis()
                (it.getDeclaredMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflaterProducer()) as B).apply {
                    _viewBinding = this
                    Timber.i("初始化ViewBing耗时:${(System.currentTimeMillis() - a)}")
                }
            } catch (e: Exception) {
                null
            }
        } ?: throw NullPointerException("init ViewBinding failed")
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: B) {
        this._viewBinding = value
    }

}