package com.eju.persistence

import android.os.Parcelable
import java.io.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//tools
//Serializable
fun <T:Serializable> T.toByteArray():ByteArray?{
    val byteArrayOutputStream = ByteArrayOutputStream()
    return ObjectOutputStream(byteArrayOutputStream).use {
        try {
            it.writeObject(this)
            it.flush()
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            null
        }
    }
}

fun <T:Serializable> ByteArray.toSerializableObj():T?{
    val byteArrayInputStream = ByteArrayInputStream(this)
    return ObjectInputStream(byteArrayInputStream).use {
        try {
            it.readObject() as? T
        } catch (e: Exception) {
            null
        }
    }
}

//extensions
val persistence :Persistence by lazy { MMKVPersistence() }

fun persistenceInt(key:String,default:Int = 0,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceIntProperty(key,default,identify)

fun persistenceLong(key:String,default:Long = 0,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceLongProperty(key,default,identify)

fun persistenceFloat(key:String,default:Float = 0F,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceFloatProperty(key,default,identify)

fun persistenceDouble(key:String,default:Double = 0.0,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceDoubleProperty(key,default,identify)

fun persistenceBoolean(key:String,default:Boolean = false,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceBooleanProperty(key,default,identify)

fun persistenceBytes(key:String,default:ByteArray? = null,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceBytesProperty(key,default,identify)

fun persistenceString(key:String,default:String? = null,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceStringProperty(key,default,identify)

fun <T:Parcelable> persistenceParcelable(key:String,clazz: Class<T>,default:T? = null,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceParcelableProperty(key,clazz,default,identify)

fun persistenceStringSet(key:String,default:Set<String>? = null,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceStringSetProperty(key,default,identify)

fun <T:Serializable> persistenceSerializable(key:String,clazz: Class<T>,default:T? = null,identify:String = Persistence.DEFAULT_IDENTIFY) = PersistenceSerializableProperty(key,clazz,default,identify)

class PersistenceIntProperty(private val key:String,
                             private val default:Int,
                             private val identify:String ):
    ReadWriteProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return persistence.getInt(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        persistence.saveInt(key,value,identify)
    }

}

class PersistenceLongProperty(private val key:String,
                              private val default:Long,
                              private val identify:String ):
    ReadWriteProperty<Any, Long> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return persistence.getLong(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        persistence.saveLong(key,value,identify)
    }

}

class PersistenceFloatProperty(private val key:String,
                               private val default:Float,
                               private val identify:String ):
    ReadWriteProperty<Any, Float> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return persistence.getFloat(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
        persistence.saveFloat(key,value,identify)
    }

}

class PersistenceDoubleProperty(private val key:String,
                                private val default:Double,
                                private val identify:String ):
    ReadWriteProperty<Any, Double> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Double {
        return persistence.getDouble(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Double) {
        persistence.saveDouble(key,value,identify)
    }

}

class PersistenceBooleanProperty(private val key:String,
                                 private val default:Boolean,
                                 private val identify:String ):
    ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return persistence.getBoolean(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        persistence.saveBoolean(key,value,identify)
    }

}

class PersistenceBytesProperty(private val key:String,
                               private val default:ByteArray?,
                               private val identify:String ):
    ReadWriteProperty<Any, ByteArray?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): ByteArray? {
        return persistence.getBytes(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: ByteArray?) {
        value?.let {
            persistence.saveBytes(key,it,identify)
        }
    }

}

class PersistenceStringProperty(private val key:String,
                                private val default:String?,
                                private val identify:String ):
    ReadWriteProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return persistence.getString(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        value?.let {
            persistence.saveString(key,it,identify)
        }
    }

}

class PersistenceParcelableProperty<T:Parcelable>(private val key:String,
                                                  private val clazz: Class<T>,
                                                  private val default:T?,
                                                  private val identify:String ):
    ReadWriteProperty<Any, T?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return persistence.getParcelable(key,clazz,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        value?.let {
            persistence.saveParcelable(key,it,identify)
        }
    }

}


class PersistenceStringSetProperty(private val key:String,
                                   private val default:Set<String>?,
                                   private val identify:String ):
    ReadWriteProperty<Any, Set<String>?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Set<String>? {
        return persistence.getStringSet(key,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>?) {
        value?.let {
            persistence.saveStringSet(key,it,identify)
        }
    }

}


class PersistenceSerializableProperty<T:Serializable>(private val key:String,
                                                      private val clazz: Class<T>,
                                                      private val default:T?,
                                                      private val identify:String ):
    ReadWriteProperty<Any, T?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return persistence.getSerializable(key,clazz,default,identify)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        value?.let {
            persistence.saveSerializable(key,it,identify)
        }
    }

}