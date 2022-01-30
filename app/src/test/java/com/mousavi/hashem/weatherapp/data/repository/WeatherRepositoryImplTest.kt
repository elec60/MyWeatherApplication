package com.mousavi.hashem.weatherapp.data.repository


import com.google.common.truth.Truth.assertThat
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.data.local.Preferences
import com.mousavi.hashem.weatherapp.data.remote.Api
import com.mousavi.hashem.weatherapp.data.remote.NetworkDataSource
import com.mousavi.hashem.weatherapp.data.remote.dto.LocationDataDto
import com.mousavi.hashem.weatherapp.data.remote.dto.WeatherDto
import com.mousavi.hashem.weatherapp.domain.entity.LocationData
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class WeatherRepositoryImplTest {

    private lateinit var repository: WeatherRepository
    private lateinit var networkDataSource: NetworkDataSource
    private lateinit var preferences: Preferences

    @Before
    fun setUp() {
        networkDataSource = Mockito.mock(NetworkDataSource::class.java)
        preferences = Mockito.mock(Preferences::class.java)
        repository = WeatherRepositoryImpl(networkDataSource, preferences)
    }

    @Test
    fun `test getLocationData returns error when cache is not exist`() = runBlocking<Unit> {
        `when`(preferences.getCurrentCity()).thenReturn("Stockholm")
        `when`(networkDataSource.getLocationData("Stockholm")).thenReturn(Either.Error("custom error"))
        val locationData = repository.getLocationData()
        assertThat(locationData).isInstanceOf(Either.Error::class.java)
        assertThat((locationData as Either.Error).error).isEqualTo("custom error")
    }

    @Test
    fun `test getLocationData returns success when cache is not exist`() = runBlocking<Unit> {
        `when`(preferences.getCurrentCity()).thenReturn("Stockholm")
        `when`(networkDataSource.getLocationData("Stockholm")).thenReturn(
            Either.Success(
                listOf(
                    LocationDataDto(
                        lattLong = "1,2",
                        locationType = "type",
                        title = "title",
                        1000
                    )
                )
            )

        )
        val locationData = repository.getLocationData()
        assertThat(locationData).isInstanceOf(Either.Success::class.java)
        assertThat((locationData as Either.Success).data).isEqualTo(
            listOf(LocationData(whereOnEarthID = 1000))
        )
    }

    @Test
    fun `test getLocationData returns success when cache is exist`() = runBlocking<Unit> {
        `when`(preferences.getCurrentCity()).thenReturn("Stockholm")
        `when`(networkDataSource.getLocationData("Stockholm")).thenReturn(
            Either.Success(
                listOf(
                    LocationDataDto(
                        lattLong = "1,2",
                        locationType = "type",
                        title = "title",
                        1000
                    )
                )
            )
        )

        repository.getLocationData()// fill map with data(key is Stockholm)

        //insure we don't go inside when branches
        `when`(networkDataSource.getLocationData("Stockholm")).thenReturn(null)

        val locationData = repository.getLocationData()
        assertThat(locationData).isInstanceOf(Either.Success::class.java)
        assertThat((locationData as Either.Success).data).isEqualTo(
            listOf(LocationData(whereOnEarthID = 1000))
        )
    }

    @Test
    fun `test getWeatherDataByDate returns success`() = runBlocking<Unit> {
        val list = listOf(
            WeatherDto(
                airPressure = 0.0,
                applicableDate = "2020-02-02",
                created = "",
                humidity = 0,
                id = 1L,
                maxTemp = 1.0,
                minTemp = 0.0,
                predictability = 0,
                theTemp = 1.0,
                visibility = 1.0,
                weatherStateName = "Snowy",
                weatherStateAbbr = "s",
                windSpeed = 0.0,
                windDirection = 0.0,
                windDirectionCompass = ""
            )
        )

        `when`(preferences.getCurrentCity()).thenReturn("Stockholm")
        `when`(networkDataSource.getWeatherDataByDate(1001, 2020, 2, 2))
            .thenReturn(
                Either.Success(list)
            )

        val result = repository.getWeatherDataByDate(1001, 2020, 2, 2)
        assertThat(result).isInstanceOf(Either.Success::class.java)
        assertThat((result as Either.Success).data).isEqualTo(
            listOf(
                Weather(
                    id = 1L,
                    applicableDate = "2020-02-02",
                    created = "",
                    maxTemp = "1°C",
                    minTemp = "0°C",
                    theTemp = "1",
                    icon = "${Api.IMAGE_BASE_URL}s.png",
                    weatherStateName = "Snowy",
                    city = "Stockholm"
                )
            )
        )

    }

    @Test
    fun `test getWeatherDataByDate returns error`() = runBlocking<Unit> {
        `when`(networkDataSource.getWeatherDataByDate(1001, 2020, 2, 2))
            .thenReturn(
                Either.Error("error!!")
            )

        val result = repository.getWeatherDataByDate(1001, 2020, 2, 2)
        assertThat(result).isInstanceOf(Either.Error::class.java)
        assertThat((result as Either.Error).error).isEqualTo("error!!")
    }

    @Test
    fun `test setCurrentCity`() = runBlocking<Unit> {
        repository.setCurrentCity("London")
        verify(preferences, times(1)).setCurrentCity("London")
    }


}