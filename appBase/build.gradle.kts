plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    //hilt
    id("dagger.hilt.android.plugin")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.getName())
        //arg("AROUTER_GENERATE_DOC", "enable")  kotlin下不支持路由文档的生成
    }
}

android {

    compileSdk =CompileConst.compileSdkVersion

    defaultConfig{
        minSdk = CompileConst.minSdkVersion
        targetSdk = CompileConst.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(GoogleDependency.coreKtx)
    implementation(GoogleDependency.appcompat)
    implementation(GoogleDependency.material)
    implementation(GoogleDependency.constraintLayout)
    implementation(GoogleDependency.coroutine)
    implementation(GoogleDependency.activityKtx)
    implementation(GoogleDependency.fragmentKtx)
    implementation(GoogleDependency.lifecycleCommon)
    implementation(GoogleDependency.viewModelKtx)
    implementation(GoogleDependency.liveDataKtx)
    implementation(GoogleDependency.lifecycleKtx)
    implementation(GoogleDependency.resultActivity)
    implementation(GoogleDependency.resultFragment)
    implementation(GoogleDependency.kotlinReflect)
    debugImplementation(ThirdDependency.leakCanary)

    //hilt
    implementation(GoogleDependency.hilt)
    kapt(GoogleDependency.hiltCompile)

    implementation(ThirdDependency.immersionBar)
    implementation(ThirdDependency.immersionBarKtx)

    implementation(ThirdDependency.timber)
    implementation(ThirdDependency.gson)

    //agentWeb
    api(ThirdDependency.agentWeb)

    //ARouter
    implementation(ThirdDependency.aRouter)
    kapt(ThirdDependency.aRouterCompiler)

}

dependencies {
    api(project(":architecture"))
    api(project(":retrofit"))
    api(project(":tools"))
    implementation(project(":startup"))
}

dependencies {
    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}