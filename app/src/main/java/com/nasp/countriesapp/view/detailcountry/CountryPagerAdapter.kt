package com.nasp.countriesapp.view.detailcountry

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nasp.countriesapp.view.detailcountry.information.CountryInformationFragment

class CountryPagerAdapter(fm: FragmentManager,
                          lifecycle: Lifecycle,) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment =
        when (position) {
            FRAGMENT_INFORMATION_POS -> CountryInformationFragment()
            else -> error("Invalid fragment position in ${javaClass.simpleName}")
        }

    companion object {
        const val FRAGMENT_INFORMATION_POS = 0
    }
}