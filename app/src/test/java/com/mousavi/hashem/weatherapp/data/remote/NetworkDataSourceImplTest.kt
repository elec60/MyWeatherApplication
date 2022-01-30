package com.mousavi.hashem.weatherapp.data.remote


import com.google.common.truth.Truth.assertThat
import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.data.StringProvider
import com.mousavi.hashem.weatherapp.data.remote.dto.LocationDataDto
import com.mousavi.hashem.weatherapp.data.remote.dto.WeatherDto
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.given
import retrofit2.HttpException
import java.io.IOException

class NetworkDataSourceImplTest {

    private lateinit var api: Api
    private lateinit var networkDataSource: NetworkDataSource
    private lateinit var stringProvider: StringProvider

    @Before
    fun setUp() {
        api = Mockito.mock(Api::class.java)
        stringProvider = Mockito.mock(StringProvider::class.java)
        networkDataSource = NetworkDataSourceImpl(api, stringProvider)
    }

    @Test
    fun `test HttpException in getLocationData`(): Unit = runBlocking {
        val exception = Mockito.mock(HttpException::class.java)
        `when`(api.getLocationData("Gothenburg"))
            .thenThrow(exception)
        `when`(stringProvider.getString(R.string.error_occurred))
            .thenReturn("error_occurred")

        val result = networkDataSource.getLocationData("Gothenburg")
        assertThat(result).isInstanceOf(Either.Error::class.java)
        val error = result as Either.Error
        assertThat(error.error).isEqualTo("error_occurred")
    }

    @Test
    fun `test IOException in getLocationData`(): Unit = runBlocking {
        given(api.getLocationData("Gothenburg")).willAnswer {
            throw IOException()
        }
        `when`(stringProvider.getString(R.string.check_internet_connection))
            .thenReturn("Check your internet connection!")

        val result = networkDataSource.getLocationData("Gothenburg")
        assertThat(result).isInstanceOf(Either.Error::class.java)
        val error = result as Either.Error
        assertThat(error.error).isEqualTo("Check your internet connection!")
    }

    @Test
    fun `test Exception in getLocationData`(): Unit = runBlocking {
        given(api.getLocationData("Gothenburg")).willAnswer {
            throw Exception()
        }
        `when`(stringProvider.getString(R.string.unknown_error))
            .thenReturn("Unknown error!")

        val result = networkDataSource.getLocationData("Gothenburg")
        assertThat(result).isInstanceOf(Either.Error::class.java)
        val error = result as Either.Error
        assertThat(error.error).isEqualTo("Unknown error!")
    }

    @Test
    fun `test success in getLocationData`(): Unit = runBlocking {
        `when`(api.getLocationData("Gothenburg"))
            .thenReturn(listOf(LocationDataDto("1,2", "type", "title", 100)))

        val result = networkDataSource.getLocationData("Gothenburg")

        assertThat(result).isInstanceOf(Either.Success::class.java)
        val data = result as Either.Success
        assertThat(data.data).isEqualTo(listOf(LocationDataDto("1,2", "type", "title", 100)))
    }

    @Test
    fun `test HttpException in getWeatherDataByDate`(): Unit = runBlocking {
        val exception = Mockito.mock(HttpException::class.java)
        `when`(api.getWeatherDataByDate(10, 2020, 1, 1))
            .thenThrow(exception)
        `when`(stringProvider.getString(R.string.error_occurred))
            .thenReturn("error_occurred")

        val result = networkDataSource.getWeatherDataByDate(10, 2020, 1, 1)
        assertThat(result).isInstanceOf(Either.Error::class.java)
        val error = result as Either.Error
        assertThat(error.error).isEqualTo("error_occurred")
    }

    @Test
    fun `test IOException in getWeatherDataByDate`(): Unit = runBlocking {
        given(api.getWeatherDataByDate(10, 2020, 1, 1)).willAnswer {
            throw IOException()
        }
        `when`(stringProvider.getString(R.string.check_internet_connection))
            .thenReturn("Check your internet connection!")

        val result = networkDataSource.getWeatherDataByDate(10, 2020, 1, 1)
        assertThat(result).isInstanceOf(Either.Error::class.java)
        val error = result as Either.Error
        assertThat(error.error).isEqualTo("Check your internet connection!")
    }

    @Test
    fun `test Exception in getWeatherDataByDate`(): Unit = runBlocking {
        given(api.getWeatherDataByDate(10, 2020, 1, 1)).willAnswer {
            throw Exception()
        }
        `when`(stringProvider.getString(R.string.unknown_error))
            .thenReturn("Unknown error!")

        val result = networkDataSource.getWeatherDataByDate(10, 2020, 1, 1)
        assertThat(result).isInstanceOf(Either.Error::class.java)
        val error = result as Either.Error
        assertThat(error.error).isEqualTo("Unknown error!")
    }

    @Test
    fun `test success in getWeatherDataByDate`(): Unit = runBlocking {
        val list = listOf(
            WeatherDto(
                airPressure = 0.0,
                applicableDate = "2020-01-01",
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
        `when`(api.getWeatherDataByDate(10, 2020, 1, 1))
            .thenReturn(list)

        val result = networkDataSource.getWeatherDataByDate(10, 2020, 1, 1)

        assertThat(result).isInstanceOf(Either.Success::class.java)
        val data = result as Either.Success
        assertThat(data.data).isEqualTo(list)
    }


}