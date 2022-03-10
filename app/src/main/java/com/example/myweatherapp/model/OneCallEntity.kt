package com.example.myweatherapp.model

data class OneCallEntity(
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>
)
