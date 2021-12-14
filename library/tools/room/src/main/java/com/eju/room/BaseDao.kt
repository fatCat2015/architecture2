//package com.eju.room
//
//import androidx.room.*
//import androidx.sqlite.db.SimpleSQLiteQuery
//import androidx.sqlite.db.SupportSQLiteQuery
//import timber.log.Timber
//import java.lang.reflect.ParameterizedType
//
//abstract class BaseDao<T> {
//    /**
//     * 添加单个对象
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract suspend fun insert(obj: T): Long
//
//    /**
//     * 添加数组对象数据
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract suspend fun insert(vararg objs: T): LongArray?
//
//    /**
//     * 添加对象集合
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract suspend fun insert(personList: List<T>): List<Long>
//
//    /**
//     * 根据对象中的主键删除（主键是自动增长的，无需手动赋值）
//     */
//    @Delete
//    abstract suspend fun delete(obj: T):Long
//
//    @Delete
//    abstract suspend fun delete(vararg objs: T):LongArray?
//
//    @Delete
//    abstract suspend fun delete(objs: List<T>):List<Long>
//
//    /**
//     * 删除表中所有数据
//     */
//    suspend fun deleteAll(): Int {
//        val query = SimpleSQLiteQuery(
//            "delete from $tableName"
//        )
//        return doDeleteAll(query)
//    }
//
//    /**
//     * @param column 列名
//     * @param value  列的值
//     */
//    suspend fun deleteByParams(column: String, value: String): Int {
//        val query = SimpleSQLiteQuery("delete from $tableName where $column='${value}'")
//        Timber.d("deleteByParams: ${"delete from $tableName where $column='${value}'"}")
//        return doDeleteByParams(query)
//    }
//
//    /**
//     * 根据对象中的主键更新（主键是自动增长的，无需手动赋值）
//     */
//    @Update
//    abstract suspend fun update(vararg obj: T): Int
//
//
//    suspend fun findAll(): List<T>? {
//        val query = SimpleSQLiteQuery(
//            "select * from $tableName"
//        )
//        return doFindAll(query)
//    }
//
//    suspend fun find(id: Long): T? {
//        val query = SimpleSQLiteQuery(
//            "select * from $tableName where id = ?", arrayOf<Any>(id)
//        )
//        return doFind(query)
//    }
//
//
//    /**
//     * 分页查询，支持传入多个字段，但必须要按照顺序传入
//     * key = value，key = value 的形式，一一对应（可以使用 stringbuilder 去构造一下，这里就不演示了）
//     */
//    suspend fun doQueryByLimit(vararg string: String, limit: Int = 10, offset: Int = 0): List<T>? {
//        val query =
//            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE ${string[0]} = '${string[1]}' limit $limit offset $offset")
//        return doQueryByLimit(query)
//    }
//
//    /**
//     * 降序分页查询
//     */
//    suspend fun doQueryByOrder(vararg string: String, limit: Int = 10, offset: Int = 10): List<T>? {
//        val query =
//            SimpleSQLiteQuery("SELECT * FROM $tableName ORDER BY ${string[0]} desc limit '${limit}' offset '${offset}'")
//        return doQueryByOrder(query)
//    }
//
//    /**
//     * 获取表名
//     */
//    open val tableName: String
//        get() {
//            val clazz = (javaClass.superclass.genericSuperclass as ParameterizedType)
//                .actualTypeArguments[0] as Class<*>
//            val tableName = clazz.simpleName
//            Timber.d("getTableName: -->$tableName")
//            return tableName
//        }
//
//    @RawQuery
//    protected abstract suspend fun doDeleteAll(query: SupportSQLiteQuery?): Int
//
//    @RawQuery
//    protected abstract suspend fun doDeleteByParams(query: SupportSQLiteQuery?): Int
//
//    @RawQuery
//    protected abstract suspend fun doFindAll(query: SupportSQLiteQuery?): List<T>?
//
//    @RawQuery
//    protected abstract suspend fun doFind(query: SupportSQLiteQuery?): T
//
//    @RawQuery
//    protected abstract suspend fun doQueryByLimit(query: SupportSQLiteQuery?): List<T>?
//
//    @RawQuery
//    protected abstract suspend fun doQueryByOrder(query: SupportSQLiteQuery?): List<T>?
//}