package com.example.weatherapp.domain.model

data class WeatherData(
    val current: Current,
    val currentUnits: CurrentUnits
) {
    data class Current(
        val temperature: Double,
        val humidity: Double,
        val weatherType: WeatherType
    )

    data class CurrentUnits(
        val temperature: String,
        val humidity: String,
    )
}
