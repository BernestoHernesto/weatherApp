package com.example.weatherapp.data.remote

import com.squareup.moshi.Json

data class WeatherDataResponse(
    val current: Current,
    @field:Json(name = "current_units") val currentUnits: CurrentUnits
) {
    data class Current(
        @field:Json(name = "temperature_2m") val temperature: Double,
        @field:Json(name = "relativehumidity_2m") val humidity: Double,
        val weathercode: Int
    )

    data class CurrentUnits(
        @field:Json(name = "temperature_2m") val temperature: String,
        @field:Json(name = "relativehumidity_2m") val humidity: String,
    )
}
