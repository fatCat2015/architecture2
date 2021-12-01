package com.eju.appbase.router

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.google.gson.Gson
import timber.log.Timber
import java.lang.reflect.Type

/**
 * 使用Arouter的withObject传递自定义对象时,会使用SerializationService对对象做json的转化,目标页面也会做json的解析
 */
@Route(path = "/xxx/json")
class JsonServiceImpl:SerializationService {

    private lateinit var gson: Gson

    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        Timber.i("JsonServiceImpl json2Object: input:${input}")
        return gson.fromJson(input,clazz)
    }

    override fun init(context: Context?) {
        gson= Gson()
        Timber.i("JsonServiceImpl init: ${context}")
    }

    override fun object2Json(instance: Any?): String {
        Timber.i("JsonServiceImpl object2Json: ${instance}")
        return gson.toJson(instance)
    }


    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        Timber.i("JsonServiceImpl parseObject: ${input}")
        return gson.fromJson(input,clazz)
    }
}