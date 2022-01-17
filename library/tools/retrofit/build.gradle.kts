plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    //hilt
    id("dagger.hilt.android.plugin")
}

android {

    compileSdk =CompileConst.compileSdkVersion

    defaultConfig{
        minSdk = CompileConst.minSdkVersion
        targetSdk = CompileConst.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug")  {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "baseUrl", "\"${project.property("base_url")}\"")
            buildConfigField("boolean" ,"showHttpLog",project.property("show_http_log").toString())
            buildConfigField("String", "httpLogTag", "\"${project.property("http_log_tag")}\"")
            buildConfigField("String", "requestLogTag", "\"${project.property("request_log_tag")}\"")
            buildConfigField("String", "responseLogTag", "\"${project.property("response_log_tag")}\"")
            buildConfigField("boolean" ,"showStethoInfo",project.property("show_stetho_info").toString())
        }

        getByName("release")  {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "baseUrl", "\"${project.property("base_url")}\"")
            buildConfigField("boolean" ,"showHttpLog",project.property("show_http_log").toString())
            buildConfigField("String", "httpLogTag", "\"${project.property("http_log_tag")}\"")
            buildConfigField("String", "requestLogTag", "\"${project.property("request_log_tag")}\"")
            buildConfigField("String", "responseLogTag", "\"${project.property("response_log_tag")}\"")
            buildConfigField("boolean" ,"showStethoInfo",project.property("show_stetho_info").toString())
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}


dependencies {
    implementation(GoogleDependency.coreKtx)
    implementation(GoogleDependency.coroutine)
    implementation(GoogleDependency.appStartup)
    //hilt
    implementation(GoogleDependency.hilt)
    kapt(GoogleDependency.hiltCompile)
    //retrofit
    api(ThirdDependency.retrofit)
    api(ThirdDependency.converter_gson)
    api(ThirdDependency.converter_scalars)
    //stetho
    implementation(ThirdDependency.stetho)
    implementation(ThirdDependency.stetho_okhttp)
    //httpLog
    implementation(ThirdDependency.LoggingInterceptor)
    //cache
    implementation(project(":cache"))
    //tools
    implementation(project(":tools"))

    implementation(ThirdDependency.timber)


    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}