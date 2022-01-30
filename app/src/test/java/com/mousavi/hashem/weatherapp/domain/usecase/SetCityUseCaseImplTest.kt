package com.mousavi.hashem.weatherapp.domain.usecase


import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class SetCityUseCaseImplTest {

    private lateinit var repository: WeatherRepository
    private lateinit var setCityUseCase: SetCityUseCase

    @Before
    fun setUp() {
        repository = Mockito.mock(WeatherRepository::class.java)
        setCityUseCase = SetCityUseCaseImpl(repository)
    }

    @Test
    fun `test invoke must call repository method once`() = runBlocking<Unit> {
        setCityUseCase.invoke("London")
        verify(repository, times(1)).setCurrentCity("London")
    }
}