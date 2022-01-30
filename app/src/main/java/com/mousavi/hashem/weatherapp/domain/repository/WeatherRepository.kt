package com.mousavi.hashem.weatherapp.domain.repository

import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.domain.entity.LocationData
import com.mousavi.hashem.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getLocationData(): Either<List<LocationData>, String>

    suspend fun getWeatherDataByDate(
        whereOnEarthID: Int,
        year: Int,
        month: Int,
        day: Int,
    ): Either<List<Weather>, String>

    suspend fun setCurrentCity(name: String): Boolean
}