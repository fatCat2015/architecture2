package com.eju.room.demo
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*


@Entity()
data class Demo(
    var number:Long,
    @PrimaryKey
    var name:String,
    var age:Int
)