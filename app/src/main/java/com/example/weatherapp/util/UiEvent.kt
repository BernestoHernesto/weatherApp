package com.example.weatherapp.util

sealed class UiEvent {
    data class ShowSnackbar(
        val message: String
    ): UiEvent()
}