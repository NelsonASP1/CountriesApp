package com.nasp.countriesapp.view.country

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nasp.countriesapp.repositories.CountryRepository
import com.nasp.countriesapp.room.model.CountriesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CountriesListViewModel @Inject constructor(
    application: Application,
    private val countriesRepository: CountryRepository
) : AndroidViewModel(application) {

    private var countriesEntityList = emptyList<CountriesEntity>()

    private val mutableIdImage = MutableLiveData<Map<Long, Bitmap>>()
    private val mutableCountryList = MutableLiveData<List<CountriesEntity>>()
    private val mutableSearchText = MutableLiveData<String>()

    val countryList: LiveData<List<CountriesEntity>>
        get() = mutableCountryList
    val idImage: LiveData<Map<Long, Bitmap>>
        get() = mutableIdImage
    val searchText: LiveData<String>
        get() = mutableSearchText

    fun getCountries() {
        viewModelScope.launch {
            if (countriesEntityList.isNotEmpty()) {
                return@launch
            }

            countriesEntityList = withContext(Dispatchers.IO) {
                countriesRepository.getAll()
            }

            mutableCountryList.value = countriesEntityList

            withContext(Dispatchers.IO) {
                startWatchingImageCache()
            }
        }
    }

    suspend fun startWatchingImageCache() {
        val missingImagesCountry = countriesEntityList
            .sortedBy { it.name }
            .map { it.id }
        val doneList = buildList {
            repeat(countriesEntityList.size) {
                add(false)
            }
        }.toMutableList()

        val finalMap = mutableMapOf<Long, Bitmap>()

        while (true) {
            if (doneList.all { it }) {
                break
            }

            for (countryIdIndexed in missingImagesCountry.withIndex()) {
                if (doneList[countryIdIndexed.index]) {
                    continue
                }

                val file = File(
                    getApplication<Application>().cacheDir,
                    "${countryIdIndexed.value}-flag.png"
                )
                if (!file.exists()) {
                    continue
                }

                BitmapFactory.decodeFile(
                    file.absolutePath
                )?.let {
                    withContext(Dispatchers.Main) {
                        mutableIdImage.value = finalMap.apply {
                            put(countryIdIndexed.value, it)
                        }


                    }

                    doneList[countryIdIndexed.index] = true
                }
            }

            delay(200)
        }
    }

    fun setSearchText(text: String) {
        mutableSearchText.value = text
    }
}