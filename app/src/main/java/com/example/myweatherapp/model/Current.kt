package com.example.myweatherapp.model

data class Current(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val feels_like: Double,
    val weather: List<Weather>
)
