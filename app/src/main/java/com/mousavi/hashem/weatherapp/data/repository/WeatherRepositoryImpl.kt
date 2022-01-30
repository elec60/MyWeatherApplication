package com.mousavi.hashem.weatherapp.data.repository

import androidx.collection.arrayMapOf
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.data.local.Preferences
import com.mousavi.hashem.weatherapp.data.remote.NetworkDataSource
import com.mousavi.hashem.weatherapp.domain.entity.LocationData
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val preferences: Preferences
) : WeatherRepository {

    private val locationMaps = arrayMapOf<String, List<LocationData>>()

    override suspend fun getLocationData(): Either<List<LocationData>, String> {
        val currentCity = preferences.getCurrentCity()
        if (locationMaps.containsKey(currentCity)) return Either.Success(locationMaps[currentCity]!!)

        return when (val locationData = networkDataSource.getLocationData(currentCity)) {
            is Either.Success -> {
                val map = locationData.data.map { it.toLocationData() }
                locationMaps[currentCity] = map
                Either.Success(map)
            }
            is Either.Error -> {
                Either.Error(locationData.error)
            }
        }
    }

    override suspend fun getWeatherDataByDate(
        whereOnEarthID: Int,
        year: Int,
        month: Int,
        day: Int,
    ): Either<List<Weather>, String> {
        return when (
            val result = networkDataSource.getWeatherDataByDate(
                whereOnEarthID,
                year,
                month,
                day
            )
        ) {
            is Either.Success -> {
                val currentCity = preferences.getCurrentCity()
                Either.Success(result.data.map { it.toWeather(currentCity) })
            }
            is Either.Error -> {
                Either.Error(result.error)
            }
        }
    }

    override suspend fun setCurrentCity(name: String): Boolean {
        return preferences.setCurrentCity(name)
    }
}