// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath(PluginConst.gradle)
        classpath(PluginConst.kotlin)
        classpath(PluginConst.hilt)
        classpath(PluginConst.aop)
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