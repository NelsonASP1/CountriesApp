package com.nasp.countriesapp.room.model.wrappers

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.room.model.CountryTranslationEntity
import com.nasp.countriesapp.room.model.TranslationEntity

class CountryAndTranslation(
    @Embedded val countriesEntity: CountriesEntity,
    @Relation(entity = TranslationEntity::class, parentColumn = "country_id", entityColumn = "translation_id", associateBy = Junction(
        value = CountryTranslationEntity::class,
        parentColumn = "country_id",
        entityColumn = "translation_id"
    ))
    val translationEntity: List<TranslationEntity>
)