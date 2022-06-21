package com.nasp.countriesapp.room.utils

import com.nasp.countriesapp.model.Country
import com.nasp.countriesapp.model.countryinfo.*

import com.nasp.countriesapp.room.model.CountriesEntity
import com.nasp.countriesapp.worker.DownloadImagesForCountryWorker


fun Country.toEntity(): CountriesEntity = CountriesEntity(
    0,
    altSpellings ?: emptyList(),
    area,
    borders ?: emptyList(),
    capital ?: emptyList(),
    capitalInfo?.latlng ?: emptyList(),
    car?.side ?: "",
    car?.signs ?: emptyList(),
    coatOfArms?.png
        ?.replace(DownloadImagesForCountryWorker.COATS_URL, "")
        ?: "",
    continents ?: emptyList(),
    demonyms?.eng?.male ?: "",
    demonyms?.eng?.female ?: "",
    fifa ?: "",
    flag ?: "",
    flags?.png
        ?.replace(DownloadImagesForCountryWorker.FLAGS_URL, "")
        ?: "",
    independent,
    landlocked,
    latlng ?: emptyList(),
    name.common,
    name.nativeName?.first ?: "",
    name.nativeName?.second ?: "",
    name.official,
    population,
    region ?: "",
    startOfWeek ?: "",
    status ?: "",
    subregion ?: "",
    timezones ?: emptyList(),
    tld ?: emptyList(),
    unMember
)

fun CountriesEntity.toModel() = Country(
    id,
    altSpellings,
    area,
    borders,
    capital,
    CapitalInfo(capitalLatLng),
    Car(carSide, carSigns),
    CoatOfArms("${DownloadImagesForCountryWorker.COATS_URL}$coatOfArmsPng"),
    continents,
    Demonyms(DemonymEnglish(demonymMale, demonymFemale)),
    fifa,
    flagEmoji,
    Flags("${DownloadImagesForCountryWorker.FLAGS_URL}$flagsPng"),
    independent,
    landlocked,
    latlng,
    Name(name, nativeNameKey to nativeNameValue, officialName),
    population,
    region,
    startOfWeek,
    status,
    subregion,
    timezones,
    tld,
    unMember
)