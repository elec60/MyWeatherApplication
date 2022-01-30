package com.mousavi.hashem.weatherapp.data

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

class StringProviderImpl(
    private val appContext: Context,
) : StringProvider {
    override fun getString(@StringRes id: Int): String = appContext.getString(id)

    override fun getStringArray(@ArrayRes id: Int): Array<String> {
        return appContext.resources.getStringArray(id)
    }
}