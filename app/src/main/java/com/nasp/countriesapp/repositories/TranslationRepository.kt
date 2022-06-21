package com.nasp.countriesapp.repositories

import com.nasp.countriesapp.model.countryinfo.Translation
import com.nasp.countriesapp.room.CountriesDatabase
import com.nasp.countriesapp.room.utils.TranslationConverters

class TranslationRepository(
    private val roomDatabase: CountriesDatabase
) {

    suspend fun getAll() = roomDatabase.translationDao().getAll()

    suspend fun insert(translation: Pair<String, Translation>): Long =
        roomDatabase.translationDao().insertTranslation(
            TranslationConverters.toEntity(translation)
        )
}