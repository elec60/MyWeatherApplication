package com.mousavi.hashem.weatherapp.presentation.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mousavi.hashem.weatherapp.common.Either
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import com.mousavi.hashem.weatherapp.domain.usecase.GetTomorrowWeatherUseCase
import com.mousavi.hashem.weatherapp.domain.usecase.SetCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val setCityUseCase: SetCityUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private var _citySet = MutableSharedFlow<Boolean>()
    val citySet = _citySet.asSharedFlow()


    fun setCity(name: String){
        viewModelScope.launch(dispatcher) {
            _citySet.emit(setCityUseCase(name))
        }
    }
}