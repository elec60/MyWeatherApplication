package com.mousavi.hashem.weatherapp.data.local

interface Preferences {

    companion object {
        const val KEY_CITY_NAME = "key_city_name"
        const val NAME_OF_SHARED_PREF = "weather_shared_pref"
    }

    suspend fun getCurrentCity(): String
    suspend fun setCurrentCity(name: String): Boolean
}