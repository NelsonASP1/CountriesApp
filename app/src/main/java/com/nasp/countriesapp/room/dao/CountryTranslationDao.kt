package com.nasp.countriesapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.nasp.countriesapp.room.model.CountryTranslationEntity

@Dao
interface CountryTranslationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCountryTranslation(countryTranslationEntity: CountryTranslationEntity): Long
}