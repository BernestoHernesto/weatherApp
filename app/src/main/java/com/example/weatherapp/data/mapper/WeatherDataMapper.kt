package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.remote.WeatherDataResponse
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.model.WeatherType

fun WeatherDataResponse.toWeatherData(): WeatherData {
    return WeatherData(
        current = WeatherData.Current(
            temperature = current.temperature,
            humidity = current.humidity,
            weatherType = WeatherType.fromWMO(current.weathercode)
        ),
        currentUnits = WeatherData.CurrentUnits(
            temperature = currentUnits.temperature,
            humidity = currentUnits.humidity,
        )
    )
}