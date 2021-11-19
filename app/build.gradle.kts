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

    //smartRefreshLayout
    implementation(ThirdDependency.smartRefreshLayout)
    implementation(ThirdDependency.smartRefreshHeader0)

}

dependencies{
    implementation(project(":appBase"))
    implementation(project(":retrofit"))
}

dependencies{
    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}