package com.example.weatherapp.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.util.Result
import com.example.weatherapp.util.UiEvent
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
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
                    getWeatherData()
                }
            }
        }
    }

    private suspend fun getWeatherData() {
        repository.getWeatherData().collect { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(weatherData = result.data)
                    }
                }
                is Result.Loading -> _uiState.update {
                    it.copy(isLoading = result.isLoading)
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