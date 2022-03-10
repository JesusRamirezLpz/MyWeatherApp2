package com.example.myweatherapp.model

data class Daily(
    val dt: Long,
    val temp: Temp,
    val weather: List<Weather>
)
