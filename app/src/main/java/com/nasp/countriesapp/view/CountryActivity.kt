package com.nasp.countriesapp.view

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.nasp.countriesapp.databinding.ActivityCountriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryActivity: FragmentActivity() {

    private lateinit var binding: ActivityCountriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}