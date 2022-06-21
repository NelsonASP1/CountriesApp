package com.nasp.countriesapp.repositories

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.nasp.countriesapp.model.Country
import com.nasp.countriesapp.model.countryinfo.Currency
import com.nasp.countriesapp.model.countryinfo.Translation
import com.nasp.countriesapp.network.services.CountriesService
import com.nasp.countriesapp.room.CountriesDatabase
import com.nasp.countriesapp.room.model.CountryCurrencyEntity
import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.room.model.CountryLanguageEntity
import com.nasp.countriesapp.room.model.CountryTranslationEntity
import com.nasp.countriesapp.room.utils.*


class CountryRepository(
    private val countriesService: CountriesService,
    private val roomDatabase: CountriesDatabase
) {

    suspend fun fetchCountries(): List<Country> = countriesService.getAll()

    suspend fun removeCountry(country: CountriesEntity): Int =
        roomDatabase.countriesDao().removeCountry(country).also {
            if (it != 1) {
                Log.w(
                    javaClass.simpleName,
                    "No affected rows in delete country: ${country.name}"
                )
            }
        }

    suspend fun saveCountry(country: Country): Long {
        kotlin.runCatching {
            val countryId = roomDatabase.countriesDao().insertOrUpdateCountry(country.toEntity())

            country.currencies
                ?.map { CurrencyConverters.toEntity(it.key to it.value) }
                ?.forEach {
                    val currencyId = roomDatabase.currencyDao().insertCurrency(it)

                    if (currencyId == -1L) {
                        return@forEach
                    }

                    kotlin.runCatching {
                        roomDatabase.countryCurrencyDao().insertCountryCurrency(
                            CountryCurrencyEntity(
                                0,
                                countryId,
                                currencyId
                            )
                        )
                    }.exceptionOrNull()?.let { err ->
                        Log.e(
                            javaClass.simpleName,
                            "Error on country: $countryId & currencyId: $currencyId; ${err.message}"
                        )
                        err.printStackTrace()
                    }
                }

            country.languages
                ?.map { LanguageConverters.toEntity(it.toPair()) }
                ?.forEach {
                    val languageId = roomDatabase.languagesDao().insertLanguage(it)

                    if (languageId == -1L) {
                        return@forEach
                    }

                    kotlin.runCatching {
                        roomDatabase.countryLanguageDao().insertCountryLanguage(
                            CountryLanguageEntity(
                                0,
                                countryId,
                                languageId
                            )
                        )
                    }.exceptionOrNull()?.let { err ->
                        Log.e(
                            javaClass.simpleName,
                            "Error on country: $countryId & languageId: $languageId; ${err.message}"
                        )
                        err.printStackTrace()
                    }
                }

            country.translations
                ?.map { TranslationConverters.toEntity(it.toPair()) }
                ?.forEach {
                    val translationId = roomDatabase.translationDao().insertTranslation(it)

                    if (translationId == -1L) {
                        return@forEach
                    }

                    kotlin.runCatching {
                        roomDatabase.countryTranslationDao().insertCountryTranslation(
                            CountryTranslationEntity(
                                0,
                                countryId,
                                translationId
                            )
                        )
                    }.exceptionOrNull()?.let { err ->
                        Log.e(
                            javaClass.simpleName,
                            "Error on country: $countryId & transactionId: $translationId; ${err.message}"
                        )
                        err.printStackTrace()
                    }
                }

            return countryId
        }.exceptionOrNull()?.let { err ->
            Log.e(
                javaClass.simpleName,
                "Error on country: ${country.name.common}; ${err.message}"
            )
            err.printStackTrace()

        }

        return -1
    }

    suspend fun getAll() = roomDatabase.countriesDao().getCountries()

    suspend fun getCompleteAll() = roomDatabase.countriesDao().getCompleteCountries()
        .map { countryAndAll ->
            countryAndAll.countriesEntity.toModel().apply {
                currencies = mutableMapOf<String, Currency>().apply {
                    countryAndAll.currenciesEntity.forEach {
                        put(
                            it.identifier, Currency(
                                it.name,
                                it.symbol
                            )
                        )
                    }
                }

                translations = mutableMapOf<String, Translation>().apply {
                    countryAndAll.translationEntity.forEach {
                        put(
                            it.identifier,
                            Translation(
                                it.common,
                                it.official
                            )
                        )
                    }
                }

                languages = mutableMapOf<String, String>().apply {
                    countryAndAll.languagesEntity.forEach {
                        put(
                            it.identifier,
                            it.name
                        )
                    }
                }
            }
        }.distinctBy { it.roomId }

    @VisibleForTesting
    suspend fun getAllWithCurrency() = roomDatabase
        .countriesDao()
        .getCountriesWithCurrency()
        .map { countryAndCurr ->
            countryAndCurr.countriesEntity.toModel().apply {
                currencies = mutableMapOf<String, Currency>().apply {
                    countryAndCurr.currenciesEntity.forEach {
                        put(
                            it.identifier, Currency(
                                it.name,
                                it.symbol
                            )
                        )
                    }
                }
            }
        }.distinctBy { it.roomId }

    @VisibleForTesting
    suspend fun getAllWithLanguages() = roomDatabase
        .countriesDao()
        .getCountriesWithLanguages()
        .map { countryAndLanguage ->
            countryAndLanguage.countriesEntity.toModel().apply {
                languages = mutableMapOf<String, String>().apply {
                    countryAndLanguage.languagesEntity.forEach {
                        put(
                            it.identifier,
                            it.name
                        )
                    }
                }
            }
        }.distinctBy { it.roomId }

    @VisibleForTesting
    suspend fun getAllWithTranslations() = roomDatabase
        .countriesDao()
        .getCountriesWithTranslation()
        .map { countryAndTranslations ->
            countryAndTranslations.countriesEntity.toModel().apply {
                translations = mutableMapOf<String, Translation>().apply {
                    countryAndTranslations.translationEntity.forEach {
                        put(
                            it.identifier,
                            Translation(
                                it.common,
                                it.official
                            )
                        )
                    }
                }
            }
        }.distinctBy { it.roomId }
}