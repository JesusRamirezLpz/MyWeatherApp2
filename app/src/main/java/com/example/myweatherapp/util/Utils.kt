package com.example.myweatherapp.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class Utils {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun showMessage(context: Context, message: Int) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_LONG).show()
    }


    fun diaSemanaEspanol(diaSemanaIngles:String):String{
        var diaSemanaEspanol = ""
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


}