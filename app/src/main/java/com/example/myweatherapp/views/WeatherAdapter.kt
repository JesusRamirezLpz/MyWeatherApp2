package com.example.myweatherapp.views

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myweatherapp.R
import com.example.myweatherapp.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(val weather: List<Daily>, val activity: Activity): RecyclerView.Adapter<WeatherAdapter.WeatherHolder>() {

    fun diaSemanaEspanol(diaSemanaIngles:String):String{
        var diaSemanaEspanol: String = ""
        when(diaSemanaIngles){
            "Mon" -> diaSemanaEspanol = "Lun"
            "Tue" -> diaSemanaEspanol = "Mar"
            "Wed" -> diaSemanaEspanol = "Mie"
            "Thu" -> diaSemanaEspanol = "Jue"
            "Fri" -> diaSemanaEspanol = "Vie"
            "Sat" -> diaSemanaEspanol = "Sab"
            "Sun" -> diaSemanaEspanol = "Dom"
        }
        return diaSemanaEspanol
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherAdapter.WeatherHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return WeatherHolder(layoutInflater.inflate(R.layout.daily_weather,parent,false))
    }

    override fun onBindViewHolder(holder: WeatherAdapter.WeatherHolder, position: Int) {
       val weather = weather.get(position)

        with(holder){
            val icon = weather.weather[0].icon
            val iconUrl = "https://openweathermap.org/img/w/$icon.png"
            val diaSemanaIngles = SimpleDateFormat("E", Locale.forLanguageTag("es_MX")).format(weather.dt*1000)
            val diaSemanaEspanol = diaSemanaEspanol(diaSemanaIngles)
            tvDiaSemana.text = diaSemanaEspanol
            tvtempMax.text = "${weather.temp.max.toInt()}º/"
            tvtempMin.text = "${weather.temp.min.toInt()}º"
            tvDesc.text = "${weather.weather[0].description.uppercase()}"
            ivIconSemana.load(iconUrl)
        }
    }

    override fun getItemCount(): Int = weather.size

    class WeatherHolder(val view: View):RecyclerView.ViewHolder(view) {
        val tvDiaSemana: TextView = view.findViewById(R.id.tv_dia_semana)
        val tvtempMax: TextView = view.findViewById(R.id.tv_tempMax)
        val tvtempMin: TextView = view.findViewById(R.id.tv_tempMin)
        val tvDesc: TextView = view.findViewById(R.id.tv_desc)
        val ivIconSemana: ImageView = view.findViewById(R.id.iv_icon_semana)
    }
}

