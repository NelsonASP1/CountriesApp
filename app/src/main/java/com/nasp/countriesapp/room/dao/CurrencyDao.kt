package com.nasp.countriesapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nasp.countriesapp.room.model.CurrencyEntity


@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCurrency(country: CurrencyEntity): Long

    @Query("SELECT * FROM currencies_table WHERE currencies_table.currency_id == :id")
    suspend fun getCurrency(id: Int): CurrencyEntity

    @Query("SELECT * FROM currencies_table")
    suspend fun getAll(): List<CurrencyEntity>
}