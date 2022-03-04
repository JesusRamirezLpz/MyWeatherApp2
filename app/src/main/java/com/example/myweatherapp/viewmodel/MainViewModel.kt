package com.example.myweatherapp.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.myweatherapp.model.OneCallEntity
import com.example.myweatherapp.network.WeatherService
import com.example.myweatherapp.util.checkForInternet
import com.example.myweatherapp.views.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import androidx.lifecycle.lifecycleScope
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel: ViewModel(){
    /*val service = WeatherService


    private suspend fun getWeather(): OneCallEntity = withContext(Dispatchers.IO){
        //setupTitle("WeatherApp")
        //showIndicator(true)
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: WeatherService = retrofit.create(WeatherService::class.java)
        Log.e(TAG,"Latitude: $latitude Longitud: $longitude")

        service.getWeatherById(latitude,longitude,"metric", "sp",
            "8ae5c025c39ad55772a706d4c481cb8d")

    }

    fun setupViewDAta(location: Location){
        lifecycleScope.launch{
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
            //formatResponse(getWeather())
            //binding.detailsConstraintLayout.isVisible = true
            //binding.detailsContainer.isVisible = true
        }
    }*/
}