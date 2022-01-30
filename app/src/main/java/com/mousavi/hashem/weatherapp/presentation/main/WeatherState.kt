package com.mousavi.hashem.weatherapp.presentation.main

import com.mousavi.hashem.weatherapp.domain.entity.Weather

data class WeatherState(
    val loading: Boolean = false,
    val weather: Weather = Weather.Default,
    val error: String = "",
)
