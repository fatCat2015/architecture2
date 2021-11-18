plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {

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
}


dependencies {
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
    //appStartUp
    implementation(GoogleDependency.appStartup)
    //cache
    implementation(project(":cache"))
    //tools
    implementation(project(":tools"))
}