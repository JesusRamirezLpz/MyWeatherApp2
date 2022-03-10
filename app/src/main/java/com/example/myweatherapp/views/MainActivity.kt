package com.example.myweatherapp.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.myweatherapp.BuildConfig.APPLICATION_ID
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.ActivityClimaDiarioBinding
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.model.OneCallEntity
import com.example.myweatherapp.network.WeatherService
import com.example.myweatherapp.util.checkForInternet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val TAG = "MainActivityError"
    private var latitude = ""
    private var longitude = ""
    private var cityname:String =""
    private var country:String =""
    var adaptador: WeatherAdapter? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMainBinding
    private lateinit var _binding: ActivityClimaDiarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen()
        initcomponets()
        iniciarApp()
    }

   fun iniciarApp(){
        if(checkForInternet(this)) {
            if (!checkPermissions()) {
                Log.e(TAG, "reque")
                requestPermissions()
            } else {
                getLastLocation() { location ->
                    Log.e(TAG, "Error JERL 2")
                    setupViewDAta(location)
                }
            }
        }else{
            Log.e(TAG,"Error JERL 1")
            showError("Sin acceso a Internet")
            binding.detailsConstraintLayout.isVisible = false
        }
    }

    private fun initcomponets() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _binding = ActivityClimaDiarioBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    private suspend fun getWeather(): OneCallEntity = withContext(Dispatchers.IO){
        showIndicator(true)
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: WeatherService = retrofit.create(WeatherService::class.java)

        service.getWeatherById(latitude,longitude,"metric", "sp",
            "8ae5c025c39ad55772a706d4c481cb8d")
    }

    private fun setupViewDAta(location: Location){
        if(checkForInternet(this)){
            lifecycleScope.launch{
                latitude = location.latitude.toString()
                longitude = location.longitude.toString()
                formatResponse(getWeather())
                binding.detailsConstraintLayout.isVisible = true
            }
        }else{
            Log.e(TAG,"Error JERL 1")
            showError("Sin acceso a Internet")
            binding.detailsConstraintLayout.isVisible = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(onLocation: (location:Location) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnCompleteListener { taskLocation ->
                if (taskLocation.isSuccessful && taskLocation.result != null) {
                    val location = taskLocation.result
                    val lat = location.latitude
                    val lon = location.longitude
                    var geocoder = Geocoder(this)
                    val address : MutableList<Address>?
                    latitude = location?.latitude.toString()
                    longitude = location?.longitude.toString()
                    address = geocoder.getFromLocation(lat,lon,1)
                    country = address.get(0).countryCode
                    cityname = address.get(0).adminArea
                    Log.e(TAG, "Address: $address")
                    Log.e(TAG, "City: $cityname")
                    Log.e(TAG, "GetLasLoc Lat: $latitude Long: $longitude")
                    onLocation(taskLocation.result)
                } else {
                    Log.w(TAG,"getLastLocation:exception", taskLocation.exception)
                    showSnackbar(R.string.no_location_detected)
                }
            }
    }

    private fun showSnackbar(snackStrId: Int, actionStrId: Int = 0, listener: View.OnClickListener? = null){
        val snackbar = Snackbar.make(findViewById(android.R.id.content),getString(snackStrId),
            BaseTransientBottomBar.LENGTH_INDEFINITE
        )
        if (actionStrId != 0 && listener != null){
            snackbar.setAction(getString(actionStrId),listener)
        }
        snackbar.show()
    }

    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startLocationPermissionRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )){
            Log.i(TAG,"Muestra explicacion rationale para proveer un contexto adicional de porque se requiere el permiso")
            showSnackbar(R.string.permission_rationale, android.R.string.ok)
            startLocationPermissionRequest()
        }else{
            Log.i(TAG, "Solicitando permiso")
            startLocationPermissionRequest()
        }
    }

    private fun formatResponse(weatherEntity: OneCallEntity){
        showIndicator(true)
        try {
            adaptador = WeatherAdapter(weatherEntity.daily, this)
            val icon = weatherEntity.current.weather[0].icon
            val iconUrl = "https://openweathermap.org/img/w/$icon.png"
            val status = weatherEntity.current.weather[0].description.uppercase()
            val temp = "${weatherEntity.current.temp.toInt()}º"
            val wind = "${weatherEntity.current.wind_speed} km/h"
            val humidity = "${weatherEntity.current.humidity} %"
            val dt = weatherEntity.current.dt
            val currentDate: Long = dt
            val dateToday = "Hoy, "+SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
                .format(currentDate*1000)
            val dtHourly1 = weatherEntity.hourly[0].dt
            val Hour1 = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(dtHourly1*1000))
            val iconHourly1 = weatherEntity.hourly[0].weather[0].icon
            val iconHourlyUrl1 = "https://openweathermap.org/img/w/$iconHourly1.png"
            val tempHourly1 = "${weatherEntity.hourly[0].temp.toInt()}º"
            val dtHourly2 = weatherEntity.hourly[1].dt
            val Hour2 = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(dtHourly2*1000))
            val iconHourly2 = weatherEntity.hourly[1].weather[0].icon
            val iconHourlyUrl2 = "https://openweathermap.org/img/w/$iconHourly2.png"
            val tempHourly2 = "${weatherEntity.hourly[1].temp.toInt()}º"
            val dtHourly3 = weatherEntity.hourly[2].dt
            val Hour3 = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(dtHourly3*1000))
            val iconHourly3 = weatherEntity.hourly[2].weather[0].icon
            val iconHourlyUrl3 = "https://openweathermap.org/img/w/$iconHourly3.png"
            val tempHourly3 = "${weatherEntity.hourly[2].temp.toInt()}º"

            binding.apply {
                tvCityCountry.text = cityname + ", " + country
                ivIcon.load(iconUrl)
                tvDescription.text = status
                tvTemp.text = temp
                tvWind.text = wind
                tvHumidity.text = humidity
                tvDate.text = dateToday
                tvHour1.text = Hour1
                ivIcon1.load(iconHourlyUrl1)
                tvTemp1.text = tempHourly1
                tvHour2.text = Hour2
                ivIcon2.load(iconHourlyUrl2)
                tvTemp2.text = tempHourly2
                tvHour3.text = Hour3
                ivIcon3.load(iconHourlyUrl3)
                tvTemp3.text = tempHourly3
            }
            Log.e("JERL",weatherEntity.daily.toString())
            binding.recyclerClimaSemanal.layoutManager = LinearLayoutManager(this)
            binding.recyclerClimaSemanal.adapter = adaptador
            adaptador!!.notifyDataSetChanged()
            showIndicator(false)

        }catch(exception: Exception){
            Log.e("JERL", exception.toString())
            showError("Ha ocurrido un error")
            showIndicator(false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG,"onRequestPermissionsResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE){
            when{
                grantResults.isEmpty() -> Log.i(TAG, "La iteración del usuario fue cancelada.")
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> getLastLocation (this::setupViewDAta)

                else -> {
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings){
                        val intent = Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun showIndicator(visible: Boolean){
        binding.progressBarIndicator.isVisible = visible
    }

    private fun showError(message:String){
        Toast.makeText(this, message,Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        if(checkForInternet(this)) {
            val item = menu?.findItem(R.id.update)
            item?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.update -> {
                iniciarApp()
                item.isVisible = false
                Toast.makeText(this,"Refreshing...", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}