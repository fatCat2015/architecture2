plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {

    compileSdk =Versions.compileSdkVersion

    defaultConfig{
        minSdk = Versions.minSdkVersion
        targetSdk = Versions.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

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
    implementation(GoogleDependency.appcompat)
    implementation(GoogleDependency.material)
    implementation(GoogleDependency.coroutine)
    implementation(GoogleDependency.lifecycleCommon)
    implementation(GoogleDependency.appStartup)
    implementation(GoogleDependency.lifecycleProcess)
    implementation(ThirdDependency.mmkv)
    implementation(ThirdDependency.timber)
    implementation(ThirdDependency.permissionsX)

    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)

}
