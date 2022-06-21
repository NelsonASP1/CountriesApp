package com.nasp.countriesapp.room.model.wrappers

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.room.model.CountryLanguageEntity
import com.nasp.countriesapp.room.model.LanguageEntity

class CountryAndLanguage(
    @Embedded val countriesEntity: CountriesEntity,
    @Relation(entity = LanguageEntity::class, parentColumn = "country_id", entityColumn = "language_id", associateBy = Junction(
        value = CountryLanguageEntity::class,
        parentColumn = "country_id",
        entityColumn = "language_id"
    ))
    val languagesEntity: List<LanguageEntity>
)