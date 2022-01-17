plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {

    compileSdk =CompileConst.compileSdkVersion

    defaultConfig{
        minSdk = CompileConst.minSdkVersion
        targetSdk = CompileConst.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //room配置
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
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
    implementation(GoogleDependency.coroutine)
    implementation(GoogleDependency.room)
    implementation(GoogleDependency.roomKtx)
    implementation(GoogleDependency.appStartup)
    kapt(GoogleDependency.roomCompile)
    implementation(ThirdDependency.timber)


    testImplementation(Test.junit)
    androidTestImplementation(Test.extJunit)
    androidTestImplementation(Test.espresso)

}
