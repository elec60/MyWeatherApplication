package com.mousavi.hashem.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName
import com.mousavi.hashem.weatherapp.data.remote.Api.Companion.IMAGE_BASE_URL
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import kotlin.math.roundToInt

data class WeatherDto(
    @SerializedName("air_pressure")
    val airPressure: Double,
    @SerializedName("applicable_date")
    val applicableDate: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("id")
    val id: Long,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("min_temp")
    val minTemp: Double,
    @SerializedName("predictability")
    val predictability: Int,
    @SerializedName("the_temp")
    val theTemp: Double,
    @SerializedName("visibility")
    val visibility: Double,
    @SerializedName("weather_state_abbr")
    val weatherStateAbbr: String,
    @SerializedName("weather_state_name")
    val weatherStateName: String,
    @SerializedName("wind_direction")
    val windDirection: Double,
    @SerializedName("wind_direction_compass")
    val windDirectionCompass: String,
    @SerializedName("wind_speed")
    val windSpeed: Double,
) {
    fun toWeather(currentCity: String): Weather {
        return Weather(
            id = id,
            applicableDate = applicableDate,
            created = created,
            maxTemp = "${maxTemp.roundToInt()}°C",
            minTemp = "${minTemp.roundToInt()}°C",
            theTemp = theTemp.roundToInt().toString(),
            icon = "$IMAGE_BASE_URL$weatherStateAbbr.png",
            weatherStateName = weatherStateName,
            city = currentCity
        )
    }
}