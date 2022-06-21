package com.nasp.countriesapp

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.platform.app.InstrumentationRegistry
import com.nasp.countriesapp.model.countryinfo.Currency
import com.nasp.countriesapp.repositories.CurrencyRepository
import com.nasp.countriesapp.room.CountriesDatabase
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class DbTest {

    private lateinit var applicationContext: Context
    private lateinit var database: RoomDatabase
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        this.applicationContext = InstrumentationRegistry.getInstrumentation().context
        val database = Room.inMemoryDatabaseBuilder(
            applicationContext, CountriesDatabase::class.java
        ).build()
        this.database = database
        this.currencyRepository = CurrencyRepository(database)
    }

    @Test
    fun testSaveSameCurrencyMultiple() {
        val mockCurrency = "EUR" to Currency("Euro", "€")

        runBlocking {
            val savedIds = mutableListOf<Long>()

            repeat(Random(256).nextInt(2, 10)) {
                savedIds.add(currencyRepository.insert(mockCurrency))
            }

            Assert.assertEquals(2, savedIds.groupBy { it }.size)
            Assert.assertEquals(savedIds.size - 1, savedIds.count { it == -1L })
            val currencies = currencyRepository.getAll()

            Assert.assertFalse(currencies.isEmpty())
            Assert.assertEquals(1, currencies.size)
        }
    }

    @After
    fun clearDb() {
        database.clearAllTables()
    }
}