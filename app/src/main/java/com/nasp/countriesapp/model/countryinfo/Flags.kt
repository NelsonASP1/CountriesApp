package com.nasp.countriesapp.model.countryinfo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Flags(
    @SerializedName("png")
    @Expose
    val png: String
)