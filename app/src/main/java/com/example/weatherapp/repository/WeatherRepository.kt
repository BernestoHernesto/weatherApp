package com.example.weatherapp.repository

import com.example.weatherapp.domain.model.Coordinates
import com.example.weatherapp.util.Result
import com.example.weatherapp.domain.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeatherData(): Flow<Result<WeatherData>>
    fun getWeatherCoordinates(): List<Coordinates>
}