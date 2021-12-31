package com.eju.persistence

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson
import java.io.Serializable
import java.lang.IllegalArgumentException

class SPPersistence(private val context: Context,
                    private val jsonConverter: JsonConverter = DefaultJsonConverter()
):Persistence {

    private val spMap:MutableMap<String, SharedPreferences> by lazy {
        mutableMapOf<String, SharedPreferences>().also {
            it[Persistence.DEFAULT_IDENTIFY] = context.getSharedPreferences(Persistence.DEFAULT_IDENTIFY,Context.MODE_PRIVATE)
        }
    }

    private fun putIfAbsent(identify:String): SharedPreferences {
        return spMap[identify]?: context.getSharedPreferences(identify,Context.MODE_PRIVATE).also {
            spMap[identify] = it
        }
    }

    override fun saveInt(key: String, value: Int, identify: String) {
        putIfAbsent(identify).edit().putInt(key,value).apply()
    }

    override fun getInt(key: String, defaultValue: Int, identify: String): Int {
        return putIfAbsent(identify).getInt(key,defaultValue)
    }

    override fun saveLong(key: String, value: Long, identify: String) {
        putIfAbsent(identify).edit().putLong(key,value).apply()
    }

    override fun getLong(key: String, defaultValue: Long, identify: String): Long {
        return putIfAbsent(identify).getLong(key,defaultValue)
    }

    override fun saveFloat(key: String, value: Float, identify: String) {
        putIfAbsent(identify).edit().putFloat(key,value).apply()
    }

    override fun getFloat(key: String, defaultValue: Float, identify: String): Float {
        return putIfAbsent(identify).getFloat(key,defaultValue)
    }

    override fun saveDouble(key: String, value: Double, identify: String) {
        throw IllegalArgumentException("SharedPreferences does not support save or read Double")
    }

    override fun getDouble(key: String, defaultValue: Double, identify: String): Double {
        throw IllegalArgumentException("SharedPreferences does not support save or read Double")
    }

    override fun saveBoolean(key: String, value: Boolean, identify: String) {
        putIfAbsent(identify).edit().putBoolean(key,value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean, identify: String): Boolean {
        return putIfAbsent(identify).getBoolean(key,defaultValue)
    }

    override fun saveBytes(key: String, value: ByteArray, identify: String) {
        throw IllegalArgumentException("SharedPreferences does not support save or read ByteArray")
    }

    override fun getBytes(key: String, defaultValue: ByteArray?, identify: String): ByteArray? {
        throw IllegalArgumentException("SharedPreferences does not support save or read ByteArray")
    }

    override fun saveString(key: String, value: String, identify: String) {
        putIfAbsent(identify).edit().putString(key,value).apply()
    }

    override fun getString(key: String, defaultValue: String?, identify: String): String? {
        return putIfAbsent(identify).getString(key,defaultValue)
    }

    override fun saveParcelable(key: String, value: Parcelable, identify: String) {
        saveString(key,jsonConverter.obj2JsonStr(value),identify)
    }

    override fun <T : Parcelable> getParcelable(
        key: String,
        clazz: Class<T>,
        defaultValue: T?,
        identify: String
    ): T? {
        return getString(key,identify =identify)?.let {
            jsonConverter.jsonStr2Obj(clazz,it)
        }?:null
    }

    override fun saveStringSet(key: String, value: Set<String>, identify: String) {
        putIfAbsent(identify).edit().putStringSet(key,value).apply()
    }

    override fun getStringSet(
        key: String,
        defaultValue: Set<String>?,
        identify: String
    ): Set<String>? {
        return putIfAbsent(identify).getStringSet(key,defaultValue)
    }

    override fun <T : Serializable> saveSerializable(key: String, value: T, identify: String) {
        saveString(key,jsonConverter.obj2JsonStr(value),identify)
    }

    override fun <T : Serializable> getSerializable(
        key: String,
        clazz: Class<T>,
        defaultValue: T?,
        identify: String
    ): T? {
        return getString(key,identify =identify)?.let {
            jsonConverter.jsonStr2Obj(clazz,it)
        }?:null
    }

    override fun remove(key: String, identify: String) {
        putIfAbsent(identify).edit().remove(key).apply()
    }

    override fun remove(vararg keys: String, identify: String) {
        putIfAbsent(identify).edit().let { editor->
            keys.forEach {
                editor.remove(it)
            }
            editor.apply()
        }

    }

    override fun clear(identify: String?) {
        identify?.let {
            putIfAbsent(it).edit().clear().apply()
        }?: kotlin.run {
            spMap.values.forEach {
                it.edit().clear().apply()
            }
        }
    }
}

interface JsonConverter{

    fun obj2JsonStr(obj:Any):String

    fun <T> jsonStr2Obj(clazz: Class<T>,str:String):T
}

internal class DefaultJsonConverter:JsonConverter{

    private val gson = Gson()

    override fun obj2JsonStr(obj: Any): String {
        return gson.toJson(obj)
    }

    override fun <T> jsonStr2Obj(clazz: Class<T>,str: String): T {
        return gson.fromJson(str,clazz)
    }

}