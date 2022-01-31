package com.mousavi.hashem.weatherapp.domain.usecase

import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.data.StringProvider
import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetLocationDataUseCaseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
    private val stringProvider: StringProvider,
) : GetLocationDataUseCase {

    override suspend fun invoke(): Either<Int, String> {
        return when (val locationData = repository.getLocationData()) {
            is Either.Success -> {
                if (locationData.data.isEmpty()) {
                    Either.Error(stringProvider.getString(R.string.no_data_for_this_city))
                } else {
                    Either.Success(locationData.data[0].whereOnEarthID)
                }
            }
            is Either.Error -> {
                Either.Error(locationData.error)
            }
        }
    }
}

interface GetLocationDataUseCase {
    suspend operator fun invoke(): Either<Int, String>
}