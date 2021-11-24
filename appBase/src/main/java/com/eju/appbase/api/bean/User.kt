package com.eju.appbase.api.bean

import java.io.Serializable

data class User(
    val id:String,
    val name:String,
    val token:String,
    val mobile:String
):Serializable