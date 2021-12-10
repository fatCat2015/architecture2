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

include (":architecture")
project (":architecture").apply {
    projectDir = File(rootProject.projectDir, "/library/architectures/architecture")
}

include(":aop")
project (":aop").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/aop")
}

include(":cache")
project (":cache").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/cache")
}

include (":retrofit")
project (":retrofit").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/retrofit")
}

include(":startup")
project (":startup").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/startup")
}

include(":tools")
project (":tools").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/tools")
}

include(":wechat")
project (":wechat").apply {
    projectDir = File(rootProject.projectDir, "/library/sdk/wechat")
}


include(":appBase")
include (":app")
include(":moduleStart")
include(":moduleDemo")
include(":moduleMain")
