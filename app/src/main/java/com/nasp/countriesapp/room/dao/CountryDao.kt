package com.nasp.countriesapp.room.dao

import androidx.room.*
import com.nasp.countriesapp.room.model.wrappers.CountryAndCurrency
import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.room.model.wrappers.CountryAndAll
import com.nasp.countriesapp.room.model.wrappers.CountryAndLanguage
import com.nasp.countriesapp.room.model.wrappers.CountryAndTranslation

@Dao
// Suppress this waning, RewriteQueriesToDropUnusedColumns does the trick.
@Suppress(RoomWarnings.CURSOR_MISMATCH)
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: CountriesEntity): Long

    @Update
    suspend fun updateCountry(country: CountriesEntity): Int

    @Transaction
    suspend fun insertOrUpdateCountry(country: CountriesEntity): Long {
        val currentCountry = getCountry(country.name)

        return when {
            currentCountry == null -> insertCountry(country)
            currentCountry != country -> updateCountry(country).toLong()
            else -> -2L

        }
    }

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM countries_table INNER JOIN country_currency_table ON countries_table.country_id = country_currency_table.country_id")
    suspend fun getCompleteCountries(): List<CountryAndAll>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM countries_table INNER JOIN country_currency_table ON countries_table.country_id = country_currency_table.country_id")
    suspend fun getCountriesWithCurrency(): List<CountryAndCurrency>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM countries_table INNER JOIN country_language_table ON countries_table.country_id = country_language_table.country_id")
    suspend fun getCountriesWithLanguages(): List<CountryAndLanguage>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM countries_table INNER JOIN country_translation_table ON countries_table.country_id = country_translation_table.country_id")
    suspend fun getCountriesWithTranslation(): List<CountryAndTranslation>

    @Query("SELECT * FROM countries_table")
    suspend fun getCountries(): List<CountriesEntity>

    @Query("SELECT * FROM countries_table WHERE countries_table.name == :name")
    suspend fun getCountry(name: String): CountriesEntity?

    @Delete
    suspend fun removeCountry(country: CountriesEntity): Int
}