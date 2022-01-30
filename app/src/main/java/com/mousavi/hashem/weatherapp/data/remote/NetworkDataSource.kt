package com.mousavi.hashem.weatherapp.data.remote

import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.data.remote.dto.LocationDataDto
import com.mousavi.hashem.weatherapp.data.remote.dto.WeatherDto

interface NetworkDataSource {

    suspend fun getLocationData(
        name: String,
    ): Either<List<LocationDataDto>, String>

    suspend fun getWeatherDataByDate(
        whereOnEarthID: Int,
        year: Int,
        month: Int,
        day: Int,
    ): Either<List<WeatherDto>, String>
}