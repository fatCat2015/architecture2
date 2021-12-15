package com.eju.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eju.room.demo.DemoDao
import com.eju.room.demo.Demo


@Database(entities = [Demo::class],version = 1,exportSchema = true)
abstract class AppDatabase:RoomDatabase() {

    abstract fun demoDao(): DemoDao

    companion object{
        @Volatile private var instance: AppDatabase?=null
        internal fun getInstance(): AppDatabase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(ContextInitializer.sContext).also { instance =it }
            }
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,"${ContextInitializer.sContext.packageName}.db")
                .build()
        }

    }
}