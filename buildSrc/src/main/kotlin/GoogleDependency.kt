/**
 * 查询androidx下的最新的版本 https://androidx.tech/artifacts/core/core-ktx/
 */
object GoogleDependency {

    const val coreKtx = "androidx.core:core-ktx:1.7.0"
    const val appcompat = "androidx.appcompat:appcompat:1.4.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.2"
    const val material = "com.google.android.material:material:1.3.0"

    //kotlin-coroutine
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0"

    //activity对viewModel的拓展 by viewModels<T>()
    const val activityKtx = "androidx.activity:activity-ktx:1.4.0"
    //fragment对viewModel的拓展 by viewModels<T>()  by activityViewModels<T>()
    const val fragmentKtx =  "androidx.fragment:fragment-ktx:1.4.0"
    //jetpack-startup
    const val appStartup = "androidx.startup:startup-runtime:1.1.0"

    //ProcessLifecycleOwner provides a lifecycle for the whole application process
    const val lifecycleProcess = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
    //DefaultLifecycleObserver
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    //viewModel对协程的拓展 viewModelScope
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    //liveData对协程的拓展 liveData<T>{}
    const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    //lifecycle对协程的拓展 lifecycleScope
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

    //hilt
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompile = "com.google.dagger:hilt-compiler:${Versions.hilt}"

    //room
    //https://developer.android.google.cn/jetpack/androidx/releases/room#kts
    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompile = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx= "androidx.room:room-ktx:${Versions.room}"

    //activity result api
    const val resultActivity = "androidx.activity:activity:1.4.0"
    const val resultFragment = "androidx.fragment:fragment:1.4.0"

    //使用kotlin反射时添加(any::class.nestedClasses等)
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

    const val flexbox = "com.google.android:flexbox:2.0.0"

    const val palette = "androidx.palette:palette:1.0.0"
    const val paletteKtx = "androidx.palette:palette-ktx:1.0.0"
}