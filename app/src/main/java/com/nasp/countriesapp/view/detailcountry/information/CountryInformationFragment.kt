package com.nasp.countriesapp.view.detailcountry.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nasp.countriesapp.databinding.FragmentInformationBinding
import com.nasp.countriesapp.utils.BitmapUtils
import com.nasp.countriesapp.utils.toReadable
import com.nasp.countriesapp.utils.view.ListItemView
import com.nasp.countriesapp.view.detailcountry.CountryPagerAdapter
import com.nasp.countriesapp.view.detailcountry.DetailCountriesViewModel

class CountryInformationFragment : Fragment() {

    private lateinit var binding: FragmentInformationBinding
    private val viewModel: DetailCountriesViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.countriesEntity.observe(viewLifecycleOwner) { countryEntity ->
            binding.countryEntity = countryEntity

            binding.nativeNameItem.setTitleOrHide(countryEntity.nativeNameValue
                .takeUnless { it.isEmpty() }
                ?: countryEntity.name)
            binding.capitalItem.setTitleOrHide(countryEntity.capital.takeUnless { it.isEmpty() }
                ?.joinToString(","))
            binding.populationItem.setTitleOrHide(countryEntity.population.toReadable())
            binding.areaItem.setTitleOrHide(countryEntity.area.toReadable())

            binding.continentItem.setTitleOrHide(countryEntity.continents.joinToString(", "))
            binding.timezonesItem.setTitleOrHide(countryEntity.timezones.joinToString(", "))
            binding.bordersItem.setTitleOrHide(countryEntity.borders.joinToString(", "))
            binding.carSideItem.setTitleOrHide(countryEntity.carSide.replaceFirstChar { it.uppercase() })
            binding.unLogo.setImageBitmap(
                if (countryEntity.unMember)
                    BitmapUtils.bitmapAssets(requireContext(), "onu.png")
                else
                    null
            )

            binding.unLogoCaption.visibility =
                if (countryEntity.unMember)
                    View.VISIBLE
                else
                    View.GONE

        }
        viewModel.setViewPagerPosition(CountryPagerAdapter.FRAGMENT_INFORMATION_POS)
    }

    private fun ListItemView.setTitleOrHide(string: String?) {
        if (string.isNullOrEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            setTitle(string)
        }
    }
}