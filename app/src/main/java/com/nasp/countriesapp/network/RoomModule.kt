package com.nasp.countriesapp.network

import android.content.Context
import androidx.room.Room
import com.nasp.countriesapp.room.CountriesDatabase
import com.nasp.countriesapp.room.dao.CountryDao
import com.nasp.countriesapp.room.dao.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CountriesDatabase {
        return Room.databaseBuilder(
            appContext,
            CountriesDatabase::class.java,
            CountriesDatabase.DATABASE_NAME
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideCountriesDao(
        database: CountriesDatabase
    ): CountryDao =
        database.countriesDao()

    @Singleton
    @Provides
    fun provideCurrencyDao(
        database: CountriesDatabase
    ): CurrencyDao =
        database.currencyDao()
}