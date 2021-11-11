package com.eju.architecture

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.eju.architecture.base.BaseActivity
import com.eju.architecture.base.BaseViewModel
import com.eju.architecture.base.IPagingBehavior
import com.eju.architecture.base.lazy.BaseViewModelLazy
import com.eju.architecture.base.lazy.ViewBindingLazy
import com.eju.architecture.base.IViewBehavior
import timber.log.Timber
import java.lang.Exception
import java.lang.reflect.ParameterizedType


/**
 * 获取类泛型中是T类型的泛型Class
 */
inline fun <reified T> Class<*>.getActualParameterizedType():Class<T>?{
    return try {
        (genericSuperclass as? ParameterizedType)?.actualTypeArguments?.forEach {
            (it as? Class<Any>)?.let { clazz->
                if(T::class.java.isAssignableFrom(clazz)){
                    return clazz as? Class<T>
                }
            }
        }
        null
    }catch (e:Exception){
        null
    }
}

fun<B:ViewBinding> AppCompatActivity.viewBinding() = ViewBindingLazy<B>(javaClass) { layoutInflater }.apply {
    Timber.i("1111 ViewBindingLazy init")
}

fun<B:ViewBinding> Fragment.viewBinding() = ViewBindingLazy<B>(javaClass) { layoutInflater }

@MainThread
inline fun <reified VM : BaseViewModel> BaseActivity<*>.baseViewModels(): Lazy<VM> {
    return BaseViewModelLazy(VM::class,this as IViewBehavior,this as? IPagingBehavior,this as LifecycleOwner, { viewModelStore } ,{ defaultViewModelProviderFactory })
}



