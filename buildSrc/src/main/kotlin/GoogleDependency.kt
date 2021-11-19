object GoogleDependency {

    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val material = "com.google.android.material:material:1.3.0"

    //kotlin-coroutine
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0"
    //activity对viewModel的拓展 by viewModels<T>()
    const val activityKtx = "androidx.activity:activity-ktx:1.2.3"
    //fragment对viewModel的拓展 by viewModels<T>()  by activityViewModels<T>()
    const val fragmentKtx =  "androidx.fragment:fragment-ktx:1.2.4"
    //jetpack-startup
    const val appStartup = "androidx.startup:startup-runtime:1.0.0"

    private const val lifecycle_version = "2.3.1"
    //ProcessLifecycleOwner provides a lifecycle for the whole application process
    const val lifecycleProcess = "androidx.lifecycle:lifecycle-process:$lifecycle_version"
    //DefaultLifecycleObserver
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
    //viewModel对协程的拓展 viewModelScope
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    //liveData对协程的拓展 liveData<T>{}
    const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    //lifecycle对协程的拓展 lifecycleScope
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"

    //hilt
    private const val hilt_version = "2.38.1"
    const val hilt = "com.google.dagger:hilt-android:$hilt_version"
    const val hiltCompile = "com.google.dagger:hilt-compiler:$hilt_version"

    //activity result api
    const val resultActivity = "androidx.activity:activity:1.3.1"
    const val resultFragment = "androidx.fragment:fragment:1.3.6"

    //paging
    private const val paging_version = "3.0.1"
    const val paging = "androidx.paging:paging-runtime:$paging_version"

    const val flexbox = "com.google.android:flexbox:2.0.0"
    const val palette = "androidx.palette:palette:1.0.0"
}