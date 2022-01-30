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

    private var _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var _weather = MutableStateFlow(Weather.Default)
    val weather = _weather.asStateFlow()

    private var _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    fun getTomorrowWeather() {
        viewModelScope.launch(dispatcher) {
            _loading.value = true
            val result = getTomorrowWeatherUseCase()
            _loading.value = false
            when (result) {
                is Either.Success -> {
                    _weather.value = result.data
                }
                is Either.Error -> {

                }
            }

        }
    }
}