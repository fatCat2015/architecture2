plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean" ,"saveCrashLogLocally",project.property("save_crash_log_locally").toString())

        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean" ,"saveCrashLogLocally",project.property("save_crash_log_locally").toString())

        }
    }
}

dependencies {
    //ProcessLifecycleOwner
    implementation(GoogleDependency.lifecycleProcess)
    //appStartUp
    implementation(GoogleDependency.appStartup)
    //mmkv
    implementation(ThirdDependency.mmkv)

    implementation(project(":retrofit"))

}