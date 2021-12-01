package com.eju.appbase.entity

import java.io.Serializable

data class User(
    val id:String,
    val name:String):Serializable{
    var height = 88
}