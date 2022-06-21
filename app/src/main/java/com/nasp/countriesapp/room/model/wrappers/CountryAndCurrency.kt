package com.nasp.countriesapp.room.model.wrappers

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nasp.countriesapp.room.model.CountryCurrencyEntity
import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.room.model.CurrencyEntity

class CountryAndCurrency(
    @Embedded val countriesEntity: CountriesEntity,
    @Relation(entity = CurrencyEntity::class, parentColumn = "country_id", entityColumn = "currency_id", associateBy = Junction(
        value = CountryCurrencyEntity::class,
        parentColumn = "country_id",
        entityColumn = "currency_id"
    ))
    val currenciesEntity: List<CurrencyEntity>
)