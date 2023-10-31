package com.example.weatherapp.data.repository

import com.example.weatherapp.util.Result
import com.example.weatherapp.data.mapper.toWeatherData
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherData(latitude: Double, longitude: Double): Flow<Result<WeatherData>> {
        return flow {
            emit(Result.Loading(true))
            try {
                val weatherData = weatherApi.getWeatherData(latitude = latitude, longitude = longitude)
                    .toWeatherData()
                emit(Result.Success(weatherData))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message ?: "An error occurred."))
            }
            emit(Result.Loading(false))
        }.flowOn(Dispatchers.IO)
    }
}