package com.eju.architecture.global

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import timber.log.Timber
import java.io.*




private const val DEFAULT_MMKV_ID = "9527#$#_@&*default_mmkv_id"

private val mmkvMap:MutableMap<String,MMKV> by lazy {
    mutableMapOf<String,MMKV>().also {
        it.put(DEFAULT_MMKV_ID, MMKV.defaultMMKV())
    }
}

private fun putIfAbsent(identify:String):MMKV{
    return mmkvMap[identify]?:MMKV.mmkvWithID(identify).also {
        mmkvMap[identify] = it
    }
}

fun saveInt(key:String,value:Int,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchInt(key:String,defaultValue:Int = 0,identify:String = DEFAULT_MMKV_ID):Int{
    return putIfAbsent(identify).decodeInt(key,defaultValue)
}

fun saveLong(key:String,value:Long,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchLong(key:String,defaultValue:Long = 0L,identify:String = DEFAULT_MMKV_ID):Long{
    return putIfAbsent(identify).decodeLong(key,defaultValue)
}

fun saveFloat(key:String,value:Float,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchFloat(key:String,defaultValue:Float = 0F,identify:String = DEFAULT_MMKV_ID):Float{
    return putIfAbsent(identify).decodeFloat(key,defaultValue)
}

fun saveDouble(key:String,value:Double,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchDouble(key:String,defaultValue:Double = 0.0,identify:String = DEFAULT_MMKV_ID):Double{
    return putIfAbsent(identify).decodeDouble(key,defaultValue)
}

fun saveBoolean(key:String,value:Boolean,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchBoolean(key:String,defaultValue:Boolean = false,identify:String = DEFAULT_MMKV_ID):Boolean{
    return putIfAbsent(identify).decodeBool(key,defaultValue)
}

fun saveBytes(key:String,value:ByteArray?,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchBytes(key:String,defaultValue:ByteArray? =null ,identify:String = DEFAULT_MMKV_ID):ByteArray?{
    return putIfAbsent(identify).decodeBytes(key,defaultValue)
}

fun saveString(key:String,value:String?,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchString(key:String,defaultValue:String? =null ,identify:String = DEFAULT_MMKV_ID):String?{
    return putIfAbsent(identify).decodeString(key,defaultValue)
}

fun saveParcelable(key:String,value:Parcelable?,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun <T:Parcelable> fetchParcelable(key:String,clazz: Class<T>,defaultValue:T? =null ,identify:String = DEFAULT_MMKV_ID):T?{
    return putIfAbsent(identify).decodeParcelable(key,clazz,defaultValue)
}

fun saveStringSet(key:String,value:Set<String>?,identify:String = DEFAULT_MMKV_ID){
    putIfAbsent(identify).encode(key,value)
}

fun fetchStringSet(key:String,defaultValue:Set<String>? =null ,identify:String = DEFAULT_MMKV_ID):Set<String>?{
    return putIfAbsent(identify).decodeStringSet(key,defaultValue)
}

//Serializable
private fun <T:Serializable> serializable2Bytes(item:T):ByteArray?{
    val start=System.currentTimeMillis()
    val byteArrayOutputStream = ByteArrayOutputStream()
    return ObjectOutputStream(byteArrayOutputStream).use {
        try {
            it.writeObject(item)
            it.flush()
            byteArrayOutputStream.toByteArray().also {
                Timber.i("序列化时间:${System.currentTimeMillis()-start}")
            }
        } catch (e: Exception) {
            null
        }
    }
}

private fun <T:Serializable> bytes2Serializable(byteArray: ByteArray):T?{
    val start=System.currentTimeMillis()
    val byteArrayInputStream = ByteArrayInputStream(byteArray)
    return ObjectInputStream(byteArrayInputStream).use {
        try {
            (it.readObject() as? T).also {
                Timber.i("反序列化时间:${System.currentTimeMillis()-start}")
            }
        } catch (e: Exception) {
            null
        }
    }
}

fun <T:Serializable> saveSerializable(key:String,value:T?,identify:String = DEFAULT_MMKV_ID){
    value?.let {
        saveBytes(key,serializable2Bytes(it),identify)
    }
}

fun <T:Serializable> fetchSerializable(key:String,defaultValue: T?=null,identify:String = DEFAULT_MMKV_ID):T?{
    return fetchBytes(key,identify=identify)?.let{
        bytes2Serializable(it)?:defaultValue
    }?:defaultValue
}



