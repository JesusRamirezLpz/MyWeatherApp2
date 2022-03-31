package com.example.myweatherapp.views

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myweatherapp.R
import com.example.myweatherapp.model.Daily
import com.example.myweatherapp.util.Utils
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(val weather: List<Daily>, val activity: Activity): RecyclerView.Adapter<WeatherAdapter.WeatherHolder>() {
    val utils: Utils = Utils()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return WeatherHolder(layoutInflater.inflate(R.layout.daily_weather,parent,false))
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val weather = weather.get(position)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        with(holder){
            val icon = weather.weather[0].icon
            val iconUrl = "https://openweathermap.org/img/w/$icon.png"
            val diaSemanaIngles = SimpleDateFormat("E", Locale.ENGLISH).format(weather.dt*1000)
            if (!sharedPreferences.getBoolean("language",false)){
                val diaSemanaEspanol = utils.diaSemanaEspanol(diaSemanaIngles)
                tvDiaSemana.text = diaSemanaEspanol
            }else{
                tvDiaSemana.text = diaSemanaIngles
            }
            tvtempMax.text = "${weather.temp.max.toInt()}º/"
            tvtempMin.text = "${weather.temp.min.toInt()}º"
            tvDesc.text = weather.weather[0].description.uppercase()
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

