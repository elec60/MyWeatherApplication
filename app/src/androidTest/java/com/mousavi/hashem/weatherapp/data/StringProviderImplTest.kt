package com.mousavi.hashem.weatherapp.data


import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.mousavi.hashem.weatherapp.R
import org.junit.Before
import org.junit.Test

class StringProviderImplTest {

    private lateinit var context: Context
    private lateinit var stringProvider: StringProvider


    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        stringProvider = StringProviderImpl(context)
    }

    @Test
    fun getString() {
        assertThat(stringProvider.getString(R.string.no_data_for_this_city)).isEqualTo("No data for this city")
    }

    @Test
    fun getStringArray() {
        val realData = listOf(
            "Gothenburg",
            "Stockholm",
            "Mountain View",
            "London",
            "New York",
            "Berlin"
        )
        val cities = stringProvider.getStringArray(R.array.cities)
        cities.forEachIndexed { index, s ->
            assertThat(s).isEqualTo(realData[index])
        }
    }
}