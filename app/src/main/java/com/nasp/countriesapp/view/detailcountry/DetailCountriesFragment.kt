package com.nasp.countriesapp.view.detailcountry

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.nasp.countriesapp.R
import com.nasp.countriesapp.databinding.FragmentCountryDetailsBinding
import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.utils.ColorUtils
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCountriesFragment : Fragment() {

    private lateinit var binding: FragmentCountryDetailsBinding
    private lateinit var pagerAdapter: CountryPagerAdapter

    private lateinit var countriesEntity: CountriesEntity
    private var image: Bitmap? = null

    private val viewModel: DetailCountriesViewModel by viewModels()
    private val args: DetailCountriesFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.countriesEntity = args.countriesEntity
        this.image = args.flag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setCountryEntity(countriesEntity)
        viewModel.setFlag(
            countriesEntity.id,
            image
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCountryDetailsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.country = countriesEntity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val hasBadContrast = KNOWN_BAD_CONTRAST_FLAGS.any { it.startsWith(countriesEntity.name) }
        binding.toolbar.navigationIcon?.setTint(

            ContextCompat.getColor(
                requireContext(),
                if (hasBadContrast)
                    R.color.black
                else
                    R.color.white
            )
        )

        viewModel.image.observe(viewLifecycleOwner) {
            it?.let { bitmap ->
                setupActionBar(hasBadContrast, bitmap)
            }
        }

        this.pagerAdapter =
            CountryPagerAdapter(childFragmentManager, lifecycle)
        binding.pager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                CountryPagerAdapter.FRAGMENT_INFORMATION_POS -> tab.text =
                    getString(R.string.country_info_label)
                else -> error("Invalid fragment position in ${javaClass.simpleName}, TabLayoutMediator")
            }
        }.attach()

        viewModel.viewPagerPosition.observe(viewLifecycleOwner) {
            if (binding.tabLayout.selectedTabPosition != it) {
                binding.tabLayout.getTabAt(it)!!.select()
            }
        }
    }

    private fun setupActionBar(hasBadContrast: Boolean, bitmap: Bitmap) {
        val colorPrimary: TypedArray =
            requireContext().obtainStyledAttributes(
                TypedValue().data,
                intArrayOf(
                    androidx.appcompat.R.attr.colorPrimary,
                    androidx.appcompat.R.attr.colorPrimaryDark
                )
            )

        val palette = Palette.Builder(bitmap)
            .generate()

        val color = palette.mutedSwatch?.rgb
            ?: palette.darkMutedSwatch?.rgb
            ?: palette.darkVibrantSwatch?.rgb
            ?: palette.vibrantSwatch?.rgb
            ?: colorPrimary.getColor(0, 0)
        val colorDark = ColorUtils.fillColor(color, 0.7F)

        colorPrimary.recycle()

        activity?.window?.statusBarColor = colorDark

        binding.collapsingToolbar.setContentScrimColor(color)
        binding.headerImage.setImageBitmap(bitmap)

        if (hasBadContrast) {
            binding.collapsingToolbar.setExpandedTitleTextColor(ColorStateList.valueOf(Color.BLACK))
        }

    }

    companion object {
        val KNOWN_BAD_CONTRAST_FLAGS = arrayOf("Afghanistan", "United States", "Bulgaria", "Canada")
    }
}