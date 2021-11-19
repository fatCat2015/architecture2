plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.getName())
    }
}

android {

    compileSdk =Versions.compileSdkVersion

    defaultConfig{
        minSdk = Versions.minSdkVersion
        targetSdk = Versions.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
    implementation(GoogleDependency.appStartup)

    implementation(ThirdDependency.immersionBar)
    implementation(ThirdDependency.immersionBarKtx)

    implementation(ThirdDependency.timber)

    implementation(ThirdDependency.gson)

    implementation(ThirdDependency.aRouter)
    kapt(ThirdDependency.aRouterCompiler)

}

dependencies {
    api(project(":architecture"))
    api(project(":tools"))
}

dependencies {
    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}