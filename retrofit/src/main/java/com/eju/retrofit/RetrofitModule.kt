package com.eju.retrofit

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private val requestHeaders= mutableMapOf<String,String>()

    private const val cacheSize:Long = 10*1024*1024

    private val cacheDir:File by lazy {
        File(RetrofitInitializer.application.cacheDir,"httpCache")
    }

    @Singleton
    @Provides
    fun provideOKHttpclient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .cache(Cache(cacheDir,cacheSize))
            .apply {
                if(BuildConfig.showStethoInfo){
                    addNetworkInterceptor(StethoInterceptor())
                }
            }
            .addInterceptor(AddHeadersInterceptor(requestHeaders))
            .addInterceptor(CacheInterceptor())
            .addInterceptor(HttpErrorInterceptor())
            .apply {
                if(BuildConfig.DEBUG&&BuildConfig.showHttpLog){
                    addInterceptor(
                        LoggingInterceptor.Builder()
                        .setLevel(Level.BASIC)
                        .tag(BuildConfig.requestLogTag)
                        .request(BuildConfig.requestLogTag)
                        .response(BuildConfig.responseLogTag)
                        .build()
                    )
                }
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    //公共请求头的添加
    fun addRequestHeader(key:String,value:String){
        requestHeaders[key] = value
    }
    fun addRequestHeader(headers:Map<String,String>){
        requestHeaders.putAll(headers)
    }
}