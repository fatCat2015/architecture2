package com.eju.room.demo

import androidx.room.*

@Dao
interface DemoDao{

    @Insert
    fun insert(demo:Demo)

    @Insert
    fun insert(vararg demos:Demo)

    @Insert
    fun insert(demos:Collection<Demo>)

    @Query("select * from demo")
    suspend fun findAllDemos():List<Demo>?

}