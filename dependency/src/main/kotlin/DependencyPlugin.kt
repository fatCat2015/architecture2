import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.internal.extensibility.DefaultConvention
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.model.Kapt

const val api = "api"
const val implementation = "implementation"
const val testImplementation = "testImplementation"
const val androidTestImplementation = "androidTestImplementation"
const val debug:Boolean = true

class DependencyPlugin :Plugin<Project> {

    override fun apply(target: Project) {
        configProject(target)
    }

    private fun configProject(project: Project){
        if(!project.displayName.contains("root project",true)){
            project.plugins.whenPluginAdded {
                when(this){
                    is AppPlugin ->{
                        applyAppCommons(project.extensions.getByType(),project)
                        configDependencies(project.dependencies,true)
                        configTestDependencies(project.dependencies)
                    }
                    is LibraryPlugin ->{
                        applyLibraryCommons(project.extensions.getByType(),project)
                        configDependencies(project.dependencies,false)
                        configTestDependencies(project.dependencies)
                    }
                }
            }
        }else{
            project.childProjects.forEach {
                configProject(it.value)
            }
        }
    }

    private fun applyAppCommons(extension: ApplicationExtension, project: Project){
        extension.apply {
            compileSdk = Versions.compileSdkVersion

            defaultConfig {
                minSdk = Versions.minSdkVersion
                targetSdk = Versions.targetSdkVersion

                applicationId = Versions.applicationId
                versionCode = Versions.versionCode
                versionName = Versions.versionName

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            buildFeatures{
                viewBinding = true
            }
        }
    }

    private fun applyLibraryCommons(extension: LibraryExtension, project: Project){
        extension.apply {
            compileSdk = Versions.compileSdkVersion

            defaultConfig {
                minSdk = Versions.minSdkVersion
                targetSdk = Versions.targetSdkVersion

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            buildFeatures{
                viewBinding = true
            }
        }
    }

    /**
     * app Module 公共依赖
     */
    private fun configDependencies(dependencyHandler: DependencyHandler,appModule:Boolean) {
        dependencyHandler.apply {
//            add(implementation, project.fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
            add(implementation,GoogleDependency.coreKtx)
            add(implementation,GoogleDependency.appcompat)
//            add(implementation,GoogleDependency.kotlinStdlib)
            add(implementation,GoogleDependency.material)
            add(implementation,GoogleDependency.constraintLayout)
            add(implementation,GoogleDependency.coroutine)

            add(implementation,GoogleDependency.activityKtx)
            add(implementation,GoogleDependency.fragmentKtx)
            add(implementation,GoogleDependency.lifecycleCommon)
            add(implementation,GoogleDependency.viewModelKtx)
            add(implementation,GoogleDependency.liveDataKtx)
            add(implementation,GoogleDependency.lifecycleKtx)

            add(implementation,GoogleDependency.resultActivity)
            add(implementation,GoogleDependency.resultFragment)

            add(implementation,ThirdDependency.timber)
            add(implementation,ThirdDependency.immersionBar)
            add(implementation,ThirdDependency.immersionBarKtx)
        }
    }
    /**
     * test 依赖配置
     */
    private fun configTestDependencies(dependencyHandler: DependencyHandler) {
        dependencyHandler.apply {
            add(testImplementation, Test.junit)
            add(androidTestImplementation, Test.extJunit)
            add(androidTestImplementation, Test.espresso)
        }
    }
}