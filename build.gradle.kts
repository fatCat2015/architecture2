// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()  //immersionbar smartRefreshLayout还在用
        maven("https://jitpack.io")
    }
    dependencies {
        classpath(PluginConst.gradle)
        classpath(PluginConst.kotlin)
        classpath(PluginConst.hilt)
        classpath(PluginConst.aopNew)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() //immersionbar smartRefreshLayout还在用
        maven("https://jitpack.io")
    }
}


tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}