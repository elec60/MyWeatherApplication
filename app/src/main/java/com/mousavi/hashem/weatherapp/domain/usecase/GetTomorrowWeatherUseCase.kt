package com.mousavi.hashem.weatherapp.domain.usecase

import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import java.util.*
import javax.inject.Inject

class GetTomorrowWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
    private val getLocationDataUseCase: GetLocationDataUseCase,
) : GetTomorrowWeatherUseCase {

    private val calendar = Calendar.getInstance()
    private val dayMillis = 24 * 60 * 60 * 1000

    override suspend fun invoke(): Either<Weather, String> {
        calendar.timeInMillis = System.currentTimeMillis() + dayMillis
        when (val locationData = getLocationDataUseCase()) {
            is Either.Success -> {
                val weatherDataByDate = repository.getWeatherDataByDate(
                    whereOnEarthID = locationData.data,
                    year = calendar.get(Calendar.YEAR),
                    month = calendar.get(Calendar.MONTH) + 1, //as month is zero index
                    day = calendar.get(Calendar.DAY_OF_MONTH)
                )
                return when (weatherDataByDate) {
                    is Either.Success -> {
                        Either.Success(weatherDataByDate.data[0])//the most accurate prediction
                    }
                    is Either.Error -> {
                        Either.Error(weatherDataByDate.error)
                    }
                }
            }
            is Either.Error -> {
                return Either.Error(locationData.error)
            }
        }
    }
}

interface GetTomorrowWeatherUseCase {
    suspend operator fun invoke(): Either<Weather, String>
}