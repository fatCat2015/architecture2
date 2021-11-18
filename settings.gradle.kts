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
includeBuild("dependency")
include(":tools")
include(":aop")
include (":architecture")
include (":retrofit")
include(":cache")
include(":appBase")
include (":app")



