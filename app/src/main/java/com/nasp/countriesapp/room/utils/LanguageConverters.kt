package com.nasp.countriesapp.room.utils

import com.nasp.countriesapp.room.model.LanguageEntity


object LanguageConverters {

    fun toEntity(languagePair: Pair<String?, String?>?) = LanguageEntity(
        0,
        languagePair?.first ?: "",
        languagePair?.second ?: ""
    )
}