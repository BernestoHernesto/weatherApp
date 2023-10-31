package com.example.weatherapp.domain.repository

import com.example.weatherapp.util.Result
import com.example.weatherapp.domain.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherData(latitude: Double, longitude: Double): Flow<Result<WeatherData>>
}