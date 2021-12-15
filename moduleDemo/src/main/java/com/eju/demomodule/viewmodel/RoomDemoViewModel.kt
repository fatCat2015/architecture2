package com.eju.demomodule.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import com.eju.architecture.core.BaseViewModel
import com.eju.room.appDatabase
import com.eju.room.demo.Demo
import timber.log.Timber
import java.util.*

class RoomDemoViewModel:BaseViewModel() {

    val demoList = MutableLiveData<List<Demo>>()

    private var index = 0

    fun queryAllDemos(){
        launch {
            val list=appDatabase.demoDao().findAll()
            Timber.i("list111:${list}")
            index = list.size
            demoList.postValue(list)
        }
    }

    fun insert(){
        launch {
            val demo = mockDemo()
            val demos = mockDemoList()
            val demo1 = mockDemo()
            val demo2 = mockDemo()
            val a0=appDatabase.demoDao().insert(demo)
            val a1=appDatabase.demoDao().insert(demos)
            val a2=appDatabase.demoDao().insert(demo1,demo2)
            Timber.i("insert:${a0}")
            Timber.i("insert:${a1}")
            Timber.i("insert:${a2.toList()}")
        }
    }

    private fun mockDemo() = Demo(number=0L,name = "sck${index}}", age = ++index)

    private fun mockDemoList() = List(3){
        mockDemo()
    }

    fun delete(){
        launch {
//            val demo0 = Demo(number=0L,name = "sck${0}}", age = ++index)
//            val demo1 = Demo(number=0L,name = "sck${1}}", age = ++index)
//            val demo2 = Demo(number=0L,name = "sck${2}}", age = ++index)
//            val demo3 = Demo(number=0L,name = "sck${3}}", age = ++index)
//            val demo4 = Demo(number=0L,name = "sck${4}}", age = ++index)
//            Timber.i("delete:${appDatabase.demoDao().delete(demo0)}")
//            Timber.i("delete:${appDatabase.demoDao().delete(emptyList())}")
//            Timber.i("delete:${appDatabase.demoDao().delete(listOf(demo1,demo2))}")
//            Timber.i("delete:${appDatabase.demoDao().delete()}")
//            Timber.i("delete:${appDatabase.demoDao().delete(demo3,demo4)}")
//            Timber.i("delete:${appDatabase.demoDao().deleteByParams("age","7")}")
//            Timber.i("delete:${appDatabase.demoDao().deleteByParams("number","20")}")
            Timber.i("delete:${appDatabase.demoDao().deleteAll()}")
        }
    }

    fun update(){
        launch {
            val demo0 = Demo(number=300,name = "sck${0}}", age = ++index)
            val demo1 = Demo(number=300,name = "sck${1}}", age = ++index)
            val demo2 = Demo(number=300,name = "sck${2}}", age = ++index)
            val demo3 = Demo(number=300,name = "sck${3}}", age = ++index)
            val demo4 = Demo(number=300,name = "sck${4}}", age = ++index)
            Timber.i("delete:${appDatabase.demoDao().update(demo1)}")
            Timber.i("delete:${appDatabase.demoDao().update(demo0,demo2)}")
            Timber.i("delete:${appDatabase.demoDao().update(listOf(demo3,demo4))}")
        }
    }

}