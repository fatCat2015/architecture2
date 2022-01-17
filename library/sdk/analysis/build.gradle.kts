plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

val isModule:Boolean get() {
    val isModule:String by System.getProperties()
    return isModule.toBoolean()
}


android {

    compileSdk =CompileConst.compileSdkVersion

    defaultConfig {
        minSdk = CompileConst.minSdkVersion
        targetSdk = CompileConst.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean" ,"isModule",isModule.toString())
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean" ,"isModule",isModule.toString())
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
    implementation(GoogleDependency.appStartup)

    implementation(ThirdDependency.timber)


}


dependencies {
    implementation(project(":tools"))
}

dependencies {
    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)
}