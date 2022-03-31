package com.example.myweatherapp.network

import com.example.myweatherapp.model.OneCallEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ServiceNetwork {
    val retrofit = RetrofitInstance.getRetrofit().create(WeatherService::class.java)

    suspend fun getWeatherById(lat:String,lon:String,units:String,lang:String,appid:String): Response<OneCallEntity> {
        return withContext(Dispatchers.IO){
            val response = retrofit.getWeatherById(lat,lon,units,lang,appid)
            response
        }
    }
}