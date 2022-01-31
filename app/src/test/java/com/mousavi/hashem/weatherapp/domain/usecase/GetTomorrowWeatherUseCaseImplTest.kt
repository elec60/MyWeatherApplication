package com.mousavi.hashem.weatherapp.domain.usecase


import com.google.common.truth.Truth.assertThat
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

class GetTomorrowWeatherUseCaseImplTest {

    private lateinit var repository: WeatherRepository
    private lateinit var getLocationDataUseCase: GetLocationDataUseCase
    private lateinit var getTomorrowWeatherUseCase: GetTomorrowWeatherUseCase
    private val calendar = Calendar.getInstance()
    private val dayMillis = 24 * 60 * 60 * 1000

    @Before
    fun setUp() {
        repository = Mockito.mock(WeatherRepository::class.java)
        getLocationDataUseCase = Mockito.mock(GetLocationDataUseCase::class.java)
        getTomorrowWeatherUseCase =
            GetTomorrowWeatherUseCaseImpl(repository, getLocationDataUseCase)
    }

    @Test
    fun `test when service of getting woeid failed`() = runBlocking<Unit> {
        `when`(getLocationDataUseCase()).thenReturn(Either.Error("service not available"))
        val result = getTomorrowWeatherUseCase()
        assertThat(result).isInstanceOf(Either.Error::class.java)
        assertThat((result as Either.Error).error).isEqualTo("service not available")
    }

    @Test
    fun `test when service of getting woeid and repository succeed`() = runBlocking<Unit> {
        calendar.timeInMillis = System.currentTimeMillis() + dayMillis

        `when`(getLocationDataUseCase()).thenReturn(Either.Success(1001))
        `when`(
            repository.getWeatherDataByDate(
                whereOnEarthID = 1001,
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1, //as month is zero index
                day = calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
            .thenReturn(
                Either.Success(
                    listOf(
                        Weather(
                            id = 1,
                            applicableDate = "2020-01-01",
                            created = "",
                            maxTemp = "10",
                            minTemp = "0",
                            theTemp = "5",
                            icon = "",
                            weatherStateName = "rainy",
                            city = "Tehran"
                        )
                    )
                )
            )

        val result = getTomorrowWeatherUseCase()
        assertThat(result).isInstanceOf(Either.Success::class.java)
        assertThat((result as Either.Success).data).isEqualTo(
            Weather(
                id = 1,
                applicableDate = "2020-01-01",
                created = "",
                maxTemp = "10",
                minTemp = "0",
                theTemp = "5",
                icon = "",
                weatherStateName = "rainy",
                city = "Tehran"
            )
        )
    }

    @Test
    fun `test when service of getting woeid succeed and repository failed `() = runBlocking<Unit> {
        calendar.timeInMillis = System.currentTimeMillis() + dayMillis
        `when`(getLocationDataUseCase()).thenReturn(Either.Success(2000))

        `when`(
            repository.getWeatherDataByDate(
                whereOnEarthID = 2000,
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1, //as month is zero index
                day = calendar.get(Calendar.DAY_OF_MONTH)
            )
        ).thenReturn(Either.Error("an error occurred!"))

        val result = getTomorrowWeatherUseCase()
        assertThat(result).isInstanceOf(Either.Error::class.java)
        assertThat((result as Either.Error).error).isEqualTo("an error occurred!")
    }
}