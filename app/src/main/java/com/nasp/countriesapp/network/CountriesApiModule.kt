package com.nasp.countriesapp.network

import com.nasp.countriesapp.network.services.CountriesService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.nasp.countriesapp.BuildConfig


@Module
@InstallIn(SingletonComponent::class)
object CountriesApiModule {

    @Singleton
    @Provides
    fun provideCountriesRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    fun provideCountriesService(retrofit: Retrofit): CountriesService =
        retrofit
            .create(CountriesService::class.java)
}