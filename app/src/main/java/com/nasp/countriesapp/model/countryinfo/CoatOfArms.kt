package com.nasp.countriesapp.model.countryinfo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CoatOfArms(
    @SerializedName("png")
    @Expose
    val png: String?
)