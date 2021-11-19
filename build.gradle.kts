// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")

        //hilt
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")

        //aop配置 https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx
        classpath ("com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
}


tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}