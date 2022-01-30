package com.mousavi.hashem.weatherapp.data

import androidx.annotation.StringRes

interface StringProvider {

    fun getString(@StringRes id: Int): String

    fun getStringArray(id: Int): Array<String>
}