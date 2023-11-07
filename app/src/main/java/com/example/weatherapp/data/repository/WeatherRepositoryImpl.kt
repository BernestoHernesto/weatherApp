package com.example.weatherapp.data.repository

import com.example.weatherapp.util.Result
import com.example.weatherapp.data.mapper.toWeatherData
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.domain.model.Coordinates
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.delay
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRepository {

    private val refreshIntervalMs: Long = 10000L

    override fun getWeatherData(): Flow<Result<WeatherData>> {
        return flow {
            while (true) {
                getWeatherCoordinates().forEach {
                    emit(Result.Loading)
                    try {
                        val weatherData = weatherApi.getWeatherData(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                            .toWeatherData()
                        emit(Result.Success(weatherData))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emit(Result.Error(e))
                    }
                    delay(refreshIntervalMs)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getWeatherCoordinates(): List<Coordinates> {
        return listOf(
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
    }
}