plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id ("android-aspectjx")  //module需要使用aop时添加
}

android {

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

//module需要使用aop时添加
aspectjx {
    include("com.eju")
    enabled = true    //aop开关
}

dependencies {
    //module需要使用aop时添加
    implementation(project(":aop"))
}