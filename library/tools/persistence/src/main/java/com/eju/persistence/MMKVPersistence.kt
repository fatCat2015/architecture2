package com.eju.persistence

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import timber.log.Timber
import java.io.*

class MMKVPersistence:Persistence {

    private val mmkvMap:MutableMap<String, MMKV> by lazy {
        mutableMapOf<String, MMKV>().also {
            it[Persistence.DEFAULT_IDENTIFY] = MMKV.defaultMMKV()
        }
    }

    private fun putIfAbsent(mmkvId:String):MMKV{
        return mmkvMap[mmkvId]?:MMKV.mmkvWithID(mmkvId).also {
            mmkvMap[mmkvId] = it
        }
    }

    override fun saveInt(key: String, value: Int, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getInt(key: String, defaultValue: Int, identify: String): Int {
        return putIfAbsent(identify).decodeInt(key,defaultValue)
    }

    override fun saveLong(key: String, value: Long, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getLong(key: String, defaultValue: Long, identify: String) :Long {
        return putIfAbsent(identify).decodeLong(key,defaultValue)
    }

    override fun saveFloat(key: String, value: Float, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getFloat(key: String, defaultValue: Float, identify: String): Float {
        return putIfAbsent(identify).decodeFloat(key,defaultValue)
    }

    override fun saveDouble(key: String, value: Double, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getDouble(key: String, defaultValue: Double, identify: String): Double {
        return putIfAbsent(identify).decodeDouble(key,defaultValue)
    }

    override fun saveBoolean(key: String, value: Boolean, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean, identify: String): Boolean {
        return putIfAbsent(identify).decodeBool(key,defaultValue)
    }

    override fun saveBytes(key: String, value: ByteArray, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getBytes(key: String, defaultValue: ByteArray?, identify: String): ByteArray? {
        return putIfAbsent(identify).decodeBytes(key,defaultValue)
    }

    override fun saveString(key: String, value: String, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getString(key: String, defaultValue: String?, identify: String): String? {
        return putIfAbsent(identify).decodeString(key,defaultValue)
    }

    override fun saveParcelable(key: String, value: Parcelable, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun <T : Parcelable> getParcelable(
        key: String,
        clazz: Class<T>,
        defaultValue: T?,
        identify: String
    ): T? {
        return putIfAbsent(identify).decodeParcelable(key,clazz,defaultValue)
    }

    override fun saveStringSet(key: String, value: Set<String>, identify: String) {
        putIfAbsent(identify).encode(key,value)
    }

    override fun getStringSet(
        key: String,
        defaultValue: Set<String>?,
        identify: String
    ): Set<String>? {
        return putIfAbsent(identify).decodeStringSet(key,defaultValue)
    }

    override fun <T : Serializable> saveSerializable(key: String, value: T, identify: String) {
        serializable2Bytes(value)?.let {
            saveBytes(key, it,identify)
        }
    }

    override fun <T : Serializable> getSerializable(
        key: String,
        defaultValue: T?,
        identify: String
    ): T? {
        return getBytes(key,identify=identify)?.let{
            bytes2Serializable(it) ?:defaultValue
        }?:defaultValue
    }

    override fun remove(key: String, identify: String) {
        putIfAbsent(identify).removeValueForKey(key)
    }

    override fun remove(vararg keys: String, identify: String) {
        putIfAbsent(identify).removeValuesForKeys(keys)
    }

    override fun clear(identify: String?) {
        identify?.let {
            putIfAbsent(it).clearAll()
        }?: kotlin.run {
            mmkvMap.values.forEach {
                it.clearAll()
            }
        }

    }

    //Serializable
    private fun <T:Serializable> serializable2Bytes(item:T):ByteArray?{
        val byteArrayOutputStream = ByteArrayOutputStream()
        return ObjectOutputStream(byteArrayOutputStream).use {
            try {
                it.writeObject(item)
                it.flush()
                byteArrayOutputStream.toByteArray()
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun <T:Serializable> bytes2Serializable(byteArray: ByteArray):T?{
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        return ObjectInputStream(byteArrayInputStream).use {
            try {
                it.readObject() as? T
            } catch (e: Exception) {
                null
            }
        }
    }
}