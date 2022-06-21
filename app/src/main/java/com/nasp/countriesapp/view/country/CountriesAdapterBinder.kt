package com.nasp.countriesapp.view.country

import android.graphics.Bitmap
import com.nasp.countriesapp.room.model.CountriesEntity

interface CountriesAdapterBinder {

    fun onCountriesResults(countryEntities: List<CountriesWrapper>)

    fun onNoCountry()

    fun openDetails(countriesEntity: CountriesEntity, bitmap: Bitmap?)
}
