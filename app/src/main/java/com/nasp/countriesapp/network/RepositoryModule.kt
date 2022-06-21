package com.nasp.countriesapp.network

import com.nasp.countriesapp.repositories.CountryRepository
import com.nasp.countriesapp.network.services.CountriesService
import com.nasp.countriesapp.room.CountriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCountriesRepository(
        countriesService: CountriesService,
        roomDatabase: CountriesDatabase
    ): CountryRepository =
        CountryRepository(countriesService, roomDatabase)

}