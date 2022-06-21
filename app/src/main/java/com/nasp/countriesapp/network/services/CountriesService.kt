package com.nasp.countriesapp.network.services

import com.nasp.countriesapp.model.Country
import retrofit2.http.GET

interface CountriesService {

    @GET("all")
    suspend fun getAll(): List<Country>
}