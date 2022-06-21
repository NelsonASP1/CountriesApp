package com.nasp.countriesapp.view.detailcountry

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.nasp.countriesapp.room.model.CountriesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailCountriesViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val mutableImage = MutableLiveData<Bitmap?>()
    private val mutableCountriesEntity = MutableLiveData<CountriesEntity>()
    private val mutableViewPagerPosition = MutableLiveData<Int>()

    val image: LiveData<Bitmap?>
        get() = mutableImage
    val countriesEntity: LiveData<CountriesEntity>
        get() = mutableCountriesEntity
    val viewPagerPosition: LiveData<Int>
        get() = mutableViewPagerPosition

    fun setCountryEntity(countriesEntity: CountriesEntity) = viewModelScope.launch {
        mutableCountriesEntity.value = countriesEntity
    }

    fun setFlag(id: Long, bitmap: Bitmap?) = viewModelScope.launch {
        bitmap?.let {
            mutableImage.value = it
            return@launch
        }

        withContext(Dispatchers.IO) {
            bitmapFile("$id-flag.png", mutableImage)
        }
    }

    private suspend fun bitmapFile(
        fileName: String,
        referenceLiveData: MutableLiveData<Bitmap?>
    ) {
        while (true) {
            delay(1000)

            val file =
                File(getApplication<Application>().cacheDir, fileName)

            if (!file.exists()) {
                withContext(Dispatchers.Main) {
                    referenceLiveData.value = null
                }

                continue
            }

            BitmapFactory.decodeFile(
                file.absolutePath
            )?.let {
                withContext(Dispatchers.Main) {
                    referenceLiveData.value = it
                }
            }

            break
        }
    }

    fun setViewPagerPosition(position: Int) {
        mutableViewPagerPosition.value = position
    }
}