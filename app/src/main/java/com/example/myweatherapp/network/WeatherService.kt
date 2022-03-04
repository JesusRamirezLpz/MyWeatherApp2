package com.example.myweatherapp.network

import com.example.myweatherapp.model.OneCallEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    //https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
    @GET("data/2.5/onecall")
    suspend fun getWeatherById(
        // @Query("id") lon: Long,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String?,
        @Query("lang") lang: String?,
        @Query("appid") appid: String): OneCallEntity
}