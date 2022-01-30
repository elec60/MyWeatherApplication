package com.mousavi.hashem.weatherapp.domain.entity


data class Weather(
    val id: Long,
    val applicableDate: String,
    val created: String,
    val maxTemp: String,
    val minTemp: String,
    val theTemp: String,
    val icon: String,
    val weatherStateName: String,
    val city: String,
) {
    companion object {
        val Default = Weather(
            id = -1,
            applicableDate = "",
            created = "",
            maxTemp = "",
            minTemp = "",
            theTemp = "",
            icon = "",
            weatherStateName = "",
            city = ""
        )
    }
}