package com.mousavi.hashem.weatherapp.domain.usecase

import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class SetCityUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
) : SetCityUseCase {

    override suspend fun invoke(name: String): Boolean {
       return repository.setCurrentCity(name)
    }
}

interface SetCityUseCase {
    suspend operator fun invoke(name: String): Boolean
}