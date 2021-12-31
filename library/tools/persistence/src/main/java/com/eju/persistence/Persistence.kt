package com.eju.persistence

import android.os.Parcelable
import java.io.*

interface Persistence {

    companion object{
        internal const val DEFAULT_IDENTIFY = "9527#$#_@&*default_identify_id"
    }

    fun saveInt(key:String,value:Int,identify:String = DEFAULT_IDENTIFY )

    fun getInt(key:String,defaultValue:Int = 0,identify:String = DEFAULT_IDENTIFY ):Int

    fun saveLong(key:String,value:Long,identify:String = DEFAULT_IDENTIFY )

    fun getLong(key:String,defaultValue:Long = 0L,identify:String = DEFAULT_IDENTIFY ):Long

    fun saveFloat(key:String,value:Float,identify:String = DEFAULT_IDENTIFY )

    fun getFloat(key:String,defaultValue:Float = 0F,identify:String = DEFAULT_IDENTIFY ):Float

    fun saveDouble(key:String,value:Double,identify:String = DEFAULT_IDENTIFY )
    
    fun getDouble(key:String,defaultValue:Double = 0.0,identify:String = DEFAULT_IDENTIFY ):Double

    fun saveBoolean(key:String,value:Boolean,identify:String = DEFAULT_IDENTIFY )

    fun getBoolean(key:String,defaultValue:Boolean = false,identify:String = DEFAULT_IDENTIFY ):Boolean

    fun saveBytes(key:String,value:ByteArray,identify:String = DEFAULT_IDENTIFY )

    fun getBytes(key:String,defaultValue:ByteArray? =null ,identify:String = DEFAULT_IDENTIFY ):ByteArray?
   
    fun saveString(key:String,value:String,identify:String = DEFAULT_IDENTIFY )

    fun getString(key:String,defaultValue:String? =null ,identify:String = DEFAULT_IDENTIFY ):String?

    fun saveParcelable(key:String, value: Parcelable, identify:String = DEFAULT_IDENTIFY )

    fun <T: Parcelable> getParcelable(key:String, clazz: Class<T>, defaultValue:T? =null, identify:String = DEFAULT_IDENTIFY ):T?

    fun saveStringSet(key:String,value:Set<String>,identify:String = DEFAULT_IDENTIFY )

    fun getStringSet(key:String,defaultValue:Set<String>? =null ,identify:String = DEFAULT_IDENTIFY ):Set<String>?

    fun <T: Serializable> saveSerializable(key:String, value:T, identify:String = DEFAULT_IDENTIFY )
   
    fun <T: Serializable> getSerializable(key:String,clazz: Class<T>,defaultValue: T?=null, identify:String = DEFAULT_IDENTIFY ):T?

    fun remove(key:String,identify:String = DEFAULT_IDENTIFY )

    fun remove(vararg keys:String,identify:String = DEFAULT_IDENTIFY )

    fun clear(identify: String? = DEFAULT_IDENTIFY)


}