package com.nasp.countriesapp.repositories

import com.nasp.countriesapp.room.CountriesDatabase
import com.nasp.countriesapp.room.utils.LanguageConverters

class LanguageRepository(
    private val roomDatabase: CountriesDatabase
) {

    suspend fun getAll() = roomDatabase.languagesDao().getAll()

    suspend fun insert(languagePair: Pair<String, String>) = roomDatabase.languagesDao().insertLanguage(
        LanguageConverters.toEntity(languagePair)
    )
}