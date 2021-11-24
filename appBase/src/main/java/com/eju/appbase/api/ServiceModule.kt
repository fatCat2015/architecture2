package com.eju.appbase.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit):LoginService{
        return retrofit.create(LoginService::class.java).also {   Timber.i("provideLoginService ${it}") }
    }

}