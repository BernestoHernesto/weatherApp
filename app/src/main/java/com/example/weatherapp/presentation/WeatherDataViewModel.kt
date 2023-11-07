package com.example.weatherapp.presentation

import androidx.lifecycle.ViewModel
import com.example.weatherapp.util.Result
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    data class Success(val weatherData: WeatherData) : UiState()
    data class Error(val message: String) : UiState()
}


@HiltViewModel
class WeatherDataViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val userFlow: Flow<UiState> = repository.getWeatherData().map {
        when (it) {
            is Result.Loading -> UiState.Loading
            is Result.Success -> UiState.Success(it.data)
            is Result.Error -> UiState.Error(it.toString())
        }
    }

}