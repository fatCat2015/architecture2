plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    //hilt
    id("dagger.hilt.android.plugin")
}

android {

    compileSdk =CompileConst.compileSdkVersion

    defaultConfig{
        applicationId = project.property("application_id").toString()
        minSdk = CompileConst.minSdkVersion
        targetSdk = CompileConst.targetSdkVersion
        versionCode = project.property("version_code").toString().toInt()
        versionName = project.property("version_name").toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

val isModule:Boolean get() {
    val isModule:String by System.getProperties()
    return isModule.toBoolean()
}

dependencies {

    implementation(GoogleDependency.coreKtx)
    implementation(ThirdDependency.timber)

    //hilt
    implementation(GoogleDependency.hilt)
    kapt(GoogleDependency.hiltCompile)

    implementation(project(":appBase"))
    if(!isModule){
        implementation(project(":moduleStart"))
        implementation(project(":moduleMain"))
    }

}


dependencies{
    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}