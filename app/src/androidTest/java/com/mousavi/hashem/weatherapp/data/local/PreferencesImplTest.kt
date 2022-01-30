package com.mousavi.hashem.weatherapp.data.local


import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.data.StringProvider
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class PreferencesImplTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferences: Preferences
    private lateinit var stringProvider: StringProvider


    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        sharedPreferences = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        stringProvider = Mockito.mock(StringProvider::class.java)
        preferences = PreferencesImpl(sharedPreferences, stringProvider)
    }

    @Test
    fun test_getCurrentCity_when_default_value_not_used() = runBlocking<Unit> {
        Mockito.`when`(stringProvider.getStringArray(R.array.cities))
            .thenReturn(arrayOf("city1", "city2"))
        sharedPreferences.edit().putString(Preferences.KEY_CITY_NAME, "Tehran").commit()

        val currentCity = preferences.getCurrentCity()

        assertThat(currentCity).isEqualTo("Tehran")
    }


    @Test
    fun test_getCurrentCity_when_default_value_used() = runBlocking<Unit> {
        Mockito.`when`(stringProvider.getStringArray(R.array.cities))
            .thenReturn(arrayOf("city1", "city2"))
        sharedPreferences.edit().putString(Preferences.KEY_CITY_NAME, null).commit()
        val currentCity = preferences.getCurrentCity()

        assertThat(currentCity).isEqualTo("city1")
    }

    @Test
    fun test_getCurrentCity() = runBlocking<Unit> {
        Mockito.`when`(stringProvider.getStringArray(R.array.cities))
            .thenReturn(arrayOf("city1", "city2"))

        preferences.setCurrentCity("Tehran")

        assertThat(preferences.getCurrentCity()).isEqualTo("Tehran")

    }
}