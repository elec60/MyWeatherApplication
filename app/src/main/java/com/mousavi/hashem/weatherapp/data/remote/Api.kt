package com.mousavi.hashem.weatherapp.data.remote

import com.mousavi.hashem.weatherapp.data.remote.dto.LocationDataDto
import com.mousavi.hashem.weatherapp.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    companion object {
        const val BASE_URL = "https://www.metaweather.com/api/location/"
        const val IMAGE_BASE_URL = "https://www.metaweather.com/static/img/weather/png/64/"
    }

    @GET("search/")
    suspend fun getLocationData(
        @Query("search") name: String,
    ): LocationDataDto

    @GET("{woeid}/{year}/{month}/{day}/")
    suspend fun getWeatherDataByDate(
        @Path("woeid") whereOnEarthID: Int,
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int,
    ): List<WeatherDto>

}