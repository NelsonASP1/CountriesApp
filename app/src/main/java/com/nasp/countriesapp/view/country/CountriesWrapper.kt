package com.nasp.countriesapp.view.country

import android.graphics.Bitmap
import com.nasp.countriesapp.room.model.CountriesEntity

class CountriesWrapper(
    val countriesEntity: CountriesEntity,
    var flagBitmap: Bitmap?
)