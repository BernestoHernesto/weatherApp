package com.example.weatherapp.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.util.Result
import com.example.weatherapp.util.UiEvent
import com.example.weatherapp.domain.model.Coordinates
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: WeatherData? = null,
)

@HiltViewModel
class WeatherDataViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel(), DefaultLifecycleObserver {

    private var loadingWeatherDataJob: Job? = null
    private val locationChangeInterval = 10000L
    private val coordinates: List<Coordinates> = listOf(
        Coordinates(53.619653, 10.079969),
        Coordinates(53.080917, 8.847533),
        Coordinates(52.378385, 9.794862),
        Coordinates(52.496385, 13.444041),
        Coordinates(53.866865, 10.739542),
        Coordinates(54.304540, 10.152741),
        Coordinates(54.797277, 9.491039),
        Coordinates(52.426412, 10.821392),
        Coordinates(53.542788, 8.613462),
        Coordinates(53.141598, 8.242565)
    )

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        startLoadingWeatherData()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        stopLoadingWeatherData()
    }

    private fun startLoadingWeatherData() {
        if (loadingWeatherDataJob == null || loadingWeatherDataJob?.isCancelled == true) {
            loadingWeatherDataJob = viewModelScope.launch {
                while (isActive) {
                    coordinates.forEach { coordinates ->
                        getWeatherData(coordinates)
                        delay(locationChangeInterval)
                    }
                }
            }
        }
    }

    private suspend fun getWeatherData(coordinates: Coordinates) {
        repository.getWeatherData(
            latitude = coordinates.latitude,
            longitude = coordinates.longitude
        ).collect { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        _uiState.value.copy(weatherData = result.data)
                    }
                }
                is Result.Loading -> _uiState.update {
                    _uiState.value.copy(isLoading = result.isLoading)
                }
                is Result.Error -> {
                    sendUiEvent(
                        UiEvent.ShowSnackbar(message = "Error: ${result.message}")
                    )
                }
            }
        }
    }

    private fun stopLoadingWeatherData() {
        loadingWeatherDataJob?.cancel()
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}