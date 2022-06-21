package com.nasp.countriesapp.room.utils

import com.nasp.countriesapp.model.countryinfo.Currency
import com.nasp.countriesapp.room.model.CurrencyEntity


object CurrencyConverters {

    fun toEntity(currency: Pair<String?, Currency?>?) = CurrencyEntity(
        0,
        currency?.first ?: "",
        currency?.second?.name ?: "",
        currency?.second?.symbol ?: ""
    )
}