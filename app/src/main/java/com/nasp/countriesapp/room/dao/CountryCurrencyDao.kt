package com.nasp.countriesapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.nasp.countriesapp.room.model.CountryCurrencyEntity

@Dao
interface CountryCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCountryCurrency(country: CountryCurrencyEntity): Long
}