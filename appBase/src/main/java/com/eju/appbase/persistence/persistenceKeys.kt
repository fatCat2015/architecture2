package com.eju.appbase.persistence

//是否已经登录
const val IS_LOGGED = "is_logged"

//用于判断是否展示引导页
//本地保存一个int a1,gradle.properties保存一个int a2
//启动页面当a1 != a2 时才会展示引导页面,引导页面展示完成需要将本地值更新为a2
const val GUIDE_INDEX = "guide_index"

//登录用户信息
const val USER_ID = "user_id"
const val USER_TOKEN = "user_token"
const val LOGGED_MOBILE = "logged_mobile"

//用于判断是否是新版本第一次启动
//本地保存的versionCode和app的versionCode进行比较,然后更新本地的versionCode
const val VERSION_CODE = "version_code"