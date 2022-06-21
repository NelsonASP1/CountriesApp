package com.nasp.countriesapp.room.utils

import com.nasp.countriesapp.model.countryinfo.Translation
import com.nasp.countriesapp.room.model.TranslationEntity

object TranslationConverters {

    fun toEntity(translation: Pair<String?, Translation?>?) = TranslationEntity(
        0,
        translation?.first ?: "",
        translation?.second?.common ?: "",
        translation?.second?.official ?: ""
    )
}