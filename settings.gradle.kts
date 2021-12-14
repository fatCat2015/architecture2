//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//        jcenter()
//        maven("https://jitpack.io")
//    }
//}

rootProject.name = "architecture2"

//architecture
include (":architecture")
project (":architecture").apply {
    projectDir = File(rootProject.projectDir, "/library/architectures/architecture")
}

//tools module
include(":aop")
project (":aop").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/aop")
}

include(":cache")
project (":cache").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/cache")
}

include(":imageLoader")
project (":imageLoader").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/imageLoader")
}

include(":permissions")
project (":permissions").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/permissions")
}

include (":retrofit")
project (":retrofit").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/retrofit")
}

include (":room")
project (":room").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/room")
}

include(":startup")
project (":startup").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/startup")
}

include(":tools")
project (":tools").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/tools")
}

//sdk modules
include(":wechat")
project (":wechat").apply {
    projectDir = File(rootProject.projectDir, "/library/sdk/wechat")
}

include(":analysis")
project (":analysis").apply {
    projectDir = File(rootProject.projectDir, "/library/sdk/analysis")
}


include(":appBase")
include (":app")
include(":moduleStart")
include(":moduleDemo")
include(":moduleMain")

include(":permissions")
