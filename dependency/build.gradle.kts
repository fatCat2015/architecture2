buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        //版本同项目根目录
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    }
}
plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly(gradleApi())
    //版本同项目根目录
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    //版本同项目根目录
    compileOnly("com.android.tools.build:gradle:7.0.2")
}

gradlePlugin {
    plugins {
        create("version") {
            //自定义plugin的id，其他module引用要用到
            id = "com.eju.dependencyPlugin"
            //指向自定义plugin类,dependency/src/main/kotlin下的定义的plugin的相对目录
            implementationClass = "DependencyPlugin"  //DependencyPlugin dependency/src/main/koltin下
        }
    }
}