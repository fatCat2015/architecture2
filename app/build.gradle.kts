plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    //hilt
    id("dagger.hilt.android.plugin")
}

android {

    compileSdk =Versions.compileSdkVersion

    defaultConfig{
        applicationId = Versions.applicationId
        minSdk = Versions.minSdkVersion
        targetSdk = Versions.targetSdkVersion
        versionName = Versions.versionName
        versionCode = Versions.versionCode
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
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
        implementation(project(":start"))
        implementation(project(":demomodule"))
    }

}


dependencies{
    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}