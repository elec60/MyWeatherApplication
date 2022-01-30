package com.mousavi.hashem.weatherapp.data.remote

import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.data.StringProvider
import com.mousavi.hashem.weatherapp.data.remote.dto.LocationDataDto
import com.mousavi.hashem.weatherapp.data.remote.dto.WeatherDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val api: Api,
    private val stringProvider: StringProvider,
) : NetworkDataSource {

    override suspend fun getLocationData(name: String): Either<List<LocationDataDto>, String> {
        return try {
            val result = api.getLocationData(name)
            Either.Success(result)
        } catch (e: HttpException) {
            Either.Error(stringProvider.getString(R.string.error_occurred))
        } catch (e: IOException) {
            Either.Error(stringProvider.getString(R.string.check_internet_connection))
        } catch (e: Exception) {
            Either.Error(stringProvider.getString(R.string.unknown_error))
        }
    }

    override suspend fun getWeatherDataByDate(
        whereOnEarthID: Int,
        year: Int,
        month: Int,
        day: Int,
    ): Either<List<WeatherDto>, String> {
        return try {
            val result = api.getWeatherDataByDate(
                whereOnEarthID = whereOnEarthID,
                year = year,
                month = month,
                day = day
            )
            Either.Success(result)
        } catch (e: HttpException) {
            Either.Error(stringProvider.getString(R.string.error_occurred))
        } catch (e: IOException) {
            Either.Error(stringProvider.getString(R.string.check_internet_connection))
        } catch (e: Exception) {
            Either.Error(stringProvider.getString(R.string.unknown_error))
        }
    }
}