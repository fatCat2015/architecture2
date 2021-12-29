package com.eju.demomodule.demo

import timber.log.Timber
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Consumer:Subject by Delegate(){


    var str:String by StrDelegate()

    var name :String =""

    val age :String by StrDelegate()

    val height :Int by lazy {
        1
    }

    var phone:String by object:ObservableProperty<String>(""){
        override fun beforeChange(
            property: KProperty<*>,
            oldValue: String,
            newValue: String
        ): Boolean {
            return super.beforeChange(property, oldValue, newValue)
        }

        override fun afterChange(property: KProperty<*>, oldValue: String, newValue: String) {
            super.afterChange(property, oldValue, newValue)
        }
    }

    override fun buy() {

    }

}


class StrDelegate{

     operator fun getValue(thisRef: Any, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!" .also {
            Timber.i( it)
        }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: CharSequence) {
        Timber.i( "$value has been assigned to '${property.name}' in $thisRef.")

    }

}
