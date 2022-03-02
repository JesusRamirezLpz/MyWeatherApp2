package com.example.myweatherapp.network

import com.example.myweatherapp.model.*

data class WeatherEntity(
    val timezone: String,
    val current: Current
)

