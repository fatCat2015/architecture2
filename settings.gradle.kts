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

include(":tools")
project (":tools").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/tools")
}

include(":aop")
project (":aop").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/aop")
}

include (":retrofit")
project (":retrofit").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/retrofit")
}

include(":cache")
project (":cache").apply {
    projectDir = File(rootProject.projectDir, "/library/tools/cache")
}


include(":appBase")
include (":app")
include(":start")
include(":demomodule")
