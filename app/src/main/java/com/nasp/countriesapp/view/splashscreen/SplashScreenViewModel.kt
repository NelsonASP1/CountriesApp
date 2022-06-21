package com.nasp.countriesapp.view.splashscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.*
import com.nasp.countriesapp.model.Country
import com.nasp.countriesapp.repositories.CountryRepository
import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.room.utils.toEntity
import com.nasp.countriesapp.room.utils.toModel
import com.nasp.countriesapp.utils.toTwoDecimals
import com.nasp.countriesapp.worker.DownloadImagesForCountryWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    application: Application,
    private val countriesRepository: CountryRepository
) : AndroidViewModel(application) {

    private val mutableIsLoadingDone = MutableLiveData<SplashScreenStatus>()
    private val mutableProgressPercentage = MutableLiveData<Int>()

    val isLoadingDone: LiveData<SplashScreenStatus>
        get() = mutableIsLoadingDone
    val progressPercentage: LiveData<Int>
        get() = mutableProgressPercentage

    fun loadCountries() {
        viewModelScope.launch {
            mutableIsLoadingDone.value = SplashScreenStatus.LOADING

            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    fetchAndSaveCountries()

                    startWorker(countriesRepository.getAll())
                    delay(1000L)
                }

                mutableProgressPercentage.value = 100
                mutableIsLoadingDone.value = SplashScreenStatus.SUCCESS

            }.exceptionOrNull()?.let {
                it.printStackTrace()
                if (countriesRepository.getAll().isEmpty()) {
                    mutableIsLoadingDone.value = SplashScreenStatus.FAILED_BUT_NO_COUNTRIES
                } else {
                    mutableIsLoadingDone.value = SplashScreenStatus.ERROR
                }
            }
        }
    }

    private suspend fun fetchAndSaveCountries() = mutableMapOf<Country, Long>().apply {
        withContext(Dispatchers.Main) {
            mutableProgressPercentage.value = 0
        }

        val countryList = countriesRepository.fetchCountries().sortedBy { it.name.common }

        withContext(Dispatchers.Main) {
            mutableProgressPercentage.value = 10
        }

        val savedCountryEntityList = countriesRepository.getAll().sortedBy { it.name }

        val countryListToRemove = savedCountryEntityList - countryList.map { it.toEntity() }

        countryListToRemove.forEach {
            countriesRepository.removeCountry(it)
        }

        withContext(Dispatchers.Main) {
            mutableProgressPercentage.value = 15
        }

        val countryListToSave = getCountriesToSave(countryList, savedCountryEntityList)

        withContext(Dispatchers.Main) {
            mutableProgressPercentage.value = 20
        }

        val toSaveSize = countryListToSave.size.toFloat()

        if (toSaveSize > 0) {
            val incrementPercentage = (80F / toSaveSize).toTwoDecimals()
            var completionPercentage = 20F

            countryListToSave.forEach {
                val savedCountryId = countriesRepository.saveCountry(it).also { id ->
                    if (id == -1L) {
                        Log.w(
                            javaClass.simpleName,
                            "Failed to save country ${it.name.common}, Room returned -1."
                        )

                        withContext(Dispatchers.Main) {
                            mutableIsLoadingDone.value = SplashScreenStatus.ERROR
                        }

                        return@apply
                    } else if (id == -2L) {
                        Log.i(
                            javaClass.simpleName,
                            "Ignoring country ${it.name.common}, no changes detected."
                        )
                    }
                }

                put(it, savedCountryId)

                completionPercentage += incrementPercentage

                withContext(Dispatchers.Main) {
                    mutableProgressPercentage.value = completionPercentage.roundToInt()
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                mutableProgressPercentage.value = 100
            }
        }
    }

    private fun startWorker(countryList: List<CountriesEntity>) {
        val dataFlags = Data.Builder().apply {
            putString(
                DownloadImagesForCountryWorker.KEY,
                DownloadImagesForCountryWorker.FLAGS_URL
            )

            countryList

                .forEach {
                putString("${it.id}-flag", it.flagsPng)
            }
        }.build()
        val dataCoats = Data.Builder().apply {
            putString(
                DownloadImagesForCountryWorker.KEY,
                DownloadImagesForCountryWorker.COATS_URL
            )

            countryList.forEach {
                putString("${it.id}-coat", it.coatOfArmsPng)
            }
        }.build()

        WorkManager.getInstance(getApplication())
            .beginWith(
                OneTimeWorkRequest.Builder(DownloadImagesForCountryWorker::class.java)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .setInputData(dataFlags)
                    .build()
            ).then(
                OneTimeWorkRequest.Builder(DownloadImagesForCountryWorker::class.java)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .setInputData(dataCoats)
                    .build()
            )
            .enqueue()
    }

    private fun getCountriesToSave(
        countryList: List<Country>,
        savedCountriesEntityList: List<CountriesEntity>
    ): List<Country> {
        val savedCountryList = savedCountriesEntityList.map { it.toModel() }
        val modified = countryList
            .zip(savedCountryList)
            .asSequence()
            .filter { pair -> pair.first != pair.second }
            .map { it.first }
            .toList()
        val added = countryList - savedCountryList
        return added + modified
    }
}