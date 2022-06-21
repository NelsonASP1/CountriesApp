package com.nasp.countriesapp.repositories

import com.nasp.countriesapp.model.countryinfo.Currency
import com.nasp.countriesapp.room.CountriesDatabase
import com.nasp.countriesapp.room.utils.CurrencyConverters

class CurrencyRepository(
    private val roomDatabase: CountriesDatabase
) {

    suspend fun getAll() = roomDatabase.currencyDao().getAll()

    suspend fun insert(currenciesPair: Pair<String, Currency>) = roomDatabase.currencyDao().insertCurrency(
        CurrencyConverters.toEntity(currenciesPair)
    )
}