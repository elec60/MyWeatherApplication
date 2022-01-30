package com.mousavi.hashem.weatherapp.data.local

import android.content.SharedPreferences
import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.data.StringProvider
import javax.inject.Inject

class PreferencesImpl @Inject constructor(
    private val preferences: SharedPreferences,
    private val stringProvider: StringProvider,
) : Preferences {
    override suspend fun getCurrentCity(): String {
        val cities = stringProvider.getStringArray(R.array.cities)
        return preferences.getString(Preferences.KEY_CITY_NAME, cities[0])!!
    }

    override suspend fun setCurrentCity(name: String): Boolean {
        return preferences.edit().putString(Preferences.KEY_CITY_NAME, name).commit()
    }
}