package com.example.myweatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.model.OneCallEntity
import com.example.myweatherapp.network.ServiceNetwork
import com.example.myweatherapp.util.Utils
import kotlinx.coroutines.launch
import java.io.IOException


class MainViewModel: ViewModel(){
    val service: ServiceNetwork = ServiceNetwork()
    val getWeather = MutableLiveData<OneCallEntity>()
    val badRequest = MutableLiveData<Boolean>()
    val utils: Utils = Utils()

    fun getWeatherById(lat:String,lon:String,units:String,lang:String,appid:String){
      viewModelScope.launch {
            try {
                val respuesta = service.getWeatherById(lat,lon,units,lang,appid)
                Log.e("codigo", respuesta.raw().toString())
                if (respuesta.isSuccessful) {
                    getWeather.postValue(respuesta.body())
                    Log.e("Success ", respuesta.body().toString())
                }
            }catch(e: IOException){
                utils
                Log.e("Response", e.toString())
            }
        }
    }
}