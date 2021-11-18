plugins {
    id("com.android.library")
    id("kotlin-android")
    id ("android-aspectjx")
}

android {

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

//在使用aop的module中添加 在aop module中添加不起作用
//exclude inclide二选一
//exclude写法
//aspectjx {
//    exclude ("com.umeng.commonsdk")
//    exclude ("com.umeng.analytics")
//    exclude ("com.tencent.bugly")
//    exclude ("com.alipay.sdk")
//    exclude ("com.tencent.mm.opensdk")
//    exclude ("cn.jpush")
//    exclude ("cn.jcore")
//    exclude ("versions.9")
//    enabled = true    //aop开关
//}
//
////include写法
//aspectjx {
//    include("com.eju")
//    enabled = true    //aop开关
//}


dependencies {
    implementation("org.aspectj:aspectjrt:1.9.6")
}