package com.mousavi.hashem.weatherapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.domain.usecase.GetTomorrowWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTomorrowWeatherUseCase: GetTomorrowWeatherUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private var _weatherState = MutableStateFlow(WeatherState())
    val weatherState = _weatherState

    fun getTomorrowWeather() {
        viewModelScope.launch(dispatcher) {
            _weatherState.value = weatherState.value.copy(loading = true)
            val result = getTomorrowWeatherUseCase()
            _weatherState.value = weatherState.value.copy(loading = false)
            when (result) {
                is Either.Success -> {
                    _weatherState.value = weatherState.value.copy(weather = result.data)
                }
                is Either.Error -> {

                }
            }

        }
    }
}