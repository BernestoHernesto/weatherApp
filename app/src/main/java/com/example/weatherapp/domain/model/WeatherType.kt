package com.example.weatherapp.domain.model

enum class WeatherType {
    CLEAR_SKY,
    CLOUDY,
    DRIZZLE,
    RAIN,
    SNOW,
    THUNDER;

    companion object {
        fun fromWMO(code: Int): WeatherType {
            return when(code) {
                0 -> CLEAR_SKY
                1,2,3, 45, 48 -> CLOUDY
                51, 53, 55, 56, 57 -> DRIZZLE
                61, 63, 65, 80, 81, 82 -> RAIN
                66, 67, 71, 73, 75, 77, 85, 86 -> SNOW
                95, 96, 99 -> THUNDER
                else -> CLEAR_SKY
            }
        }
    }
}