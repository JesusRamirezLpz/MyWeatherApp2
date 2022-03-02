package com.example.myweatherapp.model

data class Hourly(
    val dt: Long,
    val temp: Double,
    val weather: List<Weather>
)
