// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins{
    id("com.eju.dependencyPlugin")
}

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")

        //hilt
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}



tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}