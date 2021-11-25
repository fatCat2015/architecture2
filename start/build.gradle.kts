plugins {
    val isModule:String by System.getProperties()
    if(isModule.toBoolean()){
        id("com.android.application")
    }else{
        id("com.android.library")
    }
    id("kotlin-android")
    id("kotlin-kapt")
    //hilt
    id("dagger.hilt.android.plugin")
}

//ARouter
kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.getName())
    }
}

val isModule:Boolean get() {
    val isModule:String by System.getProperties()
    return isModule.toBoolean()
}

val useIndependentApplicationId:Boolean get() {
    val useIndependentApplicationId:String by System.getProperties()
    return useIndependentApplicationId.toBoolean()
}


android {

    compileSdk =CompileConst.compileSdkVersion

    defaultConfig {
        if(isModule){
            if(!useIndependentApplicationId){
                if(this is com.android.build.api.dsl.ApplicationDefaultConfig){
                    applicationId = project.property("application_id").toString()
                }
            }
        }
        minSdk = CompileConst.minSdkVersion
        targetSdk = CompileConst.targetSdkVersion
        if(this is com.android.build.api.dsl.ApplicationDefaultConfig){
            versionCode = project.property("version_code").toString().toInt()
            versionName = project.property("version_name").toString()
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("main"){
            if(isModule){
                manifest.srcFile("src/main/module/AndroidManifest.xml")
                java.srcDir("src/main/module")
            }else{
                manifest.srcFile("src/main/AndroidManifest.xml")
            }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = File(project.rootDir,project.property("storeFilePath").toString())
            storePassword = project.property("storePassword").toString()
            keyAlias = project.property("keyAlias").toString()
            keyPassword = project.property("keyPassword").toString()
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("int" ,"guideIndex",project.property("guide_index").toString())
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("int" ,"guideIndex",project.property("guide_index").toString())
        }
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

    implementation(ThirdDependency.immersionBar)
    implementation(ThirdDependency.immersionBarKtx)

    implementation(ThirdDependency.timber)

    //hilt
    implementation(GoogleDependency.hilt)
    kapt(GoogleDependency.hiltCompile)

    //ARouter
    implementation(ThirdDependency.aRouter)
    kapt(ThirdDependency.aRouterCompiler)

    //smartRefreshLayout
    implementation(ThirdDependency.smartRefreshLayout)
    implementation(ThirdDependency.smartRefreshHeader0)

}


dependencies {
    implementation(project(":appBase"))
}

dependencies {
    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}