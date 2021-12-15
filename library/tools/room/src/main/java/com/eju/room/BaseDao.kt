package com.eju.room

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import timber.log.Timber
import java.lang.reflect.ParameterizedType

abstract class BaseDao<T> {

    /**
     * @return 插入后数据的index
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(obj: T): Long

    /**
     * @return 插入后数据的index
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg objs: T): List<Long>

    /**
     * @return 插入后数据的index
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(objs: Collection<T>): List<Long>

    /**
     * @param obj 根据主键匹配进行删除
     * @return 返回删除的数据数量 可以是0
     */
    @Delete
    abstract suspend fun delete(obj: T):Int

    /**
     * @param objs 根据主键匹配进行删除
     * @return 返回删除的数据数量 可以是0
     */
    @Delete
    abstract suspend fun delete(vararg objs: T):Int

    /**
     * @param objs 根据主键匹配进行删除
     * @return 返回删除的数据数量 可以是0
     */
    @Delete
    abstract suspend fun delete(objs: Collection<T>):Int

    /**
     * 删除表中所有数据
     * @return 返回一直是null todo
     */
    suspend fun deleteAll(): Int? {
        val query = SimpleSQLiteQuery(
            "delete from $tableName"
        )
        return doDeleteAll(query)
    }

    /**
     * @param obj 根据主键匹配进行更新
     * @return 返回更新成功的数据数量 可以是0
     */
    @Update
    abstract suspend fun update(obj: T): Int

    /**
     * @param objs 根据主键匹配进行更新
     * @return 返回更新成功的数据数量 可以是0
     */
    @Update
    abstract suspend fun update(vararg objs: T): Int

    /**
     * @param objs 根据主键匹配进行更新
     * @return 返回更新成功的数据数量 可以是0
     */
    @Update
    abstract suspend fun update( objs: Collection<T>): Int


    suspend fun findAll(): List<T> {
        val query = SimpleSQLiteQuery(
            "select * from $tableName"
        )
        return doFindAll(query)
    }


    /**
     * 获取表名
     */
    open val tableName: String
        get() {
            val clazz = (javaClass.superclass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<*>
            val tableName = clazz.simpleName
            Timber.d("getTableName: -->$tableName")
            return tableName
        }

    @RawQuery
    protected abstract suspend fun doDeleteAll(query: SupportSQLiteQuery): Int?

    @RawQuery
    protected abstract suspend fun doFindAll(query: SupportSQLiteQuery): List<T>

}