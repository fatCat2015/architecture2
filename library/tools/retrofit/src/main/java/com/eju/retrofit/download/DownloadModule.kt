package com.eju.retrofit.download

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DownloadModule {

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): DownloadService {
        return retrofit.create(DownloadService::class.java)
    }

}